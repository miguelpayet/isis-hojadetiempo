package dom.prueba;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.*;

@DomainService(repositoryFor = Prueba.class)
@DomainServiceLayout(named = "Prueba", menuBar = DomainServiceLayout.MenuBar.PRIMARY, menuOrder = "3")
public class PruebaService extends AbstractFactoryAndRepository {

    @ActionLayout(bookmarking = BookmarkPolicy.NEVER, named = "Crear prueba")
    @MemberOrder(sequence = "1")
    public Prueba crear() {
        final Prueba obj = getContainer().newTransientInstance(Prueba.class);
        getContainer().persistIfNotAlready(obj);
        return obj;
    }

}
