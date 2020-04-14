package account_manager.service.converter;

import account_manager.repository.account.Account;
import account_manager.repository.account.AccountType;
import account_manager.repository.client.Client;
import account_manager.repository.currency.Currency;
import account_manager.service.dto.AccountDto;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Service
public class AccountConverter implements Converter<Account, AccountDto> {
    private DateTimeFormatter formatter = DateTimeFormatter.RFC_1123_DATE_TIME;

    @Override
    public Account convertTo(AccountDto dto) {
        Client client = createClient(dto.getOwnerId());
        Currency currency = createCurrency(dto.getCurrencyCode());
        return createAccount(dto.getId(), dto.getNumber(), currency,
                dto.getType(), dto.getBalance(), dto.getOpenDate(), client);
    }

    private Client createClient(Integer ownerId) {
        Client client = new Client();
        client.setId(ownerId);
        return client;
    }

    private Currency createCurrency(String currencyCode) {
        Currency currency = new Currency();
        currency.setCode(currencyCode);
        return currency;
    }

    private Account createAccount(Integer id, String number, Currency currency,
                                  String accountType, Double balance, String openDate, Client client) {
        Account account = new Account();
        account.setId(id);
        account.setNumber(number);
        account.setCurrency(currency);
        account.setType(AccountType.valueOf(accountType));
        account.setBalance(balance != null ? balance : 0.0);
        Instant date = openDate != null
                ? LocalDateTime.parse(openDate, formatter).atZone(ZoneOffset.UTC).toInstant()
                : null;
        account.setOpenDate(date);
        account.setClient(client);
        return account;
    }

    @Override
    public AccountDto convertFrom(Account account) {
        return new AccountDto(account.getId(), account.getNumber(),
                account.getCurrency().getCode(), account.getType().name(), account.getBalance(),
                formatter.withZone(ZoneOffset.UTC).format(account.getOpenDate()), account.getClient().getId());
    }
}
