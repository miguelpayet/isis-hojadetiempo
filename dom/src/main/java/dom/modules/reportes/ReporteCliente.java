package dom.modules.reportes;

import dom.modules.clientes.Cliente;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.definition.ReportParameters;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class ReporteCliente extends ReporteBase {

	protected Cliente cliente;
	protected Date desde;
	protected Date hasta;

	protected class SubtotalCliente extends AbstractSimpleExpression<String> {

		protected VariableBuilder<Long> totalSegundosReales;

		public SubtotalCliente(VariableBuilder<Long> segundosReales) {
			this.totalSegundosReales = segundosReales;
		}

		@Override
		public String evaluate(ReportParameters reportParameters) {
			Long segundosReales = reportParameters.getValue(totalSegundosReales);
			Long horas = segundosReales / 3600;
			Long minutos = (segundosReales - (horas * 3600)) / 60;
			return reportParameters.getValue("nombre") + " - " + formatearHoras(horas, minutos);
		}
	}

	public ReporteCliente(Idioma idioma, Cliente cliente, Date desde, Date hasta) {
		super(idioma, cliente.getNombre());
		this.cliente = cliente;
		this.desde = desde;
		this.hasta = hasta;
		setNombre("RC", cliente.getNombre());
	}

	protected void applySqlParams() {
		sql = String.format(sql, cliente.getId(), desde, desde, desde, hasta, hasta, hasta);
	}

	public void build() throws IOException, SQLException {
		super.build();

		TextColumnBuilder<String> columnaCliente = col.column("", "nombre", type.stringType()).setStyle(arialBoldStyle);
		ColumnGroupBuilder itemGroup = grp.group(columnaCliente)
				.setHideColumn(true)
				.setHeaderLayout(GroupHeaderLayout.EMPTY);
		TextColumnBuilder<Long> columnaTiempo = col.column("Total", "tiemporeal", type.longType()).
				setValueFormatter(new TiempoRealFormatter());
		VariableBuilder<Long> sumaTiempo = variable(columnaTiempo, Calculation.SUM);
		rep.variables(sumaTiempo);
		TextFieldBuilder<String> groupSbt = cmp.text(new SubtotalCliente(sumaTiempo)).setStyle(subtotalStyle);
		rep.groupBy(itemGroup);
		itemGroup.footer(groupSbt);

		rep.columns(
				columnaAbogado(),
				columnaVacia(),
				columnaFecha(),
				columnaVacia(),
				columnaConsulta(),
				columnaVacia(),
				columnaSolicitante(),
				columnaVacia(),
				columnaReferencia(),
				columnaVacia(),
				columnaDetalles(),
				columnaVacia(),
				columnaTiempo.setStyle(arialRightStyle).setWidth(99).setTitleStyle(columnTitleStyle)
		);
	}

	protected void buildSqlOrder() {
		sql = sql + " order by a.username, h.fecha, caso";
	}

	protected void buildSqlWhere() {
		sql = sql + " where c.id = %d and h.fecha >= '%TY-%Tm-%Td' and h.fecha <= '%TY-%Tm-%Td' ";
	}

}