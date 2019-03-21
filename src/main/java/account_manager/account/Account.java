package account_manager.account;

import java.util.Date;

public class Account {
    private String number;
    private String currencyCode;
    private AccountType type;
    private int ownerId;
    private Integer id;
    private double balance;
    private Date openDate;

    public Account(String number, String currencyCode, AccountType type, int ownerId, Integer id, double balance, Date openDate) {
        this.number = number;
        this.currencyCode = currencyCode;
        this.type = type;
        this.ownerId = ownerId;
        this.id = id;
        this.balance = balance;
        this.openDate = openDate;
    }

    public Account(String number, String currencyCode, AccountType type, int ownerId) {
        this.number = number;
        this.currencyCode = currencyCode;
        this.type = type;
        this.ownerId = ownerId;
    }

    public String getNumber() {
        return number;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public AccountType getType() {
        return type;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public Integer getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
