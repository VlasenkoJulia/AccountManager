package account_manager.web.controllers;


import account_manager.card.Card;
import account_manager.card.CardRepository;
import account_manager.web.exception_handling.InputParameterValidationException;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardController {

    private final CardRepository cardRepository;
    private final Gson gson;

    @Autowired
    public CardController(CardRepository cardRepository, Gson gson) {
        this.cardRepository = cardRepository;
        this.gson = gson;
    }

    @GetMapping
    public String getCardById(@RequestParam Integer cardId) throws InputParameterValidationException {
        Card card = cardRepository.getById(cardId);
        if (card == null) {
            throw new InputParameterValidationException("Card with passed ID do not exist");
        }
        return gson.toJson(card);
    }

    @PostMapping
    public String createCard(@RequestBody String body) throws InputParameterValidationException {
        Card card = gson.fromJson(body, Card.class);
        if (card.getAccountsId().isEmpty()) {
            throw new InputParameterValidationException("Card can not be created without reference to the account(s)");
        }
        cardRepository.create(card);
        return "Created card #" + card.getNumber();
    }

    @DeleteMapping
    public String deleteAccount(@RequestParam("cardId") Integer id) throws InputParameterValidationException {
        cardRepository.deleteById(id);
        return "Deleted card #" + id;
    }


}
