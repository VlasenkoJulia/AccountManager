package account_manager.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversionDto {
    private Integer sourceAccountId;
    private Integer targetAccountId;
    private double amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConversionDto)) return false;
        ConversionDto that = (ConversionDto) o;
        return Double.compare(that.getAmount(), getAmount()) == 0 &&
                Objects.equals(getSourceAccountId(), that.getSourceAccountId()) &&
                Objects.equals(getTargetAccountId(), that.getTargetAccountId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSourceAccountId(), getTargetAccountId(), getAmount());
    }
}
