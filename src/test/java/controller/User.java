package controller;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.UserModel;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;
import setup.Setup;
import utils.Utils;

import java.io.IOException;

import static io.restassured.RestAssured.given;


public class User extends Setup {
    public User () throws IOException {
        intitconfig();
    }


    public void callingAPI () throws ConfigurationException {
        UserModel loginModel = new UserModel("salman@roadtocareer.net", "1234");
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .body(loginModel)
                        .when()
                        .post("/user/login")
                        .then()
                        .assertThat().statusCode(200).extract().response();
        JsonPath jsonpath = res.jsonPath();

        String token = jsonpath.get("token");
        String message = jsonpath.get("message");
        Utils.setEnviromentVariable("token", token);
        System.out.println(message);


    }


    public void getUserList () throws ConfigurationException, IOException {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", Utils.readConfigFile().get("token"))
                        .when()
                        .get("/user/list")
                        .then()
                        .assertThat().statusCode(200).extract().response();
//        JsonPath jsonpath = res.jsonPath();
//
//        String token = jsonpath.get("token");
//        String message = jsonpath.get("meassge");
//        utils.Utils.setEnviromentVariable("token", token);

        //getting All JSOn Path so that we can access our needed one
        JsonPath jsonPath = res.jsonPath();
        //now accessing only user list
        String usersList = jsonPath.get("users").toString();
//        System.out.println(res.asString());//asString() method will convert json Stream as a simple String value

        System.out.println(usersList);
    }


    public void getSingleUserInfo () {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.get("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .when()
                        .get("/user/search/id=1281")
                        .then()
                        .assertThat().statusCode(200).extract().response();

        JsonPath jsonPath = res.jsonPath();
        //now accessing only user list
//        String usersList=jsonPath.get("user").toString();
//        System.out.println(res.asString());//asString() method will convert json Stream as a simple String value

        System.out.println(res.asString());
    }


    //    String name,String email, String password, String phone_number, String nid, String role

    public void userCreate () throws ConfigurationException, InterruptedException {
        Thread.sleep(5000);
        Utils utils = new Utils();
        utils.genrateRandomUser();
        UserModel registerModel = new UserModel(utils.getName(), utils.getEmail(), "1234", Utils.randomNumber(), "19" + Utils.randomNumber(), "Customer");
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.get("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(registerModel)
                        .when()
                        .post("user/create");
//                        .then()
//                        .assertThat().statusCode(201).extract().response();

        JsonPath jsonpath = res.jsonPath();

        SoftAssert softAssert = new SoftAssert();
        String message = jsonpath.get("message");
        softAssert.assertEquals(message, "controller.User created");
        Utils.setEnviromentVariable("createdUserPhone", jsonpath.get("user.phone_number"));
        Utils.setEnviromentVariable("createdUserPassword", jsonpath.get("user.password"));
        Assert.assertTrue(message.contains("User created"));


    }


    public void agentCreate () throws ConfigurationException {
        Utils utils = new Utils();
        utils.genrateRandomUser();
        UserModel registerModel = new UserModel(utils.getName(), utils.getEmail(), "4X@" + Utils.randomNumber(), Utils.randomNumber(), "19" + Utils.randomNumber(), "Agent");
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.get("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(registerModel)
                        .when()
                        .post("user/create");
//                        .then()
//                        .assertThat().statusCode(201).extract().response();

        JsonPath jsonpath = res.jsonPath();

        SoftAssert softAssert = new SoftAssert();
        String message = jsonpath.get("message");
        softAssert.assertEquals(message, "controller.User created");
        Utils.setEnviromentVariable("createdAgentPhone", jsonpath.get("user.phone_number"));
        Utils.setEnviromentVariable("createdAgentPassword", jsonpath.get("user.password"));

        Assert.assertTrue(message.contains("User created"));
//        utils.Utils.setEnviromentVariable("id",jsonpath.get("user.id").toString());

        System.out.println(res.asString());


    }

    public void searchByCustomerPhoneNumber () throws InterruptedException {
        Thread.sleep(8000);
        String phoneNumber = (String) prop.get("createdUserPhone");
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.get("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .when()
                        .get("/user/search/Phonenumber/" + phoneNumber)
                        .then().statusCode(200).extract().response();
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("User found"));
        System.out.println(res.asString());

    }

}
