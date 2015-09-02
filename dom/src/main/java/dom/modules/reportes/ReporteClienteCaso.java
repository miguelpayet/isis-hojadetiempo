package dom.modules.reportes;

import dom.modules.clientes.Caso;
import dom.modules.clientes.Cliente;

import java.sql.Date;

public class ReporteClienteCaso extends ReporteCliente {

	Caso caso;

	public ReporteClienteCaso(Idioma idioma, Cliente cliente, Date desde, Date hasta, Caso caso) {
		super(idioma, cliente, desde, hasta);
		this.caso = caso;
		setNombre("RCC", cliente.getNombre());
	}

	protected void applySqlParams() {
		sql = String.format(sql, cliente.getId(), desde, desde, desde, hasta, hasta, hasta, caso.getCodigo());
	}

	protected void buildSqlWhere() {
		sql = sql + " where c.id = %d and h.fecha >= '%TY-%Tm-%Td' and h.fecha <= '%TY-%Tm-%Td' and h.caso_id_oid = " +
				"'%s'";
	}
}
