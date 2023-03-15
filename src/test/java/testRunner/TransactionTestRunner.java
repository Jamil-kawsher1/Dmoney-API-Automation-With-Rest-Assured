package testRunner;

import controller.Transaction;
import controller.User;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
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

    public TransactionTestRunner() throws IOException {
        intitconfig();
    }

    @Test(priority = 1, description = "Transaction from System Account To Agent Account ")
    public void systemToAgentTransaction() throws ConfigurationException {
        Response res = transaction.doTransactionFromSystemToAgent();
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        System.out.println(res.asString());
        Assert.assertTrue(message.contains("Deposit successful"));
        String agentLastTransactionId = jsonPath.get("trnxId");

//        Assert.assertEquals(message, "Deposit successful");
        Utils.setEnviromentVariable("agentLastTransactionId", agentLastTransactionId);


    }

    @Test(priority = 2, description = "Transaction from Agent to Customer ")
    public void agentToCustomerTransaction() throws ConfigurationException {
        Response res = transaction.agentToCustomerTransaction();
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("Deposit successful"));
        String agentAndCustomerLastTransactionId = jsonPath.get("trnxId");
//        Assert.assertEquals(message, "Deposit successful");
        Utils.setEnviromentVariable("agentAndCustomerLastTransactionId", agentAndCustomerLastTransactionId);

        System.out.println(res.asString());
    }

    @Test(priority = 3, description = "Check Customer Balance")
    public void checkCustomerBalance() throws ConfigurationException {
        Response res = transaction.checkCustomerBalance();
        JsonPath jsonPath = res.jsonPath();
        String message = jsonPath.get("message");
        System.out.println(res.asString());
        Assert.assertEquals(message, "User balance");
        String balance = jsonPath.get("balance").toString();

        Utils.setEnviromentVariable("customerCurrentBalance", balance);

    }

    @Test(priority = 4, description = "Check Statement")
    public void checkStatement() {
        Response res = transaction.checkStatement();
        JsonPath jsonPath = res.jsonPath();
        System.out.println(res.asString());
        String message = jsonPath.get("message");
        Assert.assertTrue(message.contains("Transaction list"));

    }

    @Test(priority = 5, description = "Money Withdrawal By Customer")
    public void moneyWithdrawalByCustomer() throws InterruptedException {
        Response res = transaction.moneyWithdrawalByCustomer();
        int amount = 1000;
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
    public void createSecondCustomer() throws IOException, ConfigurationException, InterruptedException {
        User user = new User();
        Utils utils = new Utils();
        utils.genrateRandomUser();
        Response response = user.userCreate(utils.getName(), utils.getEmail(), "1234", Utils.randomNumber(), "19" + Utils.randomNumber(), "Customer");
        JsonPath jsonpath = response.jsonPath();


        String message = jsonpath.get("message");

        System.out.println(response.asString());
        Assert.assertTrue(message.contains("User created"));
        Utils.setEnviromentVariable("created2ndUserPhone", jsonpath.get("user.phone_number"));
        Utils.setEnviromentVariable("created2ndUserPassword", jsonpath.get("user.password"));
    }

    @Test(priority = 5, description = "Send money To another Customer")
    public void sendMoney() throws InterruptedException {
        Thread.sleep(3000);
        Response res = transaction.sendMoneyToOtherCustomer();
        System.out.println(res.asString());
    }
@Test(priority = 6,description = "Check Customer Statement")
   public void checkCustomerStatement(){
        Response res=transaction.checkCustomerStatement();
        JsonPath jsonPath=res.jsonPath();
       System.out.println(res.asString());
   }
}


