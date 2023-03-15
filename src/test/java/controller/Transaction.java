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

    public Transaction() throws IOException {
        intitconfig();
    }

    public Response doTransactionFromSystemToAgent(boolean methodFlag) throws ConfigurationException {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
      String methodName=methodFlag?"POST":"GET";
        TransactionModel systemToAgentTransaction = new TransactionModel("SYSTEM", prop.getProperty("createdAgentPhone"), 5000);
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(systemToAgentTransaction)
                        .when().request(methodName,"transaction/deposit");

//                        .then().extract().response();
        return res;

    }

    public Response agentToCustomerTransaction(String agentPhoneNumber,String customerPhoneNumber,int ammount) throws ConfigurationException {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        TransactionModel agentTransactionModel = new TransactionModel(agentPhoneNumber, customerPhoneNumber, ammount);
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(agentTransactionModel)
                        .when()
                        .post("transaction/deposit");
//                        .then().extract().response();

        return res;


    }

    public Response checkCustomerBalance(boolean methodFlag) throws ConfigurationException {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        String methodName=methodFlag?"POST":"GET";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .when().request(methodName,"transaction/balance/" + prop.getProperty("createdUserPhone"));

//                        .then().extract().response();

        return res;



    }

    public Response checkStatement(String transactionId) {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .when()
                        .get("transaction/search/" + transactionId)
                        .then()
                        .extract().response();
        return res;


    }

    public Response moneyWithdrawalByCustomer(String customerPhoneNumber,String agentPhoneNumber,int amount) throws InterruptedException {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";

        TransactionModel withdrawal = new TransactionModel(customerPhoneNumber,agentPhoneNumber, amount);

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
        return res;



    }



    public Response sendMoneyToOtherCustomer() {
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";

        TransactionModel sendMoney = new TransactionModel(prop.getProperty("createdUserPhone"), prop.getProperty("created2ndUserPhone"), 500);


        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .body(sendMoney)
                        .when()
                        .post("transaction/sendmoney/")
                        .then()
                        .extract().response();
        return res;
    }

    public Response checkCustomerStatement(){
        RestAssured.baseURI = "http://dmoney.roadtocareer.net";
        String firstCustomerPhone=prop.getProperty("createdUserPhone");




        Response res =
                given()
                        .contentType("application/json")
                        .header("Authorization", prop.getProperty("token"))
                        .header("X-AUTH-SECRET-KEY", "ROADTOSDET")
                        .get("transaction/statement/"+firstCustomerPhone)
                        .then()
                        .extract().response();
        return res;
    }
    }


