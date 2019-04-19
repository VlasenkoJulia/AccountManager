package account_manager.currency_converter;

public class Currency {
    private String code;
    private double rate;

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
}
