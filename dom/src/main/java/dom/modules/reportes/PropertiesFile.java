package dom.modules.reportes;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

public class PropertiesFile {

    String fileName;
    java.util.Properties props;
    //@Inject
    //dom.ServletContextProvider servletContextProvider;

    public PropertiesFile(String fileName) throws IOException {
        this.fileName = "/WEB-INF/" + fileName;
        loadProperties();
    }

    public void loadProperties() throws IOException {
        //InputStream inputStream = servletContextProvider.getServletContext().getResourceAsStream(fileName);
        //if (inputStream != null) {
       //     props.load(inputStream);
       // } else {
       //     throw new java.io.FileNotFoundException("property file '" + fileName + "' not found in the classpath");
       // }
    }

    public String getProperty(String propertyName) {
        return props.getProperty(propertyName);
    }
}
