package controller;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.TransactionModel;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import setup.Setup;
import utils.Utils;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class Transaction extends Setup {

    public Transaction () throws IOException {
        intitconfig();
    }

    public void doTransactionFromSystemToAgent () throws ConfigurationException {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        TransactionModel systemToAgentTransaction = new TransactionModel("SYSTEM", (String) prop.get("createdAgentPhone"), "5000");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.get("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(systemToAgentTransaction)
                        .when()
                        .post("transaction/deposit")
                        .then().statusCode(201).extract().response();
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        String agentLastTransactionId = jsonPath.get("trnxId");

        Assert.assertEquals(message, "Deposit successful");
        Utils.setEnviromentVariable("agentLastTransactionId", agentLastTransactionId);
        System.out.println(res.asString());
    }

    public void agentToCustomerTransaction () throws ConfigurationException {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        TransactionModel agentTransactionModel = new TransactionModel((String) prop.get("createdAgentPhone"), (String) prop.get("createdUserPhone"), "2000");
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.get("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(agentTransactionModel)
                        .when()
                        .post("transaction/deposit")
                        .then().statusCode(201).extract().response();
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        String agentAndCustomerLastTransactionId = jsonPath.get("trnxId");
        Assert.assertEquals(message, "Deposit successful");
        Utils.setEnviromentVariable("agentAndCustomerLastTransactionId", agentAndCustomerLastTransactionId);

        System.out.println(res.asString());
    }
    public void checkCustomerBalance(){
        RestAssured.baseURI="http://dmoney.roadtocareer.net";

        Response res=
                given()
                        .contentType("application/json")
                        .header("Authorization",prop.get("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .when()
                        .get("transaction/balance/"+prop.get("createdUserPhone"))
                        .then().extract().response();

        JsonPath jsonPath=res.jsonPath();
        String message=jsonPath.get("message");
        Assert.assertEquals(message,"User balance");
        System.out.println(res.asString());


    }
}
