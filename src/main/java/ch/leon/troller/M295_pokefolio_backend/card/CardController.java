package ch.leon.troller.M295_pokefolio_backend.card;

import ch.leon.troller.M295_pokefolio_backend.base.MessageResponse;
import ch.leon.troller.M295_pokefolio_backend.security.Roles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
public class CardController {
    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Tag(name = "Card", description = "Get all Collections")
    @GetMapping("api/card")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<List<Card>> all() {
        List<Card> result = cardService.getCards();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Tag(name = "Card", description = "Get all Cards")
    @GetMapping("api/card/{id}")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<Card> one(@PathVariable Long id) {
        Card card = cardService.getCard(id);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @Tag(name = "Card", description = "Get all Cards")
    @PostMapping("api/card")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<Card> newDepartment(@Valid @RequestBody Card card) {
        Card savedCard = cardService.insertCard(card);
        return new ResponseEntity<>(card, HttpStatus.OK);
    }

    @Tag(name = "Card", description = "Get all Cards")
    @PutMapping("api/card/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<Card> updateCard(@Valid @RequestBody Card card, @PathVariable Long id) {
        Card savedCard = cardService.updateCard(card, id);
        return new ResponseEntity<>(savedCard, HttpStatus.OK);
    }

    @Tag(name = "Card", description = "Get all Cards")
    @DeleteMapping("api/card/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<MessageResponse> deleteCard(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(cardService.deleteCard(id));
        } catch (Throwable t) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
