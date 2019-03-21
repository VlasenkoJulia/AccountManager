package account_manager.account;

public enum AccountType {
    DEPOSIT("deposit"),
    CURRENT("current");

    private String title;

    AccountType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
