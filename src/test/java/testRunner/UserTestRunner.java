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
    @Test(priority = 1,description = "User Try to Login With Invalid credential")
public  void doLoginWithInvalidCredential() throws ConfigurationException {
    Response response = user.callingAPI("salman@roadtocareere.net", "1234");
    JsonPath jsonPath = response.jsonPath();
    System.out.println(response.asString());
    String message = jsonPath.get("message");
    Assert.assertTrue(message.contains("User not found"));


}
    @Test(priority = 2, description = "Do Login With valid credential")
    public void doLogin () throws ConfigurationException {

        Response response = user.callingAPI("salman@roadtocareer.net", "1234");
        JsonPath jsonPath = response.jsonPath();
        System.out.println(response.asString());
        String token = jsonPath.get("token");
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("Login successfully"));
        Utils.setEnviromentVariable("token", token);
    }

    @Test(priority = 3, description = "Get Whole User List", enabled = false)
    public void getUserList () throws ConfigurationException, IOException {
        user.getUserList();


    }
    @Test(priority = 4,description = "User creation With Invalid Phone Number")
public void createNewUserWithInvalidPhoneNumber() throws ConfigurationException, InterruptedException {

    Utils utils = new Utils();
    utils.genrateRandomUser();
    String token = prop.getProperty("token");
    Response response = user.userCreate(utils.getName(), utils.getEmail(), "1234","01673534", "19" + Utils.randomNumber(), "Customer",token);
    JsonPath jsonpath = response.jsonPath();
    System.out.println(response.asString());
    String message = jsonpath.get("message");


    Assert.assertTrue(message.contains("length must be at least 11 characters long"));
}
    @Test(priority = 5, description = "Create New User", enabled = true)
    public void createNewUser () throws ConfigurationException, InterruptedException {
        Utils utils = new Utils();
        utils.genrateRandomUser();
        String token = prop.getProperty("token");
        Response response = user.userCreate(utils.getName(), utils.getEmail(), "1234", Utils.randomNumber(), "19" + Utils.randomNumber(), "Customer",token);
        JsonPath jsonpath = response.jsonPath();


        String message = jsonpath.get("message");

        System.out.println(response.asString());
        Assert.assertTrue(message.contains("User created"));
        Utils.setEnviromentVariable("createdUserPhone", jsonpath.get("user.phone_number"));
        Utils.setEnviromentVariable("createdUserPassword", jsonpath.get("user.password"));
    }
@Test(priority = 6,description = "New user Creation with Invalid token")
public void  createNewAgentWithoutProperToken() throws ConfigurationException, InterruptedException {
    Utils utils = new Utils();
    utils.genrateRandomUser();
    String token ="yfuwebfuyewbfuybwefuyewufuyvbew";

    Response res = user.agentCreate(utils.getName(), utils.getEmail(), "4X@" + Utils.randomNumber(), Utils.randomNumber(), "19" + Utils.randomNumber(), "Agent",token);

    JsonPath jsonpath = res.jsonPath();


    String message = jsonpath.get("error.message");
    System.out.println(res.asString());
    Assert.assertTrue(message.contains("Token expired"));


}

    @Test(priority = 7, description = "Create New Agent", enabled = true)
    public void createNewAgent () throws ConfigurationException, InterruptedException {
        Utils utils = new Utils();
        utils.genrateRandomUser();
        String token = prop.getProperty("token");
        Thread.sleep(3000);
        Response res = user.agentCreate(utils.getName(), utils.getEmail(), "4X@" + Utils.randomNumber(), Utils.randomNumber(), "19" + Utils.randomNumber(), "Agent",token);

        JsonPath jsonpath = res.jsonPath();


        String message = jsonpath.get("message");
        System.out.println(res.asString());
        Assert.assertTrue(message.contains("User created"));
        Utils.setEnviromentVariable("createdAgentPhone", jsonpath.get("user.phone_number"));
        Utils.setEnviromentVariable("createdAgentPassword", jsonpath.get("user.password"));


    }

@Test(priority = 8,description = "Search User With Invalid Phone Number")
    public void searchUserWithInvalidPhoneNumber() throws InterruptedException {
        String phoneNumber = "01819677097";
        Response res = user.searchByCustomerPhoneNumber(phoneNumber);
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        System.out.println(res.asString());
        Assert.assertTrue(message.contains("User not found"));
    }
    @Test(priority = 9, description = "Search User by Phone Number", enabled = true)
    public void searchByCustomerPhoneNumber () throws InterruptedException {
        String phoneNumber = prop.getProperty("createdUserPhone");
        Response res = user.searchByCustomerPhoneNumber(phoneNumber);
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        System.out.println(res.asString());
        Assert.assertTrue(message.contains("User found"));
    }
}
