package dom.modules.reportes;

import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;

import java.awt.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class ReporteCliente extends ReporteBase {
	private final static String HORAS = "Hora(s),";
	private final static String MINUTOS = "Minuto(s)";

	String sql;

	public ReporteCliente(int idCliente, Date desde, Date hasta) {
		super();
		nombreReporte = "ReporteCliente-" + Integer.toString(idCliente) + "-" + getFechaString("yyyyMMdd-HHmmss") + "" +
				".pdf";
		buildSql(idCliente, desde, hasta);
	}

	private void buildSql(int idCliente, Date desde, Date hasta) {
		String sqlTmp = "select h.fecha, h.cliente_id_oid, c.nombre, h.abogado_id_oid, a.username, h.fecha, h" +
				".formaservicio_id_oid,  " +
				"f.nombre tipo_servicio, h.solicitadopor,h.caso,h.servicio,h.horasfacturables,h.minutosfacturables, " +
				"(horasfacturables * 3600 + minutosfacturables * 60) tiemporeal " +
				"from hojadetiempo h " +
				"join cliente c on c.id = h.cliente_id_oid " +
				"join applicationuser a on a.id=h.abogado_id_oid " +
				"join formaservicio f on f.id = h.formaservicio_id_oid " +
				"where c.id = %d and h.fecha >= '%TY-%Tm-%Td' and h.fecha <= '%TY-%Tm-%Td' " +
				"order by a.username, h.fecha, h.caso";
		sql = String.format(sqlTmp, idCliente, desde, desde, desde, hasta, hasta, hasta);
		LOG.info(sql);
	}

	public void build() throws IOException, SQLException {
		buildDataSource();
		rep.setDataSource(sql, conn);
		StyleBuilder arialStyle = stl.style()
				.setFontName("Arial")
				.setFontSize(8);
		StyleBuilder columnTitleStyle = stl.style(arialStyle)
				.bold().setFontSize(10)
				.setHorizontalAlignment(HorizontalAlignment.CENTER)
				.setTopBorder(stl.pen1Point().setLineColor(Color.GRAY))
				.setBottomBorder(stl.pen1Point().setLineColor(Color.GRAY));
		StyleBuilder arialBoldStyle = stl
				.style(arialStyle).bold()
				.setFontSize(14);
		StyleBuilder piePaginaStyle = stl.style(arialStyle)
				.bold()
				.setFontSize(12)
				.setHorizontalAlignment(HorizontalAlignment.CENTER);
		StyleBuilder subtotalStyle = stl.style(arialStyle)
				.bold()
				.setFontSize(12)
				.setTopBorder(stl.pen1Point())
				.setHorizontalAlignment(HorizontalAlignment.CENTER);
		rep.pageFooter(cmp.pageXofY().setStyle(piePaginaStyle));

		TextColumnBuilder<String> columnaCliente = col.column("", "nombre", type.stringType()).setStyle(arialBoldStyle);
		ColumnGroupBuilder itemGroup = grp.group(columnaCliente).setHideColumn(true);
		TextColumnBuilder<Long> columnaTiempo = col.column("Total", "tiemporeal", type.longType()).setValueFormatter(new
				TiempoRealFormatter());
		VariableBuilder<Long> sumaTiempo = variable(columnaTiempo, Calculation.SUM);
		rep.variables(sumaTiempo);
		TextFieldBuilder<String> groupSbt = cmp.text(new CustomTextSubtotal(sumaTiempo)).setStyle(subtotalStyle);
		rep.groupBy(itemGroup);
		itemGroup.footer(groupSbt);

		rep.columns(
				col.column("Abogado", "username", type.stringType())
						.setFixedColumns(6)
						.setHorizontalAlignment(HorizontalAlignment.CENTER)
						.setStyle(arialStyle)
						.setTitleStyle(columnTitleStyle), //.setLeftBorder(stl.pen1Point())),
				col.column("Fecha", "fecha", type.dateType())
						.setFixedColumns(8)
						.setHorizontalAlignment(HorizontalAlignment.CENTER)
						.setStyle(arialStyle)
						.setTitleStyle(columnTitleStyle),
				col.column("Consulta", "tipo_servicio", type.stringType())
						.setHorizontalAlignment(HorizontalAlignment.LEFT)
						.setStyle(arialStyle.setRightPadding(10))
						.setTitleStyle(columnTitleStyle),
				col.column("Solicitante", "solicitadopor", type.stringType())
						.setStyle(arialStyle)
						.setTitleStyle(columnTitleStyle),
				col.column("Referencia", "caso", type.stringType())
						.setTitleStyle(columnTitleStyle)
						.setStyle(arialStyle)
						.setWidth(100),
				col.column("Detalle", "servicio", type.stringType())
						.setTitleStyle(columnTitleStyle)
						.setStyle(arialStyle)
						.setWidth(170),
				columnaTiempo.setStyle(arialStyle)
						.setTitleStyle(columnTitleStyle) //.setRightBorder(stl.pen1Point()))
						.setWidth(130)
		);
	}

	public class TiempoRealFormatter extends AbstractValueFormatter<String, Long> {
		private static final long serialVersionUID = 1L;

		public TiempoRealFormatter() {
		}

		@Override
		public String format(Long segundosReales, ReportParameters reportParameters) {
			Long horas = segundosReales / 3600;
			Long minutos = (segundosReales - (horas * 3600)) / 60;
			return String.format("%01d " + HORAS + " %02d " + MINUTOS, horas, minutos);
		}
	}

	private class CustomTextSubtotal extends AbstractSimpleExpression<String> {
		private static final long serialVersionUID = 1L;

		private VariableBuilder<Long> totalSegundosReales;

		public CustomTextSubtotal(VariableBuilder<Long> segundosReales) {
			this.totalSegundosReales = segundosReales;
		}

		@Override
		public String evaluate(ReportParameters reportParameters) {
			Long segundosReales = reportParameters.getValue(totalSegundosReales);
			Long horas = segundosReales / 3600;
			Long minutos = (segundosReales - (horas * 3600)) / 60;
			return reportParameters.getValue("nombre") + " - " + String.format("%01d " + HORAS + " %02d " + MINUTOS,
					horas, minutos);
		}
	}

}