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
    @ResponseBody
    public Card getCardById(@RequestParam Integer cardId) {
        Card card = cardRepository.getById(cardId);
        if (card == null) {
            throw new InputParameterValidationException("Card with passed ID do not exist");
        }
        return card;
    }

    @PostMapping
    @ResponseBody
    public String createCard(@RequestBody Card card) {
        if (card.getId() != null) {
            throw new InputParameterValidationException("Can not provide insert operation with passed card");
        }
        if (card.getAccounts().isEmpty()) {
            throw new InputParameterValidationException("Card can not be created without reference to the account(s)");
        }
        cardRepository.create(card);
        return "Created card #" + card.getNumber();
    }

    @DeleteMapping
    @ResponseBody
    public String deleteCard(@RequestParam Integer cardId) {
        cardRepository.deleteById(cardId);
        return "Deleted card #" + cardId;
    }
}
