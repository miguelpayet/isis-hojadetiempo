package dom.modules.prueba;

import org.apache.isis.applib.AbstractFactoryAndRepository;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;

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
