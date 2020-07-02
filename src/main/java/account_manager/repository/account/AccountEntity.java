package account_manager.repository.account;

import account_manager.repository.card.CardEntity;
import account_manager.repository.client.ClientEntity;
import account_manager.repository.currency.CurrencyEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_code")
    private CurrencyEntity currency;

    @Enumerated(EnumType.STRING)
    private AccountType type;

    private Double balance;

    @Column(name = "open_date")
    private Instant openDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ManyToMany
    @JoinTable(
            name = "account_cards",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "card_id")
    )
    private Set<CardEntity> cards;

    @PrePersist
    void openDate() {
        this.openDate = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountEntity)) return false;
        AccountEntity accountEntity = (AccountEntity) o;
        return Objects.equals(getId(), accountEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
