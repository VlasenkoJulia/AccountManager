package account_manager.card;

import java.util.Set;

public class Card {
    private int id;
    private String number;
    private Set<Integer> accountsId;

    public Card(String number, Set<Integer> accountsId) {
        this.number = number;
        this.accountsId = accountsId;
    }

    public Card(int id, String number, Set<Integer> accountsId) {
        this.id = id;
        this.number = number;
        this.accountsId = accountsId;
    }

    public String getNumber() {
        return number;
    }

    public int getId() {
        return id;
    }

    public Set<Integer> getAccountsId() {
        return accountsId;
    }
}
