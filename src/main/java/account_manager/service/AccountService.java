package account_manager.service;

import account_manager.repository.account.AccountEntity;
import account_manager.repository.account.AccountRepository;
import account_manager.repository.card.CardEntity;
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
    private final Converter<AccountEntity, AccountDto> converter;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountValidator validator, CardService cardService, Converter<AccountEntity, AccountDto> converter) {
        this.accountRepository = accountRepository;
        this.validator = validator;
        this.cardService = cardService;
        this.converter = converter;
    }

    @Transactional
    public AccountDto getById(Integer accountId) {
        AccountEntity account = accountRepository.findById(accountId).orElse(null);
        validator.validateGet(account);
        return converter.convertFrom(account);
    }

    public String create(AccountDto accountDto) {
        AccountEntity account = converter.convertTo(accountDto);
        account.setCards(cardService.getByAccountId(account.getId()));
        validator.validateCreate(account);
        AccountEntity accountEntityCreated = accountRepository.save(account);
        log.info("Created account #{}", accountEntityCreated.getId());
        return "Created account #" + accountEntityCreated.getId();
    }

    public String update(AccountDto accountDto) {
        AccountEntity account = converter.convertTo(accountDto);
        account.setCards(cardService.getByAccountId(account.getId()));
        validator.validateUpdate(account);
        accountRepository.update(account);
        log.info("Updated account #{}", account.getId());
        return "Account updated successfully";
    }

    public String deleteById(Integer accountId) {
        Set<CardEntity> cards = cardService.getByAccountId(accountId);
        for (CardEntity card : cards) {
            List<AccountEntity> cardAccountEntities = card.getAccounts();
            if (cardAccountEntities.size() == 1 && cardAccountEntities.get(0).getId().equals(accountId)) {
                cardService.deleteById(card.getId());
            }
        }
        accountRepository.deleteById(accountId);
        log.info("Deleted account #{}", accountId);
        return "Deleted account #" + accountId;
    }

    public List<AccountDto> getByClientId(Integer clientId) {
        validator.validateGetByClientId(clientId);
        List<AccountEntity> allByClientId = accountRepository.findAllByClientId(clientId);
        return allByClientId.stream().map(converter::convertFrom).collect(Collectors.toList());
    }
}
