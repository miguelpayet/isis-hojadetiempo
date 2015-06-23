package dom.modules.reportes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Date;

public class ReporteCliente {
    private final static Logger LOG = LoggerFactory.getLogger(ReporteCliente.class);

    String sql;

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

    private void buildDataSource() throws IOException {
        PropertiesFile pr = new PropertiesFile("persistor.properties");
        String url = pr.getProperty("isis.persistor.datanucleus.impl.javax.jdo.option.ConnectionURL");
        LOG.info(url);
    }

    public void build() throws IOException {
        buildDataSource();
        /*
        report()
                .columns(col.column("Item", "item", type.stringType()),
                        col.column("Quantity", "quantity", type.integerType()),
                        col.column("Unit price", "unitprice", type.bigDecimalType()))
                .setDataSource();
*/
    }
}