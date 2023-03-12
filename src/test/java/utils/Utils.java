package utils;

import com.github.javafaker.Faker;
import model.UserModel;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utils {
    //This method will save all the information like token ,user info in config.properties file
    public static void setEnviromentVariable (String key, String value) throws ConfigurationException {

        PropertiesConfiguration config = new PropertiesConfiguration("./src/test/resources/config.properties");
        config.setProperty(key, value);
        config.save();
    }

    public static Properties readConfigFile () throws IOException {
        Properties prop = new Properties();
        FileInputStream file = new FileInputStream("./src/test/resources/config.properties");
        prop.load(file);
        return prop;


    }

    public String name;
    public String email;

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public void genrateRandomUser () {
        Faker faker = new Faker();
        setName(faker.name().fullName());
        setEmail(faker.internet().emailAddress());


    }

    public static String randomNumber () {
        String prefix = "01700";
        int min = 100000;
        int max = 999999;
        int phoneNumber = (int) Math.round(Math.random() * (max - min) + min);
        return prefix+phoneNumber;
    }

    public static void main (String[] args) throws IOException {
//     String token= (String) ;
        Utils utils = new Utils();
        UserModel testUser = new UserModel("jjjj", "badhon", "1234", "01771", "1997", "Customer");
        System.out.println(testUser);
    }
}
