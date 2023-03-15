package testRunner;

import controller.Transaction;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import setup.Setup;
import utils.Utils;

import java.io.IOException;


public class TransactionTestRunner extends Setup {

    static  Transaction transaction;

    static {
        try {
            transaction = new Transaction();
            intitconfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test(priority = 1, description = "System To User Account Transaction With Invalid get method",enabled = true)
    public void systemToUserTransactionWithInvalidMethod () throws ConfigurationException, InterruptedException {
        Thread.sleep(5000);
        boolean methodFlag = false;

        Response res = transaction.doTransactionFromSystemToAgent(methodFlag);
        JsonPath jsonPath = res.jsonPath();
        String erroMessage = jsonPath.get("error.message");
        System.out.println(res.asString());
//        Assert.assertTrue(message.contains("Deposit successful"));


        Assert.assertTrue(erroMessage.contains("Not Found"));

    }

    @Test(priority = 2, description = "Transaction from System Account To Agent Account ",enabled = true)
    public void systemToAgentTransaction () throws ConfigurationException {
        boolean methodFlag = true;

        Response res = transaction.doTransactionFromSystemToAgent(methodFlag);
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        System.out.println(res.asString());
        Assert.assertTrue(message.contains("Deposit successful"));
        String agentLastTransactionId = jsonPath.get("trnxId");

//        Assert.assertEquals(message, "Deposit successful");
        Utils.setEnviromentVariable("agentLastTransactionId", agentLastTransactionId);


    }

    @Test(priority = 3, description = "Agent To Customer deposit with Invalid Customer Number",enabled = true)
    public void agentToCustomerTransactionWithInvalidPhoneNumber () throws ConfigurationException, IOException, InterruptedException {
        intitconfig();
        Thread.sleep(3000);
        String agentPhoneNumber = prop.getProperty("createdAgentPhone");
        String customerPhoneNumber = "01819677097";


        Response res = transaction.agentToCustomerTransaction(agentPhoneNumber, customerPhoneNumber, 2000);
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        System.out.println(res.asString());
        Assert.assertTrue(message.contains("Account does not exist"));


    }

    @Test(priority = 4, description = "Transaction from Agent to Customer ",enabled = true)
    public void agentToCustomerTransaction () throws ConfigurationException, InterruptedException, IOException {
        intitconfig();
        Thread.sleep(3000);
        String agentPhoneNumber = prop.getProperty("createdAgentPhone");
        String customerPhoneNumber = prop.getProperty("createdUserPhone");


        Response res = transaction.agentToCustomerTransaction(agentPhoneNumber, customerPhoneNumber, 2000);
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("Deposit successful"));
        String agentAndCustomerLastTransactionId = jsonPath.get("trnxId");
//        Assert.assertEquals(message, "Deposit successful");
        Utils.setEnviromentVariable("agentAndCustomerLastTransactionId", agentAndCustomerLastTransactionId);

        System.out.println(res.asString());
    }

    @Test(priority = 5, description = "Check Customer Balance with Invalid POST Method",enabled = true)
    public void checkCustomerBalanceWithInvalidMethod () throws ConfigurationException {
        boolean httpMethod = true;
        Response res = transaction.checkCustomerBalance(httpMethod);
        JsonPath jsonPath = res.jsonPath();
        String errorMessage = jsonPath.get("error.message");
        System.out.println(res.asString());
        Assert.assertTrue(errorMessage.contains("Not Found"));


    }

    @Test(priority = 6, description = "Check Customer Balance with  valid GET Method",enabled = true)
    public void checkCustomerBalance () throws ConfigurationException {
        boolean httpMethod = false;
        Response res = transaction.checkCustomerBalance(httpMethod);
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        System.out.println(res.asString());
        Assert.assertEquals(message, "User balance");
        String balance = jsonPath.get("balance").toString();

        Utils.setEnviromentVariable("customerCurrentBalance", balance);

    }

    @Test(priority = 7, description = "Check Customer  Statement with Invalid Transaction Id")
    public void checkStatementWithInvalidTransectionID () {
        String customerTransactionId = "gfyedgfg";
        Response res = transaction.checkStatement(customerTransactionId);
        JsonPath jsonPath = res.jsonPath();
        System.out.println(res.asString());
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("Transaction not found"));

    }

    @Test(priority = 8, description = "Check Statement with Transaction ID")

    public void checkStatement () throws IOException, InterruptedException {
        intitconfig();
        Thread.sleep(3000);
        String customerTransactionId = prop.getProperty("agentAndCustomerLastTransactionId");
        System.out.println(customerTransactionId);
        Response res = transaction.checkStatement(customerTransactionId);
        JsonPath jsonPath = res.jsonPath();
        System.out.println(res.asString());
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("Transaction list"));

    }

    @Test(priority = 9, description = "Money Withdrawal By Customer to An invalid Agent number",enabled = true)
    public void moneyWithdrawalByCustomerToAnInvalidAgentNumber () throws InterruptedException {
        String customerPhoneNumber = prop.getProperty("createdUserPhone");
        String agentPhoneNumber = "01819677097";
        int amount = 1000;

        Response res = transaction.moneyWithdrawalByCustomer(customerPhoneNumber, agentPhoneNumber, amount);
        Thread.sleep(3000);
        System.out.println(res.asString());
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");

        Assert.assertTrue(message.contains("Account does not exist"));
    }

    @Test(priority = 10, description = "Money Withdrawal By Customer",enabled = true)
    public void moneyWithdrawalByCustomer () throws InterruptedException, IOException {
        intitconfig();
        Thread.sleep(3000);
        String customerPhoneNumber = prop.getProperty("createdUserPhone");
        String agentPhoneNumber = prop.getProperty("createdAgentPhone");
        int amount = 1000;

        Response res = transaction.moneyWithdrawalByCustomer(customerPhoneNumber, agentPhoneNumber, amount);
        intitconfig();
        int customerCurrentBalance = Integer.parseInt(prop.getProperty("customerCurrentBalance"));
        Thread.sleep(3000);
        System.out.println(res.asString());
        JsonPath jsonPath = res.jsonPath();
        int fee = jsonPath.get("fee");
        int currentBal = jsonPath.get("currentBalance");
        int expectedBalance = (customerCurrentBalance - (amount + fee));
        Assert.assertEquals(currentBal, expectedBalance);

    }


    @Test(priority = 11, description = "Send money To an invalid  Customer Number",enabled = true)
    public void sendMoneyToAnInvalidCustomerNumber () throws InterruptedException {
        String fromNumber = prop.getProperty("createdUserPhone");
        String toNumber = "01819677097";
        int amount = 500;

        Thread.sleep(3000);
        Response res = transaction.sendMoneyToOtherCustomer(fromNumber, toNumber, amount);
        System.out.println(res.asString());
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("From/To Account does not exist"));
    }

    @Test(priority = 12, description = "Send money To another Customer",enabled = true)
    public void sendMoney () throws InterruptedException, IOException {
        intitconfig();
        Thread.sleep(3000);
        String fromNumber = prop.getProperty("createdUserPhone");
        String toNumber = prop.getProperty("created2ndUserPhone");
        int amount = 500;

        Thread.sleep(3000);
        Response res = transaction.sendMoneyToOtherCustomer(fromNumber, toNumber, amount);
        System.out.println(res.asString());
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("Send money successful"));
    }

    @Test(priority = 13, description = "Check Customer Statement with phone Number",enabled = true)
    public void checkCustomerStatementWithInvalidPhoneNumber () {
        String firstCustomerPhone = "01819677097";
        Response res = transaction.checkCustomerStatement(firstCustomerPhone);
        JsonPath jsonPath = res.jsonPath();
        System.out.println(res.asString());
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("User not found"));
    }

    @Test(priority = 14, description = "Check Customer Statement with phone Number",enabled = true)
    public void checkCustomerStatement () {
        String firstCustomerPhone = prop.getProperty("createdUserPhone");
        Response res = transaction.checkCustomerStatement(firstCustomerPhone);
        JsonPath jsonPath = res.jsonPath();
        System.out.println(res.asString());
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("Transaction list"));
    }
}


