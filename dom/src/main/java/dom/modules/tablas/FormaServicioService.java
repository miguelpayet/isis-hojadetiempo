package dom.modules.tablas;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;

import java.util.List;

@DomainService(repositoryFor = FormaServicio.class)
@DomainServiceLayout(named = "Listas", menuBar = DomainServiceLayout.MenuBar.SECONDARY, menuOrder = "2.2")
public class FormaServicioService {
    @javax.inject.Inject
    DomainObjectContainer container;

    @MemberOrder(sequence = "3")
    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Crear forma de servicio")
    public FormaServicio create(final @ParameterLayout(named = "Name") String name) {
        final FormaServicio obj = container.newTransientInstance(FormaServicio.class);
        obj.setNombre(name);
        container.persistIfNotAlready(obj);
        return obj;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Buscar forma de servicio")
    @MemberOrder(sequence = "2")
    public List<FormaServicio> findByName(@ParameterLayout(named = "Nombre") final String nombre) {
        return container.allMatches(new QueryDefault<FormaServicio>(FormaServicio.class, "findByName", "name", nombre));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Listar formas de servicio")
    @MemberOrder(sequence = "1")
    public List<FormaServicio> listAll() {
        return container.allInstances(FormaServicio.class);
    }
}
