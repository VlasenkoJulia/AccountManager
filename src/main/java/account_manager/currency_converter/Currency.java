package account_manager.currency_converter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Currency {
    @Id
    private String code;
    private double rate;
    private String name;
    @Column(name = "ISO")
    private String iso;

    public Currency() {
    }

    public Currency(String code, double rate, String name, String iso) {
        this.code = code;
        this.rate = rate;
        this.name = name;
        this.iso = iso;
    }

    public String getCode() {
        return code;
    }

    public double getRate() {
        return rate;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Currency)) return false;
        Currency currency = (Currency) o;
        return Objects.equals(getCode(), currency.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode());
    }
}
