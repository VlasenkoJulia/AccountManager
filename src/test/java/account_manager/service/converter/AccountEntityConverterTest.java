package account_manager.service.converter;

import account_manager.repository.account.AccountEntity;
import account_manager.repository.account.AccountType;
import account_manager.repository.client.ClientEntity;
import account_manager.repository.currency.CurrencyEntity;
import account_manager.service.dto.AccountDto;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountEntityConverterTest {
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;
    private final static String OPEN_DATE_STRING = "Wed, 1 Jan 2020 01:00:00 GMT";
    private final static Instant OPEN_DATE_INSTANT = LocalDateTime.parse(OPEN_DATE_STRING, FORMATTER).atZone(ZoneOffset.UTC).toInstant();

    private final AccountConverter accountConverter = new AccountConverter();

    @Test
    public void convertToEntityFromDto() {
        AccountDto dto = new AccountDto(1, "123456789", "980",
                "CURRENT", 0.0, OPEN_DATE_STRING, 1);
        AccountEntity accountEntity = accountConverter.convertTo(dto);
        assertThat(accountEntity.getId()).isEqualTo(dto.getId());
        assertThat(accountEntity.getNumber()).isEqualTo(dto.getNumber());
        assertThat(accountEntity.getCurrency().getCode()).isEqualTo(dto.getCurrencyCode());
        assertThat(accountEntity.getType()).isEqualTo(AccountType.valueOf(dto.getType()));
        assertThat(accountEntity.getBalance()).isEqualTo(dto.getBalance());
        assertThat(accountEntity.getOpenDate()).isEqualTo(OPEN_DATE_INSTANT);
        assertThat(accountEntity.getClient().getId()).isEqualTo(dto.getOwnerId());
    }

    @Test
    public void convertToEntityFromDto_NullBalanceInDto() {
        AccountDto dto = new AccountDto(1, "123456789", "980",
                "CURRENT", null, OPEN_DATE_STRING, 1);
        AccountEntity accountEntity = accountConverter.convertTo(dto);
        assertThat(accountEntity.getId()).isEqualTo(dto.getId());
        assertThat(accountEntity.getNumber()).isEqualTo(dto.getNumber());
        assertThat(accountEntity.getCurrency().getCode()).isEqualTo(dto.getCurrencyCode());
        assertThat(accountEntity.getType()).isEqualTo(AccountType.valueOf(dto.getType()));
        assertThat(accountEntity.getBalance()).isEqualTo(0.0);
        assertThat(accountEntity.getOpenDate()).isEqualTo(OPEN_DATE_INSTANT);
        assertThat(accountEntity.getClient().getId()).isEqualTo(dto.getOwnerId());
    }

    @Test
    public void convertToEntityFromDto_NullOpenDateInDto() {
        AccountDto dto = new AccountDto(1, "123456789", "980",
                "CURRENT", 0.0, null, 1);
        AccountEntity accountEntity = accountConverter.convertTo(dto);
        assertThat(accountEntity.getId()).isEqualTo(dto.getId());
        assertThat(accountEntity.getNumber()).isEqualTo(dto.getNumber());
        assertThat(accountEntity.getCurrency().getCode()).isEqualTo(dto.getCurrencyCode());
        assertThat(accountEntity.getType()).isEqualTo(AccountType.valueOf(dto.getType()));
        assertThat(accountEntity.getBalance()).isEqualTo(dto.getBalance());
        assertThat(accountEntity.getOpenDate()).isNull();
        assertThat(accountEntity.getClient().getId()).isEqualTo(dto.getOwnerId());
    }

    @Test
    public void convertFromEntityToDto() {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setCode("980");
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(1);
        AccountEntity accountEntity = new AccountEntity(1, "123456789", currencyEntity,
                AccountType.CURRENT, 0.0, OPEN_DATE_INSTANT, clientEntity, null);
        AccountDto accountDto = accountConverter.convertFrom(accountEntity);
        assertThat(accountDto.getId()).isEqualTo(accountEntity.getId());
        assertThat(accountDto.getNumber()).isEqualTo(accountEntity.getNumber());
        assertThat(accountDto.getCurrencyCode()).isEqualTo(accountEntity.getCurrency().getCode());
        assertThat(accountDto.getType()).isEqualTo(accountEntity.getType().name());
        assertThat(accountDto.getBalance()).isEqualTo(accountEntity.getBalance());
        assertThat(accountDto.getOpenDate()).isEqualTo(OPEN_DATE_STRING);
        assertThat(accountDto.getOwnerId()).isEqualTo(accountEntity.getClient().getId());
    }
}
