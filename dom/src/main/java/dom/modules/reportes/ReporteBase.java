package dom.modules.reportes;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.component.PageXofYBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.isis.applib.value.Blob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public abstract class ReporteBase {

	protected final static Logger LOG = LoggerFactory.getLogger(ReporteCliente.class);
	StyleBuilder arialBoldStyle;
	StyleBuilder arialStyle;
	StyleBuilder columnTitleStyle;
	Connection conn;
	String finalFilename;
	Idioma idioma;
	String nombreReporte;
	StyleBuilder piePaginaStyle;
	JasperReportBuilder rep;
	protected String sql;
	StyleBuilder subtotalStyle;
	String titulo;

	protected class HeaderReporteExpression extends AbstractSimpleExpression<String> {
		String valor;

		public HeaderReporteExpression(String valor) {
			this.valor = valor;
		}

		public String evaluate(ReportParameters reportParameters) {
			if (reportParameters.getPageNumber() == 1) {
				return valor;
			} else {
				return "";
			}
		}
	}

	protected class TiempoRealFormatter extends AbstractValueFormatter<String, Long> {
		private static final long serialVersionUID = 1L;

		public TiempoRealFormatter() {
		}

		@Override
		public String format(Long segundosReales, ReportParameters reportParameters) {
			Long horas = segundosReales / 3600;
			Long minutos = (segundosReales - (horas * 3600)) / 60;
			return String.format("%01d " + idioma.getString("horas") + " %02d " + idioma.getString("minutos"), horas,
					minutos);
		}
	}

	public ReporteBase(Idioma idioma, String titulo) {
		this.idioma = idioma;
		this.titulo = titulo;
		buildStyles();
		buildReporte();
	}

	abstract void applySqlParams();

	protected void build() throws IOException, SQLException {
		buildSql();
		buildDataSource();
		rep.setDataSource(sql, conn);
	}

	public Blob buildBlob() throws IOException, DRException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		rep.toPdf(baos);
		baos.flush();
		baos.close();
		return new Blob(nombreReporte, "application/pdf", baos.toByteArray());
	}

	protected void buildDataSource() throws IOException, SQLException {
		PropertiesFile pr = new PropertiesFile("WEB-INF/persistor.properties");
		String url = pr.getProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionURL");
		String user = pr.getProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionUserName");
		String password = pr.getProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionUserName");
		conn = DriverManager.getConnection(url, user, password);
	}

	protected JasperPdfExporterBuilder buildPdfExporter() throws IOException {
		File f = File.createTempFile("reporte", ".pdf");
		f.delete();
		finalFilename = f.getPath();
		LOG.info(finalFilename);
		return export.pdfExporter(finalFilename).setEncrypted(false);
	}

	protected void buildReporte() {
		rep = report();
		rep.setLocale(Locale.forLanguageTag(idioma.getCodigo()));
		rep.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);
		rep.pageHeader(cmp.verticalList(cmp.horizontalList(
						cmp.text(getFechaString("dd-MM-yyyy")).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
						cmp.image(getClass().getClassLoader().getResourceAsStream("images/logo.png")).setDimension(75, 75)
								.setHorizontalImageAlignment(HorizontalImageAlignment.RIGHT))),
				cmp.text(new HeaderReporteExpression(titulo)).setStyle(arialBoldStyle));
		rep.setDetailFooterSplitType(SplitType.PREVENT);
		rep.pageHeader(cmp.verticalGap(10));
		rep.columnHeader(cmp.verticalGap(10));
		rep.addDetailFooter(cmp.verticalGap(5));
		rep.addDetailHeader(cmp.verticalGap(5));
		rep.detailFooter(cmp.line().setPen(stl.pen1Point()));
		PageXofYBuilder pageXofYBuilder = cmp.pageXofY().setStyle(piePaginaStyle).setFormatExpression(idioma.getString
				("paginador"));
		rep.pageFooter(pageXofYBuilder);
	}

	protected void buildSql() {
		buildSqlSelect();
		buildSqlWhere();
		buildSqlOrder();
		applySqlParams();
		LOG.info(sql);
	}

	abstract void buildSqlOrder();

	protected void buildSqlSelect() {
		sql = "SELECT h.fecha, c.nombre, a.username, f.nombre tipo_servicio,  f.nombreingles tipo_servicio_en, " +
				"h.solicitadopor, ca.nombre caso, h.servicio, (horasreales * 3600 + minutosreales * 60) tiemporeal, " +
				"(horasfacturables * 3600 + minutosfacturables * 60) tiempofacturable " +
				"FROM hojadetiempo h " +
				"JOIN cliente c ON c.id = h.cliente_id_oid " +
				"JOIN caso ca on ca.id = h.caso_id_oid " +
				"JOIN applicationuser a ON a.id = h.abogado_id_oid " +
				"JOIN formaservicio f ON f.id = h.formaservicio_id_oid ";
	}

	abstract void buildSqlWhere();

	protected void buildStyles() {
		arialStyle = stl.style()
				.setFontName("Arial").setPadding(0).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
				.setFontSize(8);
		columnTitleStyle = stl.style(arialStyle)
				.bold().setFontSize(10)
				.setPadding(0)
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
				.setTopBorder(stl.pen1Point().setLineColor(Color.GRAY))
				.setBottomBorder(stl.pen1Point().setLineColor(Color.GRAY));
		arialBoldStyle = stl.style(arialStyle).bold()
				.setFontSize(14);
		piePaginaStyle = stl.style(arialStyle)
				.bold()
				.setFontSize(10)
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
		subtotalStyle = stl.style(arialStyle)
				.bold()
				.setFontSize(12)
				.setTopBorder(stl.pen1Point())
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
	}

	protected ColumnBuilder columnaAbogado() {
		return col.column(idioma.getString("abogado"), "username", type.stringType())
				.setWidth(70)
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
				.setStyle(arialStyle)
				.setTitleStyle(columnTitleStyle.setLeftBorder(stl.pen1Point()));
	}

	protected ColumnBuilder columnaConsulta() {
		return col.column(idioma.getString("consulta"), idioma.getString("campo-tipo-servicio"), type.stringType())
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
				.setStyle(arialStyle.setRightPadding(10))
				.setTitleStyle(columnTitleStyle);
	}

	protected ColumnBuilder columnaDetalles() {
		return col.column(idioma.getString("detalles"), "servicio", type.stringType())
				.setTitleStyle(columnTitleStyle)
				.setStyle(arialStyle)
				.setWidth(170);
	}

	protected ColumnBuilder columnaFecha() {
		return col.column(idioma.getString("fecha"), "fecha", type.dateType())
				.setWidth(70)
				.setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
				.setStyle(arialStyle)
				.setTitleStyle(columnTitleStyle);
	}

	protected ColumnBuilder columnaReferencia() {
		return col.column(idioma.getString("referencia"), "caso", type.stringType())
				.setTitleStyle(columnTitleStyle)
				.setStyle(arialStyle)
				.setWidth(100);
	}

	protected ColumnBuilder columnaSolicitante() {
		return col.column(idioma.getString("solicitante"), "solicitadopor", type.stringType())
				.setStyle(arialStyle)
				.setTitleStyle(columnTitleStyle);
	}

	protected String formatearHoras(Long horas, Long minutos) {
		return String.format("%01d " + idioma.getString("horas") + " %02d " + idioma.getString("minutos"), horas,
				minutos);
	}

	public String getFechaString(String formato) {
		SimpleDateFormat sdfDate = new SimpleDateFormat(formato);
		java.util.Date now = new java.util.Date();
		return sdfDate.format(now);
	}

	protected void setNombre(String prefijo, String detalle) {
		nombreReporte = String.format("%s-%s-%s.pdf", prefijo, detalle.replace(" ", "-"), getFechaString
				("yyyyMMdd-HHmmss"));
	}

}
