package dom.modules.reportes;

import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import com.google.common.io.ByteStreams;
import org.apache.isis.applib.value.Blob;

import java.text.SimpleDateFormat;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;

import net.sf.dynamicreports.report.exception.DRException;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;


public class ReporteCliente {
    private final static Logger LOG = LoggerFactory.getLogger(ReporteCliente.class);

    String sql;
    Connection conn;
    String finalFilename;
    JasperReportBuilder rep;

    public ReporteCliente(int idCliente, Date desde, Date hasta) {
        buildSql(idCliente, desde, hasta);
    }

    private void buildSql(int idCliente, Date desde, Date hasta) {
        String sqlTmp = "select h.cliente_id_oid, c.nombre, h.abogado_id_oid, a.username, h.fecha, h.formaservicio_id_oid, f.nombre, " +
                "h.solicitadopor,h.caso,h.servicio,h.horasfacturables,h.minutosfacturables " +
                "from hojadetiempo h " +
                "join cliente c on c.id = h.cliente_id_oid " +
                "join applicationuser a on a.id=h.abogado_id_oid " +
                "join formaservicio f on f.id = h.formaservicio_id_oid " +
                "where c.id = %d and h.fecha >= '%TY-%Tm-%Td' and h.fecha <= '%TY-%Tm-%Td';";
        sql = String.format(sqlTmp, idCliente, desde, desde, desde, hasta, hasta, hasta);
        LOG.info(sql);
    }

    public String getFechaString(String formato) {
        SimpleDateFormat sdfDate = new SimpleDateFormat(formato);
        java.util.Date now = new java.util.Date();
        return sdfDate.format(now);
    }

    public void build() throws IOException, SQLException {
        buildDataSource();
        JasperPdfExporterBuilder pdfExporter = buildPdfExporter();
        //try {
        rep = report();
        rep.title(cmp.text("DynamicReports " + getFechaString("yyyy-MM-dd HH:mm:ss")));
        rep.columns(
                col.column("Cliente", "nombre", type.stringType()),
                col.column("Abogado", "username", type.stringType()),
                col.column("Consulta", "caso", type.stringType()),
                col.column("Solicitado por", "solicitadopor", type.stringType()),
                col.column("Forma servicio", "nombre", type.stringType()),
                col.column("Fecha", "fecha", type.dateType()));
        rep.setDataSource(sql, conn);
        //rep.toPdf(pdfExporter);
        // } catch (DRException e) {
        //     e.printStackTrace();
        //}
    }

    private void buildDataSource() throws IOException, SQLException {
        PropertiesFile pr = new PropertiesFile("persistor.properties");
        String url = pr.getProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionURL");
        String user = pr.getProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionUserName");
        String password = pr.getProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionUserName");
        conn = DriverManager.getConnection(url, user, password);
    }

    private JasperPdfExporterBuilder buildPdfExporter() throws IOException {
        JasperPdfExporterBuilder pdfExporter;
        File f = File.createTempFile("reporte", ".pdf");
        f.delete();
        finalFilename = f.getPath();
        LOG.info(finalFilename);
        return export.pdfExporter(finalFilename).setEncrypted(false);
    }

    public Blob buildBlob() throws FileNotFoundException, IOException, DRException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //FileInputStream is;
        //byte[] reporteBytes;
        //File f = new File(finalFilename);
        //LOG.info("finalFilename: " + f.length());
        //is = new FileInputStream(finalFilename);
        //reporteBytes = ByteStreams.toByteArray(is);
        //LOG.info("reporteBytes: " + Integer.toString(reporteBytes.length));
        //is.close();
        rep.toPdf(baos);
        baos.flush();
        baos.close();
        LOG.info("baos: " + Integer.toString(baos.size()));
        String nombreReporte = "ReporteCliente" + getFechaString("yyyy-MM-dd-HH-mm-ss") + ".pdf";
        return new Blob(nombreReporte, "application/pdf", baos.toByteArray());
        //return baos.toByteArray();
    }

}