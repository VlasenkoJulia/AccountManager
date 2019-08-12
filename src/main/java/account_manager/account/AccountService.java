package account_manager.account;

import account_manager.card.Card;
import account_manager.card.CardService;
import account_manager.client.Client;
import account_manager.currency_converter.Currency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service

public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountValidator validator;
    private final CardService cardService;


    @Autowired
    public AccountService(AccountRepository accountRepository, AccountValidator validator, CardService cardService) {
        this.accountRepository = accountRepository;
        this.validator = validator;
        this.cardService = cardService;
    }

    @Transactional
    public Account getById(Integer accountId) {
        Account account = accountRepository.getById(accountId);
        validator.validateGet(account);
        Currency currency = new Currency(
                account.getCurrency().getCode(),
                account.getCurrency().getRate(),
                account.getCurrency().getName(),
                account.getCurrency().getIso()
        );
        Client client = new Client(
                account.getClient().getId(),
                account.getClient().getLastName(),
                account.getClient().getFirstName()
        );
        Set<Card> cards = new HashSet<>();
        for (Card card : account.getCards()) {
            cards.add(new Card(card.getId(), card.getNumber()));
        }
        return new Account(account.getId(), account.getNumber(), currency,
                account.getType(), account.getBalance(), account.getOpenDate(),
                client, cards);
    }

    public String create(Account account) {
        validator.validateCreate(account);
        Account accountCreated = accountRepository.create(account);
        return "Created account #" + accountCreated.getId();
    }

    public String update(Account account) {
        validator.validateUpdate(account);
        accountRepository.update(account);
        return "Account updated successfully";
    }

    public String deleteById(Integer accountId) {
        List<Card> cards = cardService.getByAccountId(accountId);
        for (Card card : cards) {
            List<Account> cardAccounts = card.getAccounts();
            if (cardAccounts.size() == 1 && cardAccounts.get(0).getId().equals(accountId)) {
                cardService.deleteById(card.getId());
            }
        }
        accountRepository.deleteById(accountId);
        return "Deleted account #" + accountId;
    }

    public List<Account> getByClientId(Integer clientId) {
        validator.validateGetByClientId(clientId);
        return accountRepository.getByClientId(clientId);
    }
}
