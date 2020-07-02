package account_manager.card;

import account_manager.repository.account.AccountEntity;
import account_manager.repository.account.AccountType;
import account_manager.repository.card.CardEntity;
import account_manager.repository.card.CardRepository;
import account_manager.repository.client.ClientEntity;
import account_manager.repository.currency.CurrencyEntity;
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

public class CardEntityServiceTest {
    @Mock
    private CardValidator validator;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private Converter<CardEntity, CardDto> converter;
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
        ClientEntity clientEntity = new ClientEntity(1, "John", "Doe");
        CurrencyEntity currencyEntity = new CurrencyEntity("840", 1.0, "US Dollar", "USD");
        AccountEntity accountEntity = new AccountEntity(1, "111", currencyEntity, AccountType.CURRENT, 1.0, Instant.ofEpochSecond(0L), clientEntity, Collections.emptySet());
        CardEntity cardEntity = createCard(1, "111");
        cardEntity.setAccounts(Collections.singletonList(accountEntity));
        when(cardRepository.findById(1)).thenReturn(Optional.of(cardEntity));
        doNothing().when(validator).validateGet(cardEntity);
        CardDto cardDto = new CardDto(1, "111", List.of(1));
        when(converter.convertFrom(cardEntity)).thenReturn(cardDto);
        CardDto foundCard = cardService.getById(1);
        Assert.assertEquals(cardDto, foundCard);
    }

    @Test(expected = InputParameterValidationException.class)
    public void create_CardNotValid_ShouldThrowException() {
        CardDto cardDto = new CardDto(1, "111", Collections.emptyList());
        CardEntity cardEntity = createCard(1, "111");
        when(converter.convertTo(cardDto)).thenReturn(cardEntity);
        doThrow(InputParameterValidationException.class).when(validator).validateCreate(cardEntity);
        cardService.create(cardDto);
    }

    @Test
    public void create_CardValid_ShouldReturnSuccessMessage() {
        CardDto cardDto = new CardDto(1, "111", Collections.emptyList());
        CardEntity cardEntity = createCard(null, "111");
        when(converter.convertTo(cardDto)).thenReturn(cardEntity);
        doNothing().when(validator).validateCreate(cardEntity);
        cardEntity.setId(1);
        when(cardRepository.save(cardEntity)).thenReturn(cardEntity);
        String message = cardService.create(cardDto);
        Assert.assertEquals("Created card #111", message);
    }

    @Test
    public void deleteById_CardFound_ShouldReturnSuccessMessage() {
        doNothing().when(cardRepository).deleteById(1);
        String message = cardService.deleteById(1);
        Assert.assertEquals("Deleted card #1", message);
    }


    private CardEntity createCard(Integer cardId, String number) {
        CardEntity cardEntity = new CardEntity();
        cardEntity.setId(cardId);
        cardEntity.setNumber(number);
        return cardEntity;
    }
}
