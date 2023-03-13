package testRunner;

import controller.Transaction;
import org.apache.commons.configuration.ConfigurationException;
import org.testng.annotations.Test;

import java.io.IOException;


public class TransactionTestRunner {
    Transaction transaction = new Transaction();

    public TransactionTestRunner () throws IOException {

    }

    @Test(priority = 1,description = "Transaction from System Account To Agent Account ")
    public void systemToAgentTransaction () throws ConfigurationException {
        transaction.doTransactionFromSystemToAgent();

    }

    @Test(priority = 2,description = "Transaction from Agent to Customer ")
    public  void agentToCustomerTransaction() throws ConfigurationException {
        transaction.agentToCustomerTransaction();
    }
    @Test(priority = 3,description = "Check Customer Balance")
    public void checkCustomerBalance() throws ConfigurationException {
        transaction.checkCustomerBalance();
    }
@Test(priority =4,description = "Check Statement")
    public void checkStatement(){
        transaction.checkStatement();
    }
@Test(priority = 5,description = "Money Withdrawal By Customer")
    public void moneyWithdrawalByCustomer() throws InterruptedException {
        transaction.moneyWithdrawalByCustomer();
    }
}


