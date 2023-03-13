package testRunner;

import controller.User;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import setup.Setup;
import utils.Utils;

import java.io.IOException;

public class UserTestRunner extends Setup {
    User user = new User();


    public UserTestRunner () throws IOException {
        intitconfig();
    }

    @Test(priority = 1, description = "Do Login With valid credential")
    public void doLogin () throws ConfigurationException {

        Response response = user.callingAPI("salman@roadtocareer.net", "1234");
        JsonPath jsonPath = response.jsonPath();
        System.out.println(response.asString());
        String token = jsonPath.get("token");
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("Login successfully"));
        Utils.setEnviromentVariable("token", token);
    }

    @Test(priority = 2, description = "Get Whole User List", enabled = false)
    public void getUserList () throws ConfigurationException, IOException {
        user.getUserList();


    }

    @Test(priority = 3, description = "Create New User", enabled = true)
    public void createNewUser () throws ConfigurationException, InterruptedException {
        Utils utils = new Utils();
        utils.genrateRandomUser();
        Response response = user.userCreate(utils.getName(), utils.getEmail(), "1234", Utils.randomNumber(), "19" + Utils.randomNumber(), "Customer");
        JsonPath jsonpath = response.jsonPath();

        SoftAssert softAssert = new SoftAssert();
        String message = jsonpath.get("message");

        System.out.println(response.asString());
        Assert.assertTrue(message.contains("User created"));
        Utils.setEnviromentVariable("createdUserPhone", jsonpath.get("user.phone_number"));
        Utils.setEnviromentVariable("createdUserPassword", jsonpath.get("user.password"));
    }

    @Test(priority = 4, description = "Create New Agent", enabled = true)
    public void createNewAgent () throws ConfigurationException {
        Utils utils = new Utils();
        utils.genrateRandomUser();
        Response res = user.agentCreate(utils.getName(), utils.getEmail(), "4X@" + Utils.randomNumber(), Utils.randomNumber(), "19" + Utils.randomNumber(), "Agent");

        JsonPath jsonpath = res.jsonPath();


        String message = jsonpath.get("message");
        System.out.println(res.asString());
        Assert.assertTrue(message.contains("User created"));
        Utils.setEnviromentVariable("createdAgentPhone", jsonpath.get("user.phone_number"));
        Utils.setEnviromentVariable("createdAgentPassword", jsonpath.get("user.password"));


    }

    @Test(priority = 5, description = "Search User by Phone Number", enabled = true)
    public void searchByCustomerPhoneNumber () throws InterruptedException {
        String phoneNumber = prop.getProperty("createdUserPhone");
        Response res = user.searchByCustomerPhoneNumber(phoneNumber);
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        System.out.println(res.asString());
        Assert.assertTrue(message.contains("User found"));
    }
}
