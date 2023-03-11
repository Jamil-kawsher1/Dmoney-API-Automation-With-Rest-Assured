package testRunner;

import controller.User;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.annotations.Test;

public class UsertestRunner {
    User user = new User();
@Test(priority = 1,description = "Do Login Wih valid Credential")
    public void doLogin () throws ConfigurationException {
        user.callingLoginAPI();


    }
}
