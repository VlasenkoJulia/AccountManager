package account_manager.repository.card;

import account_manager.repository.account.AccountEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "card")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String number;
    @ManyToMany(mappedBy = "cards")
    private List<AccountEntity> accounts;

    public CardEntity(Integer id, String number) {
        this.id = id;
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CardEntity)) return false;
        CardEntity cardEntity = (CardEntity) o;
        return Objects.equals(getId(), cardEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
