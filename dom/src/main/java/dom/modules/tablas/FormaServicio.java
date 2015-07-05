package dom.modules.tablas;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.PropertyLayout;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.util.ObjectContracts;

import javax.jdo.annotations.*;

@PersistenceCapable(identityType = IdentityType.DATASTORE)
@DatastoreIdentity(strategy = IdGeneratorStrategy.IDENTITY, column = "id")
@Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@Queries({
		@Query(name = "find", language = "JDOQL", value = "SELECT FROM dom.modules.tablas.FormaServicio"),
		@Query(name = "listarPorNombre",
				language = "JDOQL",
				value = "SELECT FROM dom.modules.tablas.FormaServicio order by nombre"),
		@Query(name = "findByName",
				language = "JDOQL",
				value = "SELECT FROM hojadetiempo.dom.modules.tablas.FormaServicio WHERE nombre.indexOf(:nombre) >= 0 ")})
@Unique(name = "FormaServicio_nombre_UNQ", members = {"nombre"})
@DomainObject(objectType = "FormaServicio", bounded = true)
public class FormaServicio extends AbstractFactoryAndRepository implements Comparable<FormaServicio> {

	public static final String OTROS = "Otros";
	private String nombre;
	private String nombreIngles;

	public int compareTo(final FormaServicio other) {
		return ObjectContracts.compare(this, other, "nombre");
	}

	@Column(allowsNull = "false", length = 40)
	@Title(sequence = "1")
	@MemberOrder(name = "Tipo de Servicio", sequence = "1")
	@PropertyLayout(named = "Nombre Español")
	public String getNombre() {
		return nombre;
	}

	@Column(allowsNull = "false", length = 40)
	@MemberOrder(name = "Tipo de Servicio", sequence = "2")
	@PropertyLayout(named = "Nombre Inglés")
	public String getNombreIngles() {
		return nombreIngles;
	}

	public void setNombre(final String nombre) {
		this.nombre = nombre;
	}

	public void setNombreIngles(String nombreIngles) {
		this.nombreIngles = nombreIngles;
	}
}
