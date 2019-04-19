package account_manager.card;

import java.util.HashSet;
import java.util.Set;

public class Card {
    private int id;
    private String number;
    private Set<Integer> accountIds;

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

    public Set<Integer> getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(Set<Integer> accountIds) {
        this.accountIds = accountIds;
    }

    public void addAccountId(int accountId) {
        if (accountIds == null) {
            accountIds = new HashSet<>();
        }
        accountIds.add(accountId);
    }
}
