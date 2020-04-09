package account_manager.card;

import account_manager.repository.account.Account;
import account_manager.repository.card.Card;
import account_manager.service.validator.CardValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class CardValidatorTest {
    private CardValidator validator;

    @Before
    public void setup() {
        validator = new CardValidator();
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateGet_CardIsNull_ShouldThrowException() {
        validator.validateGet(null);
    }

    @Test
    public void validateGet_CardIsValid() {
        validator.validateGet(new Card());
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateCreate_CardIdIsNotNull_ShouldThrowException() {
        validator.validateCreate(createCard(1, new ArrayList<>()));
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateCreate_AccountListIsEmpty_ShouldThrowException() {
        validator.validateCreate(createCard(null, new ArrayList<>()));
    }

    @Test(expected = InputParameterValidationException.class)
    public void validateCreate_AccountListIsNull_ShouldThrowException() {
        validator.validateCreate(createCard(null, null));
    }

    @Test
    public void validateCreate_cardIsValid() {
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(new Account());
        validator.validateCreate(createCard(null, accounts));
    }

    private Card createCard(Integer cardId, List<Account> accounts) {
        Card card = new Card();
        card.setId(cardId);
        card.setAccounts(accounts);
        return card;
    }
}
