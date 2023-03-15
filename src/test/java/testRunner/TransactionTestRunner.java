package testRunner;

import controller.Transaction;
import controller.User;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import model.TransactionModel;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import setup.Setup;
import utils.Utils;

import java.io.IOException;


public class TransactionTestRunner extends Setup {
    Transaction transaction = new Transaction();

    public TransactionTestRunner () throws IOException {
        intitconfig();
    }

    @Test(priority = 1, description = "System To User Account Transaction With Invalid get method")
    public void systemToUserTransactionWithInvalidMethod () throws ConfigurationException {
        boolean methodFlag = false;
        Response res = transaction.doTransactionFromSystemToAgent(methodFlag);
        JsonPath jsonPath = res.jsonPath();
        String erroMessage = jsonPath.get("error.message");
        System.out.println(res.asString());
//        Assert.assertTrue(message.contains("Deposit successful"));


        Assert.assertTrue(erroMessage.contains("Not Found"));

    }

    @Test(priority = 2, description = "Transaction from System Account To Agent Account ")
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

    @Test(priority = 3, description = "Agent To Customer deposit with Invalid Customer Number")
    public void agentToCustomerTransactionWithInvalidPhoneNumber () throws ConfigurationException {
        String agentPhoneNumber = prop.getProperty("createdAgentPhone");
        String customerPhoneNumber = "01819677097";


        Response res = transaction.agentToCustomerTransaction(agentPhoneNumber, customerPhoneNumber, 2000);
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        System.out.println(res.asString());
        Assert.assertTrue(message.contains("Account does not exist"));


    }

    @Test(priority = 4, description = "Transaction from Agent to Customer ")
    public void agentToCustomerTransaction () throws ConfigurationException {
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

    @Test(priority = 5, description = "Check Customer Balance with Invalid POST Method")
    public void checkCustomerBalanceWithInvalidMethod () throws ConfigurationException {
        boolean httpMethod = true;
        Response res = transaction.checkCustomerBalance(httpMethod);
        JsonPath jsonPath = res.jsonPath();
        String errorMessage = jsonPath.get("error.message");
        System.out.println(res.asString());
        Assert.assertTrue(errorMessage.contains("Not Found"));


    }

    @Test(priority = 6, description = "Check Customer Balance with  valid GET Method")
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
    public void checkStatement () {
        String customerTransactionId = prop.getProperty("agentAndCustomerLastTransactionId");
        Response res = transaction.checkStatement(customerTransactionId);
        JsonPath jsonPath = res.jsonPath();
        System.out.println(res.asString());
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("Transaction list"));

    }

    @Test(priority = 9, description = "Money Withdrawal By Customer to An invalid Agent number")
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

    @Test(priority = 10, description = "Money Withdrawal By Customer")
    public void moneyWithdrawalByCustomer () throws InterruptedException {
        String customerPhoneNumber = prop.getProperty("createdUserPhone");
        String agentPhoneNumber = prop.getProperty("createdAgentPhone");
        int amount = 1000;

        Response res = transaction.moneyWithdrawalByCustomer(customerPhoneNumber, agentPhoneNumber, amount);
        int customerCurrentBalance = Integer.parseInt(prop.getProperty("customerCurrentBalance"));
        Thread.sleep(3000);
        System.out.println(res.asString());
        JsonPath jsonPath = res.jsonPath();
        int fee = jsonPath.get("fee");
        int currentBal = jsonPath.get("currentBalance");
        int expectedBalance = (customerCurrentBalance - (amount + fee));
        Assert.assertEquals(currentBal, expectedBalance);

    }

    @BeforeTest
    public void createSecondCustomer () throws IOException, ConfigurationException, InterruptedException {
        User user = new User();
        Utils utils = new Utils();
        utils.genrateRandomUser();
        String token = prop.getProperty("token");
        Thread.sleep(3000);
        Response response = user.userCreate(utils.getName(), utils.getEmail(), "1234", Utils.randomNumber(), "19" + Utils.randomNumber(), "Customer", token);
        JsonPath jsonpath = response.jsonPath();


        String message = jsonpath.get("message");

        System.out.println(response.asString());
        Assert.assertTrue(message.contains("User created"));
        Utils.setEnviromentVariable("created2ndUserPhone", jsonpath.get("user.phone_number"));
        Utils.setEnviromentVariable("created2ndUserPassword", jsonpath.get("user.password"));
    }

    @Test(priority = 11, description = "Send money To another Customer")
    public void sendMoney () throws InterruptedException {
        Thread.sleep(3000);
        Response res = transaction.sendMoneyToOtherCustomer();
        System.out.println(res.asString());
    }

    @Test(priority = 12, description = "Check Customer Statement with phone Number")
    public void checkCustomerStatement () {
        Response res = transaction.checkCustomerStatement();
        JsonPath jsonPath = res.jsonPath();
        System.out.println(res.asString());
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("Transaction list"));
    }
}


