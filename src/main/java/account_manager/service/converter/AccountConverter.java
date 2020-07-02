package account_manager.service.converter;

import account_manager.repository.account.AccountEntity;
import account_manager.repository.account.AccountType;
import account_manager.repository.client.ClientEntity;
import account_manager.repository.currency.CurrencyEntity;
import account_manager.service.dto.AccountDto;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class AccountConverter implements Converter<AccountEntity, AccountDto> {
    private final DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;

    @Override
    public AccountEntity convertTo(AccountDto dto) {
        ClientEntity clientEntity = createClient(dto.getOwnerId());
        CurrencyEntity currencyEntity = createCurrency(dto.getCurrencyCode());
        return createAccount(dto.getId(), dto.getNumber(), currencyEntity,
                dto.getType(), dto.getBalance(), dto.getOpenDate(), clientEntity);
    }

    private ClientEntity createClient(Integer ownerId) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(ownerId);
        return clientEntity;
    }

    private CurrencyEntity createCurrency(String currencyCode) {
        CurrencyEntity currencyEntity = new CurrencyEntity();
        currencyEntity.setCode(currencyCode);
        return currencyEntity;
    }

    private AccountEntity createAccount(Integer id, String number, CurrencyEntity currencyEntity,
                                        String accountType, Double balance, String openDate, ClientEntity clientEntity) {
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setId(id);
        accountEntity.setNumber(number);
        accountEntity.setCurrency(currencyEntity);
        accountEntity.setType(AccountType.valueOf(accountType));
        accountEntity.setBalance(balance != null ? balance : 0.0);
        Instant date = openDate != null
                ? LocalDateTime.parse(openDate, formatter).atZone(ZoneOffset.UTC).toInstant()
                : null;
        accountEntity.setOpenDate(date);
        accountEntity.setClient(clientEntity);
        return accountEntity;
    }

    @Override
    public AccountDto convertFrom(AccountEntity accountEntity) {
        return new AccountDto(accountEntity.getId(), accountEntity.getNumber(),
                accountEntity.getCurrency().getCode(), accountEntity.getType().name(), accountEntity.getBalance(),
                formatter.withZone(ZoneOffset.UTC).format(accountEntity.getOpenDate()), accountEntity.getClient().getId());
    }
}
