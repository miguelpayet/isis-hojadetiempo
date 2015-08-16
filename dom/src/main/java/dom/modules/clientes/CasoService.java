package dom.modules.clientes;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;

import javax.inject.Inject;
import java.util.List;

@DomainService(repositoryFor = Caso.class)
@DomainServiceLayout(named = "Clientes", menuBar = DomainServiceLayout.MenuBar.SECONDARY, menuOrder = "1.2")
public class CasoService {

	@Inject
	ClienteService clienteService;
	@Inject
	DomainObjectContainer container;

	public List<Cliente> autoComplete0Create(final String name) {
		return clienteService.findClientesByName(name);
	}

	@MemberOrder(sequence = "3")
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Crear caso")
	public Caso create(final @ParameterLayout(named = "Cliente") Cliente cliente,
	                   final @ParameterLayout(named = "Código") String codigo,
	                   final @ParameterLayout(named = "Nombre") String nombre) {
		final Caso obj = container.newTransientInstance(Caso.class);
		obj.setCliente(cliente);
		obj.setCodigo(codigo);
		obj.setNombre(nombre);
		container.persistIfNotAlready(obj);
		return obj;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Buscar caso")
	@MemberOrder(sequence = "2")
	public List<Caso> findByName(@ParameterLayout(named = "Nombre") final String nombre) {
		return container.allMatches(new QueryDefault<Caso>(Caso.class, "findByName", "name", nombre));
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Listar casos")
	@MemberOrder(sequence = "1")
	public List<Caso> listAll() {
		return container.allInstances(Caso.class);
	}
}
