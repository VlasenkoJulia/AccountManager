package account_manager.web.controllers;


import account_manager.card.Card;
import account_manager.card.CardRepository;
import account_manager.web.exception_handling.InputParameterValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardRepository cardRepository;

    @Autowired
    public CardController(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @GetMapping
    public Card getCardById(@RequestParam Integer cardId) {
        Card card = cardRepository.getById(cardId);
        if (card == null) {
            throw new InputParameterValidationException("Card with passed ID do not exist");
        }
        return card;
    }

    @PostMapping
    public String createCard(@RequestBody Card card) {
        if (card.getAccounts().isEmpty()) {
            throw new InputParameterValidationException("Card can not be created without reference to the account(s)");
        }
        cardRepository.create(card);
        return "Created card #" + card.getNumber();
    }

    @DeleteMapping
    public String deleteCard(@RequestParam("cardId") Integer id) {
        cardRepository.deleteById(id);
        return "Deleted card #" + id;
    }
}
