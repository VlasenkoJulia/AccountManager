package account_manager.service;

import account_manager.service.validator.AccountValidator;
import account_manager.repository.entity.Card;
import account_manager.repository.entity.Client;
import account_manager.repository.entity.Currency;
import account_manager.repository.entity.Account;
import account_manager.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service

public class AccountService {
    private static Logger log = LoggerFactory.getLogger(AccountService.class.getName());

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
        log.info("Created account #{}", accountCreated.getId());
        return "Created account #" + accountCreated.getId();
    }

    public String update(Account account) {
        validator.validateUpdate(account);
        accountRepository.update(account);
        log.info("Updated account #{}", account.getId());
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
        log.info("Deleted account #{}", accountId);
        return "Deleted account #" + accountId;
    }

    public List<Account> getByClientId(Integer clientId) {
        validator.validateGetByClientId(clientId);
        return accountRepository.getByClientId(clientId);
    }
}
