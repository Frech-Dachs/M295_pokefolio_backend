package ch.leon.troller.M295_pokefolio_backend.cardinstance;

import ch.leon.troller.M295_pokefolio_backend.base.MessageResponse;
import ch.leon.troller.M295_pokefolio_backend.security.Roles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("api/cardinstance")
@Tag(name = "CardInstance")
public class CardInstanceController {

    private final CardInstanceService cardInstanceService;

    public CardInstanceController(CardInstanceService cardInstanceService) {
        this.cardInstanceService = cardInstanceService;
    }

    private String username(Jwt jwt) {
        return jwt.getClaim("preferred_username");
    }

    @GetMapping("/collection/{collectionId}")
    @RolesAllowed(Roles.Read)
    @Tag(name = "CardInstance", description = "Get all CardInstances in a Collection")
    public ResponseEntity<List<CardInstance>> getByCollection(@PathVariable Long collectionId) {
        return new ResponseEntity<>(cardInstanceService.getByCollection(collectionId), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @RolesAllowed(Roles.Read)
    @Tag(name = "CardInstance", description = "Get CardInstance by ID")
    public ResponseEntity<CardInstance> getById(@PathVariable Long id) {
        return new ResponseEntity<>(cardInstanceService.getById(id), HttpStatus.OK);
    }

    @PostMapping("/collection/{collectionId}/card/{cardId}")
    @RolesAllowed(Roles.Read)
    @Tag(name = "CardInstance", description = "Add a Card to a Collection as CardInstance")
    public ResponseEntity<CardInstance> addToCollection(
            @PathVariable Long collectionId,
            @PathVariable Long cardId,
            @RequestBody CardInstance cardInstance,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(
                cardInstanceService.addToCollection(cardId, collectionId, cardInstance, username(jwt)),
                HttpStatus.CREATED);
    }

    @PostMapping("/admin/collection/{collectionId}/card/{cardId}")
    @RolesAllowed(Roles.Admin)
    @Tag(name = "CardInstance", description = "Add a Card to any Collection as CardInstance (Admin)")
    public ResponseEntity<CardInstance> addToCollectionAdmin(
            @PathVariable Long collectionId,
            @PathVariable Long cardId,
            @RequestBody CardInstance cardInstance) {
        return new ResponseEntity<>(
                cardInstanceService.addToCollectionAdmin(cardId, collectionId, cardInstance),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @RolesAllowed(Roles.Read)
    @Tag(name = "CardInstance", description = "Update a CardInstance (condition, quantity)")
    public ResponseEntity<CardInstance> update(
            @PathVariable Long id,
            @RequestBody CardInstance cardInstance,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(
                cardInstanceService.updateCardInstance(id, cardInstance, username(jwt)),
                HttpStatus.OK);
    }

    @PutMapping("/admin/{id}")
    @RolesAllowed(Roles.Admin)
    @Tag(name = "CardInstance", description = "Update any CardInstance (Admin)")
    public ResponseEntity<CardInstance> updateAdmin(
            @PathVariable Long id,
            @RequestBody CardInstance cardInstance) {
        return new ResponseEntity<>(
                cardInstanceService.updateCardInstanceAdmin(id, cardInstance),
                HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed(Roles.Read)
    @Tag(name = "CardInstance", description = "Remove CardInstance from Collection")
    public ResponseEntity<MessageResponse> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(cardInstanceService.deleteCardInstance(id, username(jwt)));
    }

    @DeleteMapping("/admin/{id}")
    @RolesAllowed(Roles.Admin)
    @Tag(name = "CardInstance", description = "Remove any CardInstance (Admin)")
    public ResponseEntity<MessageResponse> deleteAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(cardInstanceService.deleteCardInstanceAdmin(id));
    }
}