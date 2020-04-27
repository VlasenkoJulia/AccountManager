package account_manager.service.converter;

import account_manager.repository.account.Account;
import account_manager.repository.card.Card;
import account_manager.service.dto.CardDto;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CardConverterTest {
    private CardConverter cardConverter = new CardConverter();

    @Test
    public void convertToEntityFromDto() {
        CardDto dto = new CardDto(1, "1111", List.of(1));
        Card card = cardConverter.convertTo(dto);
        assertThat(card.getId()).isEqualTo(dto.getId());
        assertThat(card.getNumber()).isEqualTo(dto.getNumber());
        assertThat(card.getAccounts().get(0).getId()).isEqualTo(dto.getAccountIds().get(0));
    }

    @Test
    public void convertFromEntityToDto() {
        Account account = new Account();
        account.setId(1);
        Card card = new Card(1, "1111", List.of(account));
        CardDto cardDto = cardConverter.convertFrom(card);
        assertThat(cardDto.getId()).isEqualTo(card.getId());
        assertThat(cardDto.getNumber()).isEqualTo(card.getNumber());
        assertThat(cardDto.getAccountIds().get(0)).isEqualTo(card.getAccounts().get(0).getId());
    }
}
