package model;

public class TransactionModel {

    public String from_account, to_account, amount;

    public TransactionModel (String from_account, String to_account, String amount) {
        this.from_account = from_account;
        this.to_account = to_account;
        this.amount = amount;
    }
}
