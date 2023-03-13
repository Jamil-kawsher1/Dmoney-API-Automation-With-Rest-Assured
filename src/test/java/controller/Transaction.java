package controller;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.TransactionModel;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.xml.internal.Parser;
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
        TransactionModel systemToAgentTransaction = new TransactionModel("SYSTEM", prop.getProperty("createdAgentPhone"), 5000);
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(systemToAgentTransaction)
                        .when()
                        .post("transaction/deposit")
                        .then().extract().response();
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        String agentLastTransactionId = jsonPath.get("trnxId");

//        Assert.assertEquals(message, "Deposit successful");
        Utils.setEnviromentVariable("agentLastTransactionId", agentLastTransactionId);
        System.out.println(res.asString());
        System.out.println(prop.getProperty("createdAgentPhone"));
    }

    public void agentToCustomerTransaction () throws ConfigurationException {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        TransactionModel agentTransactionModel = new TransactionModel(prop.getProperty("createdAgentPhone"), prop.getProperty("createdUserPhone"), 2000);
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(agentTransactionModel)
                        .when()
                        .post("transaction/deposit")
                        .then().extract().response();
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        String agentAndCustomerLastTransactionId = jsonPath.get("trnxId");
//        Assert.assertEquals(message, "Deposit successful");
        Utils.setEnviromentVariable("agentAndCustomerLastTransactionId", agentAndCustomerLastTransactionId);

        System.out.println(res.asString());
    }

    public void checkCustomerBalance () throws ConfigurationException {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";

        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .when()
                        .get("transaction/balance/" + prop.getProperty("createdUserPhone"))
                        .then().extract().response();

        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        String balance = jsonPath.get("balance").toString();
        Assert.assertEquals(message, "User balance");
        Utils.setEnviromentVariable("customerCurrentBalance", balance);
        System.out.println(res.asString());


    }

    public void checkStatement () {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .when()
                        .get("transaction/search/" + prop.getProperty("agentAndCustomerLastTransactionId"))
                        .then()
                        .extract().response();
        System.out.println(res.asString());

    }

    public void moneyWithdrawalByCustomer () throws InterruptedException {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        int amount = 1000;
        TransactionModel withdrawal = new TransactionModel(prop.getProperty("createdUserPhone"), prop.getProperty("createdAgentPhone"), amount);
        int customerCurrentBalance = Integer.parseInt(prop.getProperty("customerCurrentBalance"));
        Thread.sleep(2000);
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(withdrawal)
                        .when()
                        .post("transaction/withdraw/")
                        .then()
                        .extract().response();
        JsonPath jsonPath = res.jsonPath();
        int fee = jsonPath.get("fee");
        int currentBal = jsonPath.get("currentBalance");
        int expectedBalance = (customerCurrentBalance - (amount + fee));
        Assert.assertEquals(currentBal, expectedBalance);
        System.out.println(res.asString());


    }

@BeforeTest
@Test
    public void sendMoneyToOtherCustomer(){


    }
}

