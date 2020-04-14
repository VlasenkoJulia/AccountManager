package account_manager.service;

import account_manager.repository.card.Card;
import account_manager.repository.card.CardRepository;
import account_manager.service.converter.Converter;
import account_manager.service.dto.CardDto;
import account_manager.service.validator.CardValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
public class CardService {
    private static Logger log = LoggerFactory.getLogger(CardService.class.getName());
    private final CardValidator validator;
    private final CardRepository cardRepository;
    private final Converter<Card, CardDto> converter;

    @Autowired
    public CardService(CardValidator validator, CardRepository cardRepository, Converter<Card, CardDto> converter) {
        this.validator = validator;
        this.cardRepository = cardRepository;
        this.converter = converter;
    }

    @Transactional
    public CardDto getById(Integer cardId) {
        Card card = cardRepository.findById(cardId).orElse(null);
        validator.validateGet(card);
        return converter.convertFrom(card);
    }

    public String create(CardDto cardDto) {
        Card card = converter.convertTo(cardDto);
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

    public Set<Card> getByAccountId(Integer accountId) {
        return cardRepository.findByAccountId(accountId);
    }
}
