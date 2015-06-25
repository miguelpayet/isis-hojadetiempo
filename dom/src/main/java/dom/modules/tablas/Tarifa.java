package dom.modules.tablas;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.Title;
import org.apache.isis.applib.util.ObjectContracts;

import javax.jdo.annotations.*;

@PersistenceCapable(identityType = IdentityType.DATASTORE, table = "Tarifa")
@DatastoreIdentity(strategy = javax.jdo.annotations.IdGeneratorStrategy.IDENTITY, column = "id")
@Version(strategy = VersionStrategy.VERSION_NUMBER, column = "version")
@Queries({
        @javax.jdo.annotations.Query(name = "find", language = "JDOQL", value = "SELECT FROM dom.modules.estado.Tarifa"),
        @Query(	name = "listarPorNombre",
                language = "JDOQL",
                value = "SELECT FROM dom.modules.tablas.Tarifa order by nombre"),
        @javax.jdo.annotations.Query(	name = "findByName",
                language = "JDOQL",
                value = "SELECT FROM dom.modules.tablas.Tarifa WHERE nombre.indexOf(:nombre) >= 0") })
@Unique(name = "Tarifa_nombre_UNQ", members = { "nombre" })
@DomainObject(bounded = true, objectType = "Tarifa")
public class Tarifa implements Comparable<Tarifa> {
    @javax.inject.Inject
    @SuppressWarnings("unused")
    private DomainObjectContainer container;

    private String nombre;

    @Override
    public int compareTo(final Tarifa other) {
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
