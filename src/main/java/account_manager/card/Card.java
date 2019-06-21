package account_manager.card;

import account_manager.account.Account;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String number;
    @ManyToMany(mappedBy = "cards")
    private List<Account> accounts;

    public Card() {
    }

    public Card(Integer id, String number) {
        this.id = id;
        this.number = number;
    }

    public Card(Integer id, String number, List<Account> accounts) {
        this.id = id;
        this.number = number;
        this.accounts = accounts;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return Objects.equals(getId(), card.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
