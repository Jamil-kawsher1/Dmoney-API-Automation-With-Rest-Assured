package controller;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.UserModel;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import utils.Utils;

import static io.restassured.RestAssured.given;


public class User {

    public void callingLoginAPI () throws ConfigurationException {
        UserModel loginModel = new UserModel("salman@roadtocareer.net", "1234");
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";

        Response res =
                given()
                        .contentType("application/json")
                        .body(loginModel)
                        .when()
                        .post("/user/login")
                        .then().statusCode(200).extract().response();
        //getting json path ready so that we can access it with json path name
        JsonPath resposePathList = res.jsonPath();
        String message = resposePathList.get("message");
        String token = resposePathList.get("token");
        Assert.assertEquals(message,"Login successfully");


//        System.out.println(res.asString()); //this method will display whole json response it will help us to view and pick a path

        //saving token to config.properties file so that we can access token to automate other API action without repetitive login
        Utils.setEnviromentVariable("token",token);



    }


    //create Customer
    public void createCustomer(){

        RestAssured.baseURI="http://dmoney.roadtocareer.net";


    }

}
