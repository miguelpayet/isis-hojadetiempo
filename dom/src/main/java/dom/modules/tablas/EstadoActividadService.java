package dom.modules.tablas;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;

import java.util.List;

@DomainService(repositoryFor = EstadoActividad.class)
@DomainServiceLayout(named = "Listas", menuBar = DomainServiceLayout.MenuBar.SECONDARY, menuOrder = "2.1")
public class EstadoActividadService extends AbstractFactoryAndRepository {

    @javax.inject.Inject
    DomainObjectContainer container;

    @MemberOrder(sequence = "3")
    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Crear estado de actividad")
    public EstadoActividad create(final @ParameterLayout(named = "Name") String name) {
        final EstadoActividad obj = container.newTransientInstance(EstadoActividad.class);
        obj.setNombre(name);
        container.persistIfNotAlready(obj);
        return obj;
    }

    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Buscar estados de actividad")
    @MemberOrder(sequence = "2")
    public List<EstadoActividad> findByName(@ParameterLayout(named = "Nombre") final String nombre) {
        return container.allMatches(new QueryDefault<EstadoActividad>(EstadoActividad.class, "findByName", "name", nombre));
    }

    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Listar estados de actividad")
    @MemberOrder(sequence = "1")
    public List<EstadoActividad> listAll() {
        return container.allInstances(EstadoActividad.class);
    }

}
