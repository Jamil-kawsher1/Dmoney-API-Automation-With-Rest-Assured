package testRunner;

import controller.User;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.annotations.Test;

import java.io.IOException;

public class UserTestRunner {
    User user = new User();



    public UserTestRunner () throws IOException {
    }

    @Test(priority = 1, description = "Do Login With valid credential")
    public void doLogin () throws ConfigurationException {

        user.callingAPI();


    }

    @Test(priority = 2, description = "Get Whole User List", enabled = false)
    public void getUserList () throws ConfigurationException, IOException {
        user.getUserList();


    }

    @Test(priority = 3, description = "Create New User")
    public void createNewUser () throws ConfigurationException, InterruptedException {

        user.userCreate();
    }

    @Test(priority = 4, description = "Create New Agent")
    public void createNewAgent () throws ConfigurationException {

        user.agentCreate();
    }

    @Test(priority = 5, description = "Search User by Phone Number")
    public void searchByCustomerPhoneNumber () throws InterruptedException {
        user.searchByCustomerPhoneNumber();
    }
}
