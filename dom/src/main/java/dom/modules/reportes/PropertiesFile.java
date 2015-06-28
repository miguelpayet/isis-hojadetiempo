package dom.modules.reportes;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileNotFoundException;

public class PropertiesFile {

    String fileName;
    java.util.Properties props;
    String baseFilename;

    public PropertiesFile(String fileName) throws IOException {
        this.baseFilename = fileName;
        this.fileName = fileName;
        loadProperties();
    }

    public void loadProperties() throws IOException {
        //FileInputStream inputStream = new FileInputStream(fileName);
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(baseFilename);
        if (inputStream != null) {
            props = new java.util.Properties();
            props.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + fileName + "' not found in the classpath");
        }
    }

    public String getProperty(String propertyName) {
        return props.getProperty(propertyName);
    }
}
