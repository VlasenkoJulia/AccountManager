package account_manager.currency_converter;

public class ConversionDto {
    private Integer sourceAccountId;
    private Integer targetAccountId;
    private double amount;

    public ConversionDto(Integer sourceAccountId, Integer targetAccountId, double amount) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    public Integer getSourceAccountId() {
        return sourceAccountId;
    }

    public Integer getTargetAccountId() {
        return targetAccountId;
    }

    public double getAmount() {
        return amount;
    }
}
