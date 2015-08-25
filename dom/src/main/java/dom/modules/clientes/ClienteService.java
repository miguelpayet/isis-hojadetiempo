package dom.modules.clientes;

import dom.modules.hoja.HojaDeTiempo;
import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.jdosupport.IsisJdoSupport;

import javax.inject.Inject;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DomainService(repositoryFor = Cliente.class)
@DomainServiceLayout(named = "Clientes", menuBar = DomainServiceLayout.MenuBar.SECONDARY, menuOrder = "1.1")
public class ClienteService extends AbstractFactoryAndRepository {

	@Inject
	DomainObjectContainer container;

	@Inject
	private IsisJdoSupport isisJdoSupport;

	@Action(semantics = SemanticsOf.SAFE)
	@MemberOrder(sequence = "3")
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Crear cliente")
	public Cliente create(final @ParameterLayout(named = "Name") String name) {
		final Cliente obj = container.newTransientInstance(Cliente.class);
		obj.setNombre(name);
		container.persistIfNotAlready(obj);
		return obj;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Buscar cliente")
	@MemberOrder(sequence = "2")
	public List<Cliente> findByName(@ParameterLayout(named = "Nombre") final String nombre) {
		return container.allMatches(new QueryDefault<Cliente>(Cliente.class, "findByName", "name", nombre));
	}

	@Action()
	@MemberOrder(sequence = "4")
	public List<Cliente> findClientesByName(final @ParameterLayout(named = "Name") String name) {
		final String nameRegex = "(?i).*" + name + ".*";
		return allMatches(new QueryDefault<Cliente>(Cliente.class, "findByNameContaining", "nameRegex", nameRegex));
	}

	@Programmatic
	public List<String> getCasos(Cliente cliente, Date desde, Date hasta) {
		PersistenceManager pm = isisJdoSupport.getJdoPersistenceManager();
		Query q = pm.newQuery(HojaDeTiempo.class, "cliente == :cliente && fecha >= :desde && fecha <= :hasta");
		q.setResult("distinct caso");
		java.sql.Date _desde = new java.sql.Date(desde.getTime());
		java.sql.Date _hasta = new java.sql.Date(hasta.getTime());
		return (List<String>) q.execute(cliente, _desde, _hasta);
	}

	@Action(semantics = SemanticsOf.SAFE)
	@ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Listar clientes")
	@MemberOrder(sequence = "1")
	public List<Cliente> listAll() {
		return container.allInstances(Cliente.class);
	}

	@Programmatic
	public List<Caso> listarCasos(Cliente cliente) {
		PersistenceManager pm = isisJdoSupport.getJdoPersistenceManager();
		return new ArrayList<Caso>(cliente.getCasos());
	}

}
