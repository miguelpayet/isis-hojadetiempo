package dom.modules.clientes;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;

import javax.jdo.annotations.*;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@Queries({
		@Query(name = "find", language = "JDOQL", value = "SELECT FROM dom.modules.clientes.Caso"),
		@Query(name = "listarPorCliente",
				language = "JDOQL",
				value = "SELECT FROM dom.modules.clientes.Caso order by nombre")})
@Unique(name = "Caso_nombre_UNQ", members = {"cliente", "nombre"})
@DomainObject(objectType = "Caso", bounded = false)
public class Caso {

	Cliente cliente;
	String codigo;
	String nombre;

	@Column(allowsNull = "false")
	@MemberOrder(name = "Cliente", sequence = "1")
	public Cliente getCliente() {
		return cliente;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Código", sequence = "2")
	public String getCodigo() {
		return codigo;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Nombre", sequence = "3")
	public String getNombre() {
		return nombre;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
