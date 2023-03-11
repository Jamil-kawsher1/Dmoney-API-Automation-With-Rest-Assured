package setup;

import org.testng.annotations.BeforeTest;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Setup {
    public Properties prop;
    @BeforeTest
    //This Function will load the config file so that we can read config file property when we need them
    public void intitconfig() throws IOException {
        prop=new Properties();
        FileInputStream file=new FileInputStream("./src/test/resources/config.properties");
        prop.load(file);
    }
}
