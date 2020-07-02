package account_manager.repository.currency;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="currency")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyEntity {
    @Id
    private String code;
    private double rate;
    private String name;
    @Column(name = "ISO")
    private String iso;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CurrencyEntity)) return false;
        CurrencyEntity currencyEntity = (CurrencyEntity) o;
        return Objects.equals(getCode(), currencyEntity.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
