package account_manager.web.controllers;


import account_manager.card.Card;
import account_manager.card.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/card")
public class CardController {
    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    @ResponseBody
    public Card getCardById(@RequestParam Integer cardId) {
        return cardService.getById(cardId);
    }

    @PostMapping
    @ResponseBody
    public String createCard(@RequestBody Card card) {
        return cardService.create(card);
    }

    @DeleteMapping
    @ResponseBody
    public String deleteCard(@RequestParam Integer cardId) {
        return cardService.deleteById(cardId);
    }
}
