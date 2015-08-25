package dom.modules.clientes;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberGroupLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.Title;

import javax.jdo.annotations.*;

@DomainObject(objectType = "Caso", bounded = false)
@MemberGroupLayout(columnSpans = {12, 0, 0, 12}, left = {"Caso"})
@PersistenceCapable(identityType = IdentityType.DATASTORE)
@Queries({
		@Query(name = "find", language = "JDOQL", value = "SELECT FROM dom.modules.clientes.Caso"),
		@Query(name = "findByClienteyNombre",
				language = "JDOQL",
				value = "SELECT FROM dom.modules.clientes.Caso where nombre.indexOf(:nombre) >= 0 && cliente == " +
						":cliente")})
@Uniques({@Unique(name = "Caso_nombre_UNQ", members = {"cliente", "nombre"}),
		@Unique(name = "Caso_codigo_UNQ", members = {"cliente", "codigo"})})
@Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
public class Caso implements Comparable<Caso> {

	Cliente cliente;
	String codigo;
	int id;
	String nombre;

	public int compareTo(Caso c) {
		return getNombre().compareTo(c.getNombre());
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Caso", sequence = "1")
	public Cliente getCliente() {
		return cliente;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Caso", sequence = "2")
	public String getCodigo() {
		return codigo;
	}

	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	public int getId() {
		return id;
	}

	@Column(allowsNull = "false")
	@MemberOrder(name = "Caso", sequence = "3")
	@Title(sequence = "1")
	public String getNombre() {
		return nombre;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
