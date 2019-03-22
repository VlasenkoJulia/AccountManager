package account_manager.currency_converter;

public class Currency {
    private String code;
    private double rate;

    public Currency(String code, double rate) {
        this.code = code;
        this.rate = rate;
    }

    public String getCode() {
        return code;
    }

    public double getRate() {
        return rate;
    }
}
