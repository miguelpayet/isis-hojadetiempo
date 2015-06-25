package dom.modules.tablas;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;

import java.util.List;

@DomainService(repositoryFor = Tarifa.class)
@DomainServiceLayout(named = "Listas", menuBar = DomainServiceLayout.MenuBar.SECONDARY, menuOrder = "2.3")
public class TarifaService {
    @javax.inject.Inject
    DomainObjectContainer container;

    @MemberOrder(sequence = "3")
    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Crear tarifa")
    public Tarifa create(final @ParameterLayout(named = "Name") String name) {
        final Tarifa obj = container.newTransientInstance(Tarifa.class);
        obj.setNombre(name);
        container.persistIfNotAlready(obj);
        return obj;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Buscar tarifa")
    @MemberOrder(sequence = "2")
    public List<Tarifa> findByName(@ParameterLayout(named = "Nombre") final String nombre) {
        return container.allMatches(new QueryDefault<Tarifa>(Tarifa.class, "findByName", "name", nombre));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Listar tarifas")
    @MemberOrder(sequence = "1")
    public List<Tarifa> listAll() {
        return container.allInstances(Tarifa.class);
    }
}
