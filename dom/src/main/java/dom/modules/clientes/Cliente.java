package dom.modules.clientes;

import org.apache.isis.applib.AbstractDomainObject;
import org.apache.isis.applib.annotation.*;

import javax.jdo.annotations.*;
import java.util.List;
import java.util.SortedSet;

@DomainObject(objectType = "Cliente")
@MemberGroupLayout(columnSpans = {4, 4, 0, 12}, left = {"Cliente"}, middle = {"Dirección"})
@PersistenceCapable(identityType = IdentityType.DATASTORE, table = "Cliente")
@Queries({
		@Query(name = "find", language = "JDOQL", value = "SELECT FROM dom.modules.clientes.Cliente"),
		@Query(name = "findByName",
				language = "JDOQL",
				value = "SELECT FROM dom.modules.clientes.Cliente WHERE nombre.indexOf(:nombre) >= 0"),
		@Query(name = "findByNameContaining",
				language = "JDOQL",
				value = "SELECT FROM dom.modules.clientes.Cliente WHERE nombre.matches(:nameRegex)")})
@Uniques({@Unique(name = "Cliente_nombre_UNQ", members = {"nombre"}),
		@Unique(name = "Cliente_documento_UNQ", members = {"tipoDocumento", "numDocumento"})})
@Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")

public class Cliente extends AbstractDomainObject {

	private SortedSet<Caso> casos;
	private String direccion;
	private int id;
	private String nombre;
	private String numDocumento;
	private String telefono;
	private String tipoDocumento;
	private String tipoPersona;

	public List<String> choicesTipoDocumento() {
		return TipoDocumento.getTipos();
	}

	public List<String> choicesTipoPersona() {
		return TipoPersona.getTipos();
	}

	@Persistent(mappedBy = "cliente")
	@MemberOrder(name = "Casos", sequence = "5")
	public SortedSet<Caso> getCasos() {
		return casos;
	}

	@Column(allowsNull = "true", length = 100)
	@MemberOrder(name = "Dirección", sequence = "2")
	@PropertyLayout(multiLine = 5, named = "Dirección", typicalLength = 100, labelPosition = LabelPosition.TOP)
	public String getDireccion() {
		return direccion;
	}

	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@PrimaryKey
	public int getId() {
		return id;
	}

	@Column(allowsNull = "true", length = 40)
	@MemberOrder(name = "Cliente", sequence = "1")
	@Title
	public String getNombre() {
		return nombre;
	}

	@Column(allowsNull = "true")
	@MemberOrder(name = "Cliente", sequence = "4")
	@Property(hidden = Where.ALL_TABLES)
	public String getNumDocumento() {
		return numDocumento;
	}

	@Column(allowsNull = "true")
	@MemberOrder(name = "Dirección", sequence = "1")
	@PropertyLayout(multiLine = 3, named = "Teléfonos", typicalLength = 50, labelPosition = LabelPosition.TOP)
	public String getTelefono() {
		return telefono;
	}

	@Column(allowsNull = "true")
	@MemberOrder(name = "Cliente", sequence = "3")
	@Property(hidden = Where.ALL_TABLES)
	public String getTipoDocumento() {
		return tipoDocumento;
	}

	@Column(allowsNull = "true")
	@MemberOrder(name = "Cliente", sequence = "2")
	@Property(hidden = Where.ALL_TABLES)
	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setCasos(SortedSet<Caso> casos) {
		this.casos = casos;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setNumDocumento(String numDocumento) {
		this.numDocumento = numDocumento;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

}
