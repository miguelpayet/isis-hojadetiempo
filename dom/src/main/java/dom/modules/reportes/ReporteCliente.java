package dom.modules.reportes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import net.sf.dynamicreports.report.exception.DRException;

public class ReporteCliente {
    private final static Logger LOG = LoggerFactory.getLogger(ReporteCliente.class);

    String sql;
    Connection conn;

    public ReporteCliente(int idCliente, Date desde, Date hasta) {
        initSql(idCliente, desde, hasta);
    }

    private void initSql(int idCliente, Date desde, Date hasta) {
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

    private void buildDataSource() throws IOException, SQLException {
        PropertiesFile pr = new PropertiesFile("persistor.properties");
        String url = pr.getProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionURL");
        String user = pr.getProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionUserName");
        String password = pr.getProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionUserName");
        conn = DriverManager.getConnection(url, user, password);
    }

    public void build() throws IOException, SQLException {
        LOG.info(new java.io.File(".").getAbsolutePath());
        buildDataSource();
        try {
            report()
                    .columns(
                            col.column("Cliente", "nombre", type.stringType()),
                            col.column("Abogado", "username", type.stringType()),
                            col.column("Fecha", "fecha", type.dateType()))
                    .setDataSource(sql, conn)
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }
}