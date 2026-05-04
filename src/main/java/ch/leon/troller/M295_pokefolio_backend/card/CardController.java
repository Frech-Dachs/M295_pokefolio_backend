package ch.leon.troller.M295_pokefolio_backend.card;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Tag(name = "Card", description = "Get all Cards")
    @GetMapping("api/card")
    public ResponseEntity<List<Card>> all() {
        List<Card> result = cardService.getCards();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Tag(name = "Card", description = "Get all Cards")
    @GetMapping("api/card/{id}")
    public ResponseEntity<Card> one(@PathVariable Long id) {
        Card card = cardService.getCard(id);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }


}
