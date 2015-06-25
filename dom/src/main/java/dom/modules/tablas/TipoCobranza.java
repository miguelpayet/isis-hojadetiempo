package dom.modules.tablas;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.util.ObjectContracts;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

@javax.jdo.annotations.PersistenceCapable(identityType = IdentityType.DATASTORE)
@javax.jdo.annotations.DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@javax.jdo.annotations.Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(name = "find", language = "JDOQL", value = "SELECT "
                + "FROM hojadetiempo.dom.modules.estado.TipoCobranza "),
        @javax.jdo.annotations.Query(name = "findByName", language = "JDOQL", value = "SELECT "
                + "FROM hojadetiempo.dom.modules.estado.TipoCobranza " + "WHERE nombre.indexOf(:nombre) >= 0 ") })
@javax.jdo.annotations.Unique(name = "TipoCobranza_nombre_UNQ", members = { "nombre" })
@DomainObject(bounded = true, objectType = "TipoCobranza")
public class TipoCobranza implements Comparable<TipoCobranza> {
    @javax.inject.Inject
    @SuppressWarnings("unused")
    private DomainObjectContainer container;

    private String nombre;

    @Override
    public int compareTo(final TipoCobranza other) {
        return ObjectContracts.compare(this, other, "nombre");
    }

    @javax.jdo.annotations.Column(allowsNull = "false", length = 40)
    @Title(sequence = "1")
    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }
}
