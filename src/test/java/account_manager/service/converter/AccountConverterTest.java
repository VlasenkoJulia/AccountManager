package account_manager.service.converter;

import account_manager.repository.account.Account;
import account_manager.repository.account.AccountType;
import account_manager.repository.client.Client;
import account_manager.repository.currency.Currency;
import account_manager.service.dto.AccountDto;
import org.junit.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountConverterTest {
    private final static DateTimeFormatter FORMATTER = DateTimeFormatter.RFC_1123_DATE_TIME;
    private final static String OPEN_DATE_STRING = "Wed, 1 Jan 2020 01:00:00 GMT";
    private final static Instant OPEN_DATE_INSTANT = LocalDateTime.parse(OPEN_DATE_STRING, FORMATTER).atZone(ZoneOffset.UTC).toInstant();

    private AccountConverter accountConverter = new AccountConverter();

    @Test
    public void convertToEntityFromDto() {
        AccountDto dto = new AccountDto(1, "123456789", "980",
                "CURRENT", 0.0, OPEN_DATE_STRING, 1);
        Account account = accountConverter.convertTo(dto);
        assertThat(account.getId()).isEqualTo(dto.getId());
        assertThat(account.getNumber()).isEqualTo(dto.getNumber());
        assertThat(account.getCurrency().getCode()).isEqualTo(dto.getCurrencyCode());
        assertThat(account.getType()).isEqualTo(AccountType.valueOf(dto.getType()));
        assertThat(account.getBalance()).isEqualTo(dto.getBalance());
        assertThat(account.getOpenDate()).isEqualTo(OPEN_DATE_INSTANT);
        assertThat(account.getClient().getId()).isEqualTo(dto.getOwnerId());
    }

    @Test
    public void convertToEntityFromDto_NullBalanceInDto() {
        AccountDto dto = new AccountDto(1, "123456789", "980",
                "CURRENT", null, OPEN_DATE_STRING, 1);
        Account account = accountConverter.convertTo(dto);
        assertThat(account.getId()).isEqualTo(dto.getId());
        assertThat(account.getNumber()).isEqualTo(dto.getNumber());
        assertThat(account.getCurrency().getCode()).isEqualTo(dto.getCurrencyCode());
        assertThat(account.getType()).isEqualTo(AccountType.valueOf(dto.getType()));
        assertThat(account.getBalance()).isEqualTo(0.0);
        assertThat(account.getOpenDate()).isEqualTo(OPEN_DATE_INSTANT);
        assertThat(account.getClient().getId()).isEqualTo(dto.getOwnerId());
    }

    @Test
    public void convertToEntityFromDto_NullOpenDateInDto() {
        AccountDto dto = new AccountDto(1, "123456789", "980",
                "CURRENT", 0.0, null, 1);
        Account account = accountConverter.convertTo(dto);
        assertThat(account.getId()).isEqualTo(dto.getId());
        assertThat(account.getNumber()).isEqualTo(dto.getNumber());
        assertThat(account.getCurrency().getCode()).isEqualTo(dto.getCurrencyCode());
        assertThat(account.getType()).isEqualTo(AccountType.valueOf(dto.getType()));
        assertThat(account.getBalance()).isEqualTo(dto.getBalance());
        assertThat(account.getOpenDate()).isNull();
        assertThat(account.getClient().getId()).isEqualTo(dto.getOwnerId());
    }

    @Test
    public void convertFromEntityToDto() {
        Currency currency = new Currency();
        currency.setCode("980");
        Client client = new Client();
        client.setId(1);
        Account account = new Account(1, "123456789", currency,
                AccountType.CURRENT, 0.0, OPEN_DATE_INSTANT, client, null);
        AccountDto accountDto = accountConverter.convertFrom(account);
        assertThat(accountDto.getId()).isEqualTo(account.getId());
        assertThat(accountDto.getNumber()).isEqualTo(account.getNumber());
        assertThat(accountDto.getCurrencyCode()).isEqualTo(account.getCurrency().getCode());
        assertThat(accountDto.getType()).isEqualTo(account.getType().name());
        assertThat(accountDto.getBalance()).isEqualTo(account.getBalance());
        assertThat(accountDto.getOpenDate()).isEqualTo(OPEN_DATE_STRING);
        assertThat(accountDto.getOwnerId()).isEqualTo(account.getClient().getId());
    }
}
