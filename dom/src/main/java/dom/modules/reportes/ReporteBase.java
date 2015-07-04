package dom.modules.reportes;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.SplitType;
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

	protected final static String HORAS = "Hora(s),";
	protected final static Logger LOG = LoggerFactory.getLogger(ReporteCliente.class);
	protected final static String MINUTOS = "Minuto(s)";
	StyleBuilder arialBoldStyle;
	StyleBuilder arialStyle;
	StyleBuilder columnTitleStyle;
	Connection conn;
	String finalFilename;
	String nombreReporte;
	StyleBuilder piePaginaStyle;
	JasperReportBuilder rep;
	protected String sql;
	StyleBuilder subtotalStyle;

	protected class TiempoRealFormatter extends AbstractValueFormatter<String, Long> {
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

	public ReporteBase() {
		buildReporte();
		buildStyles();
	}

	abstract void applySqlParams();

	protected void build() throws IOException, SQLException {
		buildSql();
		buildDataSource();
		rep.setDataSource(sql, conn);
		rep.pageFooter(cmp.pageXofY().setStyle(piePaginaStyle));
	}

	public Blob buildBlob() throws IOException, DRException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		rep.toPdf(baos);
		baos.flush();
		baos.close();
		return new Blob(nombreReporte, "application/octet-stream", baos.toByteArray());
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
		rep.setLocale(Locale.forLanguageTag("ES"));
		rep.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);
		rep.pageHeader(cmp.horizontalList(
				cmp.text(getFechaString("dd-MM-yyyy")).setHorizontalAlignment(HorizontalAlignment.LEFT),
				cmp.image(getClass().getClassLoader().getResourceAsStream("images/logo.png")).setDimension(75, 75)
						.setHorizontalAlignment(HorizontalAlignment.RIGHT)));
		rep.setDetailFooterSplitType(SplitType.PREVENT);
		rep.pageHeader(cmp.verticalGap(10));
		rep.columnHeader(cmp.verticalGap(10));
		rep.addDetailFooter(cmp.verticalGap(5));
		rep.addDetailHeader(cmp.verticalGap(5));
		rep.detailFooter(cmp.line().setPen(stl.pen1Point()));
	}

	protected void buildSql() {
		buildSqlSelect();
		buildSqlWhere();
		buildSqlOrder();
		applySqlParams();
		LOG.info(sql);
	}

	abstract void buildSqlOrder();

	abstract void buildSqlSelect();

	abstract void buildSqlWhere();

	protected void buildStyles() {
		arialStyle = stl.style()
				.setFontName("Arial")
				.setFontSize(8);
		columnTitleStyle = stl.style(arialStyle)
				.bold().setFontSize(10)
				.setHorizontalAlignment(HorizontalAlignment.CENTER)
				.setTopBorder(stl.pen1Point().setLineColor(Color.GRAY))
				.setBottomBorder(stl.pen1Point().setLineColor(Color.GRAY));
		arialBoldStyle = stl
				.style(arialStyle).bold()
				.setFontSize(14);
		piePaginaStyle = stl.style(arialStyle)
				.bold()
				.setFontSize(12)
				.setHorizontalAlignment(HorizontalAlignment.CENTER);
		subtotalStyle = stl.style(arialStyle)
				.bold()
				.setFontSize(12)
				.setTopBorder(stl.pen1Point())
				.setHorizontalAlignment(HorizontalAlignment.CENTER);
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
