package account_manager.account;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class Account {
    private String number;
    private String currencyCode;
    private AccountType type;
    private int ownerId;
    private Integer id;
    private double balance;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    private Date openDate;

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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd.MM.yyyy")
    public Date getOpenDate() {
        return openDate;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }
}
