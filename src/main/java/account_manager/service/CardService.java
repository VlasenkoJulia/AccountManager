package account_manager.service;

import account_manager.service.validator.CardValidator;
import account_manager.repository.account.Account;
import account_manager.repository.client.Client;
import account_manager.repository.currency.Currency;
import account_manager.repository.card.Card;
import account_manager.repository.card.CardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CardService {
    private static Logger log = LoggerFactory.getLogger(CardService.class.getName());
    private final CardValidator validator;
    private final CardRepository cardRepository;

    @Autowired
    public CardService(CardValidator validator, CardRepository cardRepository) {
        this.validator = validator;
        this.cardRepository = cardRepository;
    }

    @Transactional
    public Card getById(Integer cardId) {
        Card card = cardRepository.findById(cardId).orElse(null);
        validator.validateGet(card);
        ArrayList<Account> accounts = new ArrayList<>();
        for (Account acc : card.getAccounts()) {
            Account account = new Account();
            account.setId(acc.getId());
            account.setNumber(acc.getNumber());
            Currency currency = new Currency(
                    acc.getCurrency().getCode(),
                    acc.getCurrency().getRate(),
                    acc.getCurrency().getName(),
                    acc.getCurrency().getIso()
            );
            account.setCurrency(currency);
            Client client = new Client(
                    acc.getClient().getId(),
                    acc.getClient().getLastName(),
                    acc.getClient().getFirstName()
            );
            account.setClient(client);
            account.setType(acc.getType());
            account.setBalance(acc.getBalance());
            account.setOpenDate(acc.getOpenDate());
            accounts.add(account);
        }
        return new Card(card.getId(), card.getNumber(), accounts);
    }

    public String create(Card card) {
        validator.validateCreate(card);
        cardRepository.save(card);
        log.info("Created card #{}", card.getNumber());
        return "Created card #" + card.getNumber();
    }

    public String deleteById(Integer cardId) {
        cardRepository.deleteById(cardId);
        log.info("Deleted card #{}", cardId);
        return "Deleted card #" + cardId;
    }

    public List<Card> getByAccountId(Integer accountId) {
        return cardRepository.findByAccountId(accountId);
    }
}
