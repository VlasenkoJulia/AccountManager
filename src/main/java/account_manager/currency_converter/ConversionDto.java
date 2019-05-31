package account_manager.currency_converter;

public class ConversionDto {
    private Integer sourceAccountId;
    private Integer targetAccountId;
    private double amount;

    public Integer getSourceAccountId() {
        return sourceAccountId;
    }

    public Integer getTargetAccountId() {
        return targetAccountId;
    }

    public double getAmount() {
        return amount;
    }

    public void setSourceAccountId(Integer sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public void setTargetAccountId(Integer targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ConversionDto(Integer sourceAccountId, Integer targetAccountId, double amount) {
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
    }

    public ConversionDto() {
    }
}
