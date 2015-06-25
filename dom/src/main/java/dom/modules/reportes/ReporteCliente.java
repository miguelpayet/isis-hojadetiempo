package dom.modules.reportes;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.GroupHeaderLayout;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.isis.applib.value.Blob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;


public class ReporteCliente {
    private final static Logger LOG = LoggerFactory.getLogger(ReporteCliente.class);

    String sql;
    Connection conn;
    String finalFilename;
    JasperReportBuilder rep;
    String nombreReporte;

    public ReporteCliente(int idCliente, Date desde, Date hasta) {
        nombreReporte = "ReporteCliente-" + Integer.toString(idCliente) + "-" + getFechaString("yyyyMMdd-HHmmss") + ".pdf";
        buildSql(idCliente, desde, hasta);
    }

    private void buildSql(int idCliente, Date desde, Date hasta) {
        String sqlTmp = "select h.fecha, h.cliente_id_oid, c.nombre, h.abogado_id_oid, a.username, h.fecha, h.formaservicio_id_oid,  " +
                "f.nombre tipo_servicio, h.solicitadopor,h.caso,h.servicio,h.horasfacturables,h.minutosfacturables, " +
                "concat(h.horasreales, ' Hora(s), ', h.minutosreales, ' Minuto(s)') tiemporeal " +
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
        try {
            JasperPdfExporterBuilder pdfExporter = buildPdfExporter();

            TextColumnBuilder<String> itemColumn = col.column("Cliente", "nombre", type.stringType());
            ColumnGroupBuilder itemGroup = grp.group(itemColumn)
                    .setTitleWidth(40)
                    .setHeaderLayout(GroupHeaderLayout.TITLE_AND_VALUE);

            rep = report();
            rep.title(cmp.text("DynamicReports " + getFechaString("yyyy-MM-dd HH:mm:ss")));
            rep.highlightDetailEvenRows();
            rep.groupBy(itemGroup);
            StyleBuilder boldStyle = stl.style().bold();
            StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalAlignment(HorizontalAlignment.CENTER);
            StyleBuilder columnTitleStyle = stl.style(boldCenteredStyle).setBorder(stl.pen1Point());
            rep.setColumnTitleStyle(columnTitleStyle);
            rep.setDetailFooterSplitType(SplitType.PREVENT);
            rep.columns(
                    col.column("Abogado", "username", type.stringType()),
                    col.column("Fecha", "fecha", type.dateType()),
                    col.column("Consulta", "tipo_servicio", type.stringType()),
                    col.column("Persona Solicitante", "solicitadopor", type.stringType()),
                    col.column("Referencia", "caso", type.stringType()),
                    col.column("Detalle de Consulta", "servicio", type.stringType()),
                    col.column("Total", "tiemporeal", type.stringType()));
            rep.pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle));
            rep.setDataSource(sql, conn);
            rep.toPdf(pdfExporter);
        } catch (DRException e) {
            e.printStackTrace();
        }
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
        return new Blob(nombreReporte, "application/octet-stream", baos.toByteArray());
        //return baos.toByteArray();
    }

}