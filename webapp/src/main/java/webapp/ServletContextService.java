package webapp;

import dom.ServletContextProvider;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.Programmatic;
import org.apache.wicket.protocol.http.WebApplication;

import javax.servlet.ServletContext;

@DomainService
public class ServletContextService implements ServletContextProvider {

    @Programmatic
    public ServletContext getServletContext() {
        return WebApplication.get().getServletContext();
    }

}