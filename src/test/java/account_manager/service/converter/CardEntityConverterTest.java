package account_manager.service.converter;

import account_manager.repository.account.AccountEntity;
import account_manager.repository.card.CardEntity;
import account_manager.service.dto.CardDto;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CardEntityConverterTest {
    private final CardConverter cardConverter = new CardConverter();

    @Test
    public void convertToEntityFromDto() {
        CardDto dto = new CardDto(1, "1111", List.of(1));
        CardEntity cardEntity = cardConverter.convertTo(dto);
        assertThat(cardEntity.getId()).isEqualTo(dto.getId());
        assertThat(cardEntity.getNumber()).isEqualTo(dto.getNumber());
        assertThat(cardEntity.getAccounts().get(0).getId()).isEqualTo(dto.getAccountIds().get(0));
    }

    @Test
    public void convertFromEntityToDto() {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(1);
        CardEntity cardEntity = new CardEntity(1, "1111", List.of(accountEntity));
        CardDto cardDto = cardConverter.convertFrom(cardEntity);
        assertThat(cardDto.getId()).isEqualTo(cardEntity.getId());
        assertThat(cardDto.getNumber()).isEqualTo(cardEntity.getNumber());
        assertThat(cardDto.getAccountIds().get(0)).isEqualTo(cardEntity.getAccounts().get(0).getId());
    }
}
