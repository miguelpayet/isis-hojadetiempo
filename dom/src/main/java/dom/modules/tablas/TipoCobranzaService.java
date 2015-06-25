package dom.modules.tablas;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;

import java.util.List;

@DomainService(repositoryFor = TipoCobranza.class)
@DomainServiceLayout(named = "Listas", menuBar = DomainServiceLayout.MenuBar.SECONDARY, menuOrder = "2.4")
public class TipoCobranzaService {

    @javax.inject.Inject
    DomainObjectContainer container;

    @MemberOrder(sequence = "3")
    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Crear tipo de cobranza")
    public TipoCobranza create(final @ParameterLayout(named = "Name") String name) {
        final TipoCobranza obj = container.newTransientInstance(TipoCobranza.class);
        obj.setNombre(name);
        container.persistIfNotAlready(obj);
        return obj;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Buscar tipo de cobranza")
    @MemberOrder(sequence = "2")
    public List<TipoCobranza> findByName(@ParameterLayout(named = "Nombre") final String nombre) {
        return container.allMatches(new QueryDefault<TipoCobranza>(TipoCobranza.class, "findByName", "name", nombre));
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Listar tipos de cobranza")
    @MemberOrder(sequence = "1")
    public List<TipoCobranza> listAll() {
        return container.allInstances(TipoCobranza.class);
    }
}
