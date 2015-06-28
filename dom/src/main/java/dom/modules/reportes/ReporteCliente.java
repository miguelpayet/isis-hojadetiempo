package dom.modules.reportes;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.jasper.builder.export.JasperPdfExporterBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.VariableBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.builder.group.ColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.*;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import org.apache.isis.applib.value.Blob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

public class ReporteCliente {
    private final static Logger LOG = LoggerFactory.getLogger(ReporteCliente.class);
    private final static String HORAS = "Hora(s),";
    private final static String MINUTOS = "Minuto(s)";

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

    public String getFechaString(String formato) {
        SimpleDateFormat sdfDate = new SimpleDateFormat(formato);
        java.util.Date now = new java.util.Date();
        return sdfDate.format(now);
    }

    public void build() throws IOException, SQLException {
        buildDataSource();
        rep = report();
        rep.setLocale(Locale.forLanguageTag("EN"));
        rep.setPageFormat(PageType.A4, PageOrientation.PORTRAIT);
        rep.pageHeader(cmp.horizontalList(
                cmp.text(getFechaString("dd-MM-yyyy")).setHorizontalAlignment(HorizontalAlignment.LEFT),
                cmp.image(getClass().getClassLoader().getResourceAsStream("images/logo.png")).setDimension(75, 75).setHorizontalAlignment(HorizontalAlignment.RIGHT)));
        rep.highlightDetailEvenRows();
        rep.setDataSource(sql, conn);
        rep.setDetailFooterSplitType(SplitType.PREVENT);
        rep.pageHeader(cmp.verticalGap(10));
        rep.columnHeader(cmp.verticalGap(10));
        rep.addDetailFooter(cmp.verticalGap(5));
        rep.addDetailHeader(cmp.verticalGap(5));

        StyleBuilder arialStyle = stl.style().setFontName("Arial").setFontSize(8);
        StyleBuilder columnTitleStyle = stl.style(arialStyle).bold().setFontSize(10).setHorizontalAlignment(HorizontalAlignment.CENTER).setBorder(stl.pen1Point());
        StyleBuilder arialBoldStyle = stl.style(arialStyle).bold().setFontSize(14);
        StyleBuilder piePaginaStyle = stl.style(arialStyle).bold().setFontSize(12).setHorizontalAlignment(HorizontalAlignment.CENTER);
        StyleBuilder subtotalStyle = stl.style(arialStyle).bold().setFontSize(12).setTopBorder(stl.pen1Point()).setHorizontalAlignment(HorizontalAlignment.CENTER);
        rep.setColumnTitleStyle(columnTitleStyle);
        rep.pageFooter(cmp.pageXofY().setStyle(piePaginaStyle));

        TextColumnBuilder<String> columnaCliente = col.column("", "nombre", type.stringType()).setStyle(arialBoldStyle);
        ColumnGroupBuilder itemGroup = grp.group(columnaCliente).setHeaderLayout(GroupHeaderLayout.VALUE);
        TextColumnBuilder<Long> columnaTiempo = col.column("Total", "tiemporeal", type.longType()).setValueFormatter(new TiempoRealFormatter());
        VariableBuilder<Long> sumaTiempo = variable(columnaTiempo, Calculation.SUM);
        rep.variables(sumaTiempo);
        TextFieldBuilder<String> groupSbt = cmp.text(new CustomTextSubtotal(sumaTiempo)).setStyle(subtotalStyle);
        rep.groupBy(itemGroup);
        itemGroup.footer(groupSbt);

        rep.columns(
                col.column("Abogado", "username", type.stringType()).setStyle(arialStyle).setHorizontalAlignment(HorizontalAlignment.CENTER).setFixedColumns(6),
                col.column("Fecha", "fecha", type.dateType()).setStyle(arialStyle).setHorizontalAlignment(HorizontalAlignment.CENTER).setFixedColumns(8),
                col.column("Consulta", "tipo_servicio", type.stringType()).setStyle(arialStyle.setRightPadding(10)).setHorizontalAlignment(HorizontalAlignment.LEFT),
                col.column("Persona Solicitante", "solicitadopor", type.stringType()).setStyle(arialStyle),
                col.column("Referencia", "caso", type.stringType()).setStyle(arialStyle).setWidth(100),
                col.column("Detalle de Consulta", "servicio", type.stringType()).setStyle(arialStyle).setWidth(170),
                columnaTiempo.setStyle(arialStyle).setWidth(130));
    }

    private void buildDataSource() throws IOException, SQLException {
        PropertiesFile pr = new PropertiesFile("WEB-INF/persistor.properties");
        String url = pr.getProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionURL");
        String user = pr.getProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionUserName");
        String password = pr.getProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionUserName");
        conn = DriverManager.getConnection(url, user, password);
    }

    private JasperPdfExporterBuilder buildPdfExporter() throws IOException {
        File f = File.createTempFile("reporte", ".pdf");
        f.delete();
        finalFilename = f.getPath();
        LOG.info(finalFilename);
        return export.pdfExporter(finalFilename).setEncrypted(false);
    }

    public Blob buildBlob() throws IOException, DRException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        rep.toPdf(baos);
        baos.flush();
        baos.close();
        return new Blob(nombreReporte, "application/octet-stream", baos.toByteArray());
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
            return reportParameters.getValue("nombre") + " - " + String.format("%01d " + HORAS + " %02d " + MINUTOS, horas, minutos);
        }
    }


}