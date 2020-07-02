package account_manager.account;

import account_manager.repository.account.AccountEntity;
import account_manager.repository.currency.CurrencyEntity;
import account_manager.repository.account.AccountType;
import account_manager.service.validator.AccountValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Before;
import org.junit.Test;

public class AccountEntityValidatorTest {
    private AccountValidator validator;

    @Before
    public void setup() {
        validator = new AccountValidator();
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateGet_AccountIsNull_ShouldThrowException() {
        validator.validateGet(null);
    }

    @Test
    public void validateGet_AccountIsValid() {
        validator.validateGet(new AccountEntity());
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateCreate_AccountIsNotNull_ShouldThrowException() {
        validator.validateCreate(createAccount(1));
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateCreate_AccountCurrencyIsNull_ShouldThrowException() {
        validator.validateCreate(createAccount(null));
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateCreate_AccountCurrencyCodeIsEmptyString_ShouldThrowException() {
        validator.validateCreate(createAccount(null, AccountType.CURRENT, ""));
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateCreate_AccountTypeIsNull_ShouldThrowException() {
        validator.validateCreate(createAccount(null, null, "980"));
    }

    @Test
    public void validateCreate_AccountIsValid() {
        validator.validateGet(createAccount(null, AccountType.DEPOSIT, "980"));
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateUpdate_AccountIdNull_ShouldThrowException() {
        validator.validateUpdate(createAccount(null));
    }

    @Test
    public void validateUpdate_AccountIsValid() {
        validator.validateUpdate(createAccount(1));
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateGetByClientId_ClientIdIsNull_ShouldThrowException() {
        validator.validateGetByClientId(null);
    }

    @Test
    public void validateGetByClientId_ClientIdIsValid() {
        validator.validateGetByClientId(1);
    }

    private AccountEntity createAccount(Integer id, AccountType type, String currencyCode) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);
        accountEntity.setType(type);
        accountEntity.setCurrency(createCurrency(currencyCode));
        return accountEntity;
    }

    private AccountEntity createAccount(Integer id) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);
        return accountEntity;
    }

    private CurrencyEntity createCurrency(String code) {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setCode(code);
        return currencyEntity;
    }

}
