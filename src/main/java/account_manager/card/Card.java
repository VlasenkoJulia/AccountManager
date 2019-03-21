package account_manager.card;

import java.util.HashSet;
import java.util.Set;

public class Card {
    private int id;
    private String number;
    private Set<Integer> accountsId;

    public Card(String number, Set<Integer> accountsId) {
        this.number = number;
        this.accountsId = accountsId;
    }

    Card(int id, String number, Set<Integer> accountsId) {
        this.id = id;
        this.number = number;
        this.accountsId = accountsId;
    }

    public Card() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Set<Integer> getAccountsId() {
        return accountsId;
    }

    public void setAccountsId(Set<Integer> accountsId) {
        this.accountsId = accountsId;
    }

    public void addAccountId(int accountId) {
        if (accountsId == null) {
            accountsId = new HashSet<>();
        }
        accountsId.add(accountId);
    }
}
