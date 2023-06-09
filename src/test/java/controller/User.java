package controller;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.UserModel;
import org.apache.commons.configuration.ConfigurationException;
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
                        .post("/user/login");


        return res;


    }










    public Response userCreate (String name, String email, String password, String phone_number, String nid, String role,String token) throws ConfigurationException, InterruptedException {
        Thread.sleep(5000);

        Thread.sleep(2000);
        UserModel registerModel = new UserModel(name, email, password, phone_number, nid, role);
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", token)
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(registerModel)
                        .when()
                        .post("user/create");
//                        .then()
//                        .assertThat().statusCode(201).extract().response();


        return res;

    }


    public Response agentCreate (String name, String email, String password, String phone_number, String nid, String role,String token) throws ConfigurationException, InterruptedException {

        UserModel registerModel = new UserModel(name, email, password, phone_number, nid, role);

        Thread.sleep(2000);
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", token)
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(registerModel)
                        .when()
                        .post("user/create");

        return res;


    }

    public Response searchByCustomerPhoneNumber (String phoneNumber) throws InterruptedException, IOException {
        Thread.sleep(8000);
        intitconfig();
        Thread.sleep(3000);
        String token = prop.getProperty("token");
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", token)
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .when()
                        .get("/user/search/Phonenumber/" + phoneNumber);


        return res;


    }

}
