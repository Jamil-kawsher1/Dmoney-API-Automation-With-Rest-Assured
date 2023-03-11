package utils;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public class Utils {
    //This method will save all the information like token ,user info in config.properties file
    public static void setEnviromentVariable (String key, String value) throws ConfigurationException {

        PropertiesConfiguration config = new PropertiesConfiguration("./src/test/resources/config.properties");
        config.setProperty(key, value);
        config.save();
    }
}
