package account_manager.card;

import account_manager.account.Account;
import account_manager.account.AccountType;
import account_manager.client.Client;
import account_manager.currency_converter.Currency;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.mockito.Mockito.*;

public class CardServiceTest {
    @Mock
    private CardValidator validator;
    @Mock
    private CardRepository cardRepository;
    @InjectMocks
    private CardService cardService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InputParameterValidationException.class)
    public void getById_CardNotFound_ShouldThrowException() {
        when(cardRepository.getById(1)).thenReturn(null);
        doThrow(InputParameterValidationException.class).when(validator).validateGet(null);
        cardService.getById(1);
    }

    @Test
    public void getById_CardFound_ShouldReturnCard() {
        Client client = new Client(1, "John", "Doe");
        Currency currency = new Currency("840", 1.0, "US Dollar", "USD");
        Account account = new Account(1, "111", currency, AccountType.CURRENT, 1.0, new Date(1L), client, Collections.emptySet());
        Card card = createCard(1, "111");
        card.setAccounts(new ArrayList<>(Collections.singletonList(account)));
        when(cardRepository.getById(1)).thenReturn(card);
        doNothing().when(validator).validateGet(card);
        Card foundCard = cardService.getById(1);
        Assert.assertEquals(card, foundCard);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_CardNotValid_ShouldThrowException() {
        Card card = createCard(1, "111");
        doThrow(InputParameterValidationException.class).when(validator).validateCreate(card);
        cardService.create(card);
    }

    @Test
    public void create_CardValid_ShouldReturnSuccessMessage() {
        Card card = createCard(null, "111");
        doNothing().when(validator).validateCreate(card);
        doNothing().when(cardRepository).create(card);
        String message = cardService.create(card);
        Assert.assertEquals("Created card #111", message);
    }

    @Test
    public void deleteById_CardFound_ShouldReturnSuccessMessage() {
        doNothing().when(cardRepository).deleteById(1);
        String message = cardService.deleteById(1);
        Assert.assertEquals("Deleted card #1", message);
    }


    private Card createCard(Integer cardId, String number) {
        Card card = new Card();
        card.setId(cardId);
        card.setNumber(number);
        return card;
    }

}