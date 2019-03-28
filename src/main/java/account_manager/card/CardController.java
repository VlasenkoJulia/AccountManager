package account_manager.card;


import account_manager.account.Account;
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
    public String getCardById(@RequestParam("cardId") Integer id) {
        Card card = cardRepository.getById(id);
        return gson.toJson(card);
    }

    @PostMapping
    public String createCard(@RequestBody String body) {
        Card card = gson.fromJson(body, Card.class);
        if (card.getAccountsId().isEmpty()) {
            throw new IllegalArgumentException("Card can not be created without reference to the account(s)");
        }
        cardRepository.create(card);
        return "Created card #" + card.getNumber();
    }

    @DeleteMapping
    public String deleteAccount(@RequestParam("cardId") Integer id) {
        cardRepository.deleteById(id);
        return "Deleted card #" + id;
    }


}
