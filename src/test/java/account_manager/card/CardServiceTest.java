package account_manager.card;

import account_manager.repository.account.Account;
import account_manager.repository.account.AccountType;
import account_manager.repository.card.Card;
import account_manager.repository.card.CardRepository;
import account_manager.repository.client.Client;
import account_manager.repository.currency.Currency;
import account_manager.service.CardService;
import account_manager.service.converter.Converter;
import account_manager.service.dto.CardDto;
import account_manager.service.validator.CardValidator;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class CardServiceTest {
    @Mock
    private CardValidator validator;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private Converter<Card, CardDto> converter;
    @InjectMocks
    private CardService cardService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = InputParameterValidationException.class)
    public void getById_CardNotFound_ShouldThrowException() {
        when(cardRepository.findById(1)).thenReturn(Optional.empty());
        doThrow(InputParameterValidationException.class).when(validator).validateGet(null);
        cardService.getById(1);
    }

    @Test
    public void getById_CardFound_ShouldReturnCardDto() {
        Client client = new Client(1, "John", "Doe");
        Currency currency = new Currency("840", 1.0, "US Dollar", "USD");
        Account account = new Account(1, "111", currency, AccountType.CURRENT, 1.0, Instant.ofEpochSecond(0L), client, Collections.emptySet());
        Card card = createCard(1, "111");
        card.setAccounts(Collections.singletonList(account));
        when(cardRepository.findById(1)).thenReturn(Optional.of(card));
        doNothing().when(validator).validateGet(card);
        CardDto cardDto = new CardDto(1, "111", List.of(1));
        when(converter.convertFrom(card)).thenReturn(cardDto);
        CardDto foundCard = cardService.getById(1);
        Assert.assertEquals(cardDto, foundCard);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_CardNotValid_ShouldThrowException() {
        CardDto cardDto = new CardDto(1, "111", Collections.emptyList());
        Card card = createCard(1, "111");
        when(converter.convertTo(cardDto)).thenReturn(card);
        doThrow(InputParameterValidationException.class).when(validator).validateCreate(card);
        cardService.create(cardDto);
    }

    @Test
    public void create_CardValid_ShouldReturnSuccessMessage() {
        CardDto cardDto = new CardDto(1, "111", Collections.emptyList());
        Card card = createCard(null, "111");
        when(converter.convertTo(cardDto)).thenReturn(card);
        doNothing().when(validator).validateCreate(card);
        card.setId(1);
        when(cardRepository.save(card)).thenReturn(card);
        String message = cardService.create(cardDto);
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
