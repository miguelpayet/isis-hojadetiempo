package dom;

import javax.servlet.ServletContext;

public interface ServletContextProvider {

    @org.apache.isis.applib.annotation.Programmatic
    public ServletContext getServletContext();

}