package dom.modules.reportes;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.query.QueryDefault;

import javax.inject.Inject;
import java.util.List;

@DomainService(nature = NatureOfService.DOMAIN, repositoryFor = Idioma.class)
public class IdiomaService extends AbstractFactoryAndRepository {
	@Inject
	DomainObjectContainer container;

	@Action(semantics = SemanticsOf.SAFE)
	@Programmatic
	public Idioma getIdiomaDefault() {
		Idioma idiomaDefault = firstMatch(new QueryDefault<>(Idioma.class, "idiomaDefault"));
		return idiomaDefault;
	}

	@Action(semantics = SemanticsOf.SAFE)
	@Programmatic
	public List<Idioma> listAll() {
		return container.allInstances(Idioma.class);
	}

}
