package account_manager.service;

import account_manager.repository.account.Account;
import account_manager.repository.account.AccountRepository;
import account_manager.repository.card.Card;
import account_manager.service.converter.Converter;
import account_manager.service.dto.AccountDto;
import account_manager.service.validator.AccountValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private static Logger log = LoggerFactory.getLogger(AccountService.class.getName());

    private final AccountRepository accountRepository;
    private final AccountValidator validator;
    private final CardService cardService;
    private final Converter<Account, AccountDto> converter;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountValidator validator, CardService cardService, Converter<Account, AccountDto> converter) {
        this.accountRepository = accountRepository;
        this.validator = validator;
        this.cardService = cardService;
        this.converter = converter;
    }

    @Transactional
    public AccountDto getById(Integer accountId) {
        Account account = accountRepository.findById(accountId).orElse(null);
        validator.validateGet(account);
        return converter.convertFrom(account);
    }

    public String create(AccountDto accountDto) {
        Account account = converter.convertTo(accountDto);
        account.setCards(cardService.getByAccountId(account.getId()));
        validator.validateCreate(account);
        Account accountCreated = accountRepository.save(account);
        log.info("Created account #{}", accountCreated.getId());
        return "Created account #" + accountCreated.getId();
    }

    public String update(AccountDto accountDto) {
        Account account = converter.convertTo(accountDto);
        account.setCards(cardService.getByAccountId(account.getId()));
        validator.validateUpdate(account);
        accountRepository.save(account);
        log.info("Updated account #{}", account.getId());
        return "Account updated successfully";
    }

    public String deleteById(Integer accountId) {
        Set<Card> cards = cardService.getByAccountId(accountId);
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

    public List<AccountDto> getByClientId(Integer clientId) {
        validator.validateGetByClientId(clientId);
        List<Account> allByClientId = accountRepository.findAllByClientId(clientId);
        return allByClientId.stream().map(converter::convertFrom).collect(Collectors.toList());
    }
}
