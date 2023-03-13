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


    public Response callingAPI (String email, String password) throws ConfigurationException {
        UserModel loginModel = new UserModel(email, password);
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .body(loginModel)
                        .when()
                        .post("/user/login")
                        .then()
                        .assertThat().statusCode(200).extract().response();
        return res;


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

    public Response userCreate (String name, String email, String password, String phone_number, String nid, String role) throws ConfigurationException, InterruptedException {
        Thread.sleep(5000);

        UserModel registerModel = new UserModel(name, email, password, phone_number, nid, role);
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(registerModel)
                        .when()
                        .post("user/create");
//                        .then()
//                        .assertThat().statusCode(201).extract().response();


        return res;

    }


    public Response agentCreate (String name, String email, String password, String phone_number, String nid, String role) throws ConfigurationException {

        UserModel registerModel = new UserModel(name, email, password, phone_number, nid, role);
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(registerModel)
                        .when()
                        .post("user/create");

        return res;


    }

    public Response searchByCustomerPhoneNumber (String phoneNumber) throws InterruptedException {
        Thread.sleep(8000);

        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.get("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .when()
                        .get("/user/search/Phonenumber/" + phoneNumber);


          return res;



    }

}
