package dom.modules.reportes;

import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Title;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.VersionStrategy;
import java.util.HashMap;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE, table = "Idioma")
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Queries({
		@Query(name = "find", language = "JDOQL", value = "SELECT FROM dom.modules.reportes.Idioma "),
		@Query(name = "idiomaDefault", language = "JDOQL", value = "SELECT FROM dom.modules.reportes.Idioma WHERE " +
				"porDefault == true")})
@DomainObject(objectType = "Idioma", bounded = true)
public class Idioma {

	private String codigo;
	private String nombre;
	private boolean porDefault;
	private HashMap<String, String> strings;

	public Idioma() {
	}

	@Column(allowsNull = "false")
	public String getCodigo() {
		return codigo;
	}

	@Column(allowsNull = "false")
	@Title
	public String getNombre() {
		return nombre;
	}

	@Column(allowsNull = "false")
	public boolean getPorDefault() {
		return porDefault;
	}

	public String getString(String name) {
		String valor = "nulo";
		if (strings == null) {
			loadStrings(codigo);
		}
		if (strings.containsKey(name)) {
			valor = strings.get(name).toString();
		}
		return valor;
	}

	private void loadStrings(String codigo) {
		switch (codigo) {
			case "en":
				strings.put("paginador", "Page {0} of {1}");
				strings.put("campo-tipo-servicio", "tipo_servicio_en");
				strings.put("titulo-cliente", "Client");
				strings.put("abogado", "Attorney");
				strings.put("fecha", "Date");
				strings.put("consulta", "Query");
				strings.put("solicitante", "Applicant");
				strings.put("referencia", "Reference");
				strings.put("detalles", "Details");
				break;
			case "es":
				strings.put("paginador", "Página {0} de {1}");
				strings.put("detalles", "Detalles");
				strings.put("campo-tipo-servicio", "tipo_servicio");
				strings.put("titulo-cliente", "Cliente");
				strings.put("abogado", "Abogado");
				strings.put("fecha", "Fecha");
				strings.put("consulta", "Consulta");
				strings.put("solicitante", "Solicitante");
				strings.put("referencia", "Referencia");
				break;
		}
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPorDefault(boolean porDefault) {
		this.porDefault = porDefault;
	}
}
