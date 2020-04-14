package account_manager.web.controller;


import account_manager.service.CardService;
import account_manager.service.dto.CardDto;
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
    public CardDto getCardById(@RequestParam Integer cardId) {
        return cardService.getById(cardId);
    }

    @PostMapping
    @ResponseBody
    public String createCard(@RequestBody CardDto card) {
        return cardService.create(card);
    }

    @DeleteMapping
    @ResponseBody
    public String deleteCard(@RequestParam Integer cardId) {
        return cardService.deleteById(cardId);
    }
}
