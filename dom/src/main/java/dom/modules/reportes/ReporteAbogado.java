package dom.modules.reportes;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import org.isisaddons.module.security.dom.user.ApplicationUser;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class ReporteAbogado extends ReporteBase {

	protected ApplicationUser abogado;
	protected Date desde;
	protected Date hasta;

	protected class SubtotalAbogado extends AbstractSimpleExpression<String> {

		protected VariableBuilder<Long> totalSegundosReales;

		public SubtotalAbogado(VariableBuilder<Long> segundosReales) {
			this.totalSegundosReales = segundosReales;
		}

		@Override
		public String evaluate(ReportParameters reportParameters) {
			Long segundosReales = reportParameters.getValue(totalSegundosReales);
			Long horas = segundosReales / 3600;
			Long minutos = (segundosReales - (horas * 3600)) / 60;
			return reportParameters.getValue("username") + " - " + formatearHoras(horas, minutos);
		}
	}

	public ReporteAbogado(Idioma idioma, ApplicationUser abogado, Date desde, Date hasta) {
		super(idioma);
		this.abogado = abogado;
		this.desde = desde;
		this.hasta = hasta;
		setNombre("RA", abogado.getUsername());
	}

	protected void applySqlParams() {
		sql = String.format(sql, abogado.getUsername(), desde, desde, desde, hasta, hasta, hasta);
	}

	public void build() throws IOException, SQLException {
		super.build();

		TextColumnBuilder<String> columnaAbogado = col.column("", "username", type.stringType()).setStyle
				(arialBoldStyle);
		ColumnGroupBuilder itemGroup = grp.group(columnaAbogado).setHideColumn(true);
		TextColumnBuilder<Long> columnaTiempoReal = col.column(idioma.getString("tiempo-real"), "tiemporeal", type
				.longType()).
				setValueFormatter(new TiempoRealFormatter());
		TextColumnBuilder<Long> columnaTiempoFact = col.column(idioma.getString("tiempo-facturable"),
				"tiempofacturable", type.longType())
				.setValueFormatter(new TiempoRealFormatter());
		VariableBuilder<Long> sumaTiempoReal = variable(columnaTiempoReal, Calculation.SUM);
		VariableBuilder<Long> sumaTiempoFact = variable(columnaTiempoReal, Calculation.SUM);
		rep.variables(sumaTiempoReal, sumaTiempoFact);
		TextFieldBuilder<String> groupSbt = cmp.text(new SubtotalAbogado(sumaTiempoReal)).setStyle(subtotalStyle);
		rep.groupBy(itemGroup);
		itemGroup.footer(groupSbt);

		rep.columns(
				col.column(idioma.getString("cliente"), "nombre", type.stringType())
						.setWidth(60)
						.setHorizontalAlignment(HorizontalAlignment.CENTER)
						.setStyle(arialStyle)
						.setTitleStyle(columnTitleStyle.setLeftBorder(stl.pen1Point())),
				col.column(idioma.getString("fecha"), "fecha", type.dateType())
						.setFixedColumns(8)
						.setHorizontalAlignment(HorizontalAlignment.CENTER)
						.setStyle(arialStyle)
						.setTitleStyle(columnTitleStyle),
				col.column(idioma.getString("consulta"), idioma.getString("campo-tipo-servicio"), type.stringType())
						.setHorizontalAlignment(HorizontalAlignment.LEFT)
						.setStyle(arialStyle.setRightPadding(10))
						.setTitleStyle(columnTitleStyle),
				col.column(idioma.getString("solicitante"), "solicitadopor", type.stringType())
						.setStyle(arialStyle)
						.setTitleStyle(columnTitleStyle),
				col.column(idioma.getString("referencia"), "caso", type.stringType())
						.setTitleStyle(columnTitleStyle)
						.setStyle(arialStyle)
						.setWidth(100),
				col.column(idioma.getString("detalles"), "servicio", type.stringType())
						.setTitleStyle(columnTitleStyle)
						.setStyle(arialStyle)
						.setWidth(100),
				columnaTiempoReal.setStyle(arialStyle)
						.setTitleStyle(columnTitleStyle)
						.setWidth(70),
				columnaTiempoFact.setStyle(arialStyle)
						.setTitleStyle(columnTitleStyle.setRightBorder(stl.pen1Point()))
						.setWidth(70)
		);
	}

	protected void buildSqlOrder() {
		sql = sql + " order by a.username, h.fecha, h.caso";
	}

	protected void buildSqlSelect() {
		sql = "SELECT h.fecha, c.nombre, a.username, f.nombre tipo_servicio,  f.nombreingles tipo_servicio_en, " +
				"h.solicitadopor, h.caso, h.servicio, (horasreales * 3600 + minutosreales * 60) tiemporeal, " +
				"(horasfacturables * 3600 + minutosfacturables * 60) tiempofacturable " +
				"FROM hojadetiempo h " +
				"JOIN cliente c ON c.id = h.cliente_id_oid " +
				"JOIN applicationuser a ON a.id = h.abogado_id_oid " +
				"JOIN formaservicio f ON f.id = h.formaservicio_id_oid ";
	}

	protected void buildSqlWhere() {
		sql = sql + " WHERE a.username = (select username from applicationuser au where au.username = '%s') " +
				"AND h.fecha >= '%TY-%Tm-%Td' AND h.fecha <= '%TY-%Tm-%Td'";
	}

}
