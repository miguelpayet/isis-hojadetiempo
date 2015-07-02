package dom.modules.reportes;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.isis.applib.value.Blob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class ReporteBase {

	public ReporteBase() {
		buildReporte();
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

	protected final static Logger LOG = LoggerFactory.getLogger(ReporteCliente.class);
	Connection conn;
	String finalFilename;
	JasperReportBuilder rep;
	String nombreReporte;

	public String getFechaString(String formato) {
		SimpleDateFormat sdfDate = new SimpleDateFormat(formato);
		java.util.Date now = new java.util.Date();
		return sdfDate.format(now);
	}

	public Blob buildBlob() throws IOException, DRException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		rep.toPdf(baos);
		baos.flush();
		baos.close();
		return new Blob(nombreReporte, "application/octet-stream", baos.toByteArray());
	}

}
