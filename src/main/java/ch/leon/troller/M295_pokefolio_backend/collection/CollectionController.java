package ch.leon.troller.M295_pokefolio_backend.collection;

import ch.leon.troller.M295_pokefolio_backend.base.MessageResponse;
import ch.leon.troller.M295_pokefolio_backend.security.Roles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("api/collection")
@Tag(name = "Collection")
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    private String username(Jwt jwt) {
        return jwt.getClaim("preferred_username");
    }

    @GetMapping
    @RolesAllowed(Roles.Read)
    @Tag(name = "Collection", description = "Get my Collections")
    public ResponseEntity<List<Collection>> getMyCollections(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(collectionService.getMyCollections(username(jwt)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @RolesAllowed(Roles.Read)
    @Tag(name = "Collection", description = "Get Collection by ID")
    public ResponseEntity<Collection> one(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(collectionService.getCollection(id, username(jwt)), HttpStatus.OK);
    }

    @GetMapping("/admin/{id}")
    @RolesAllowed(Roles.Admin)
    @Tag(name = "Collection", description = "Get any Collection by ID (Admin)")
    public ResponseEntity<Collection> oneAdmin(@PathVariable Long id) {
        return new ResponseEntity<>(collectionService.getCollectionAdmin(id), HttpStatus.OK);
    }

    @PostMapping
    @RolesAllowed(Roles.Read)
    @Tag(name = "Collection", description = "Create Collection")
    public ResponseEntity<Collection> create(@AuthenticationPrincipal Jwt jwt, @RequestBody Collection collection) {
        return new ResponseEntity<>(collectionService.createCollection(username(jwt), collection), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @RolesAllowed(Roles.Read)
    @Tag(name = "Collection", description = "Update Collection")
    public ResponseEntity<Collection> updateCollection(@Valid @RequestBody Collection collection, @PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(collectionService.updateCollection(collection, id, username(jwt)), HttpStatus.OK);
    }

    @PutMapping("/admin/{id}")
    @RolesAllowed(Roles.Admin)
    @Tag(name = "Collection", description = "Update any Collection (Admin)")
    public ResponseEntity<Collection> updateCollectionAdmin(@Valid @RequestBody Collection collection, @PathVariable Long id) {
        return new ResponseEntity<>(collectionService.updateCollectionAdmin(collection, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed(Roles.Read)
    @Tag(name = "Collection", description = "Delete Collection")
    public ResponseEntity<MessageResponse> deleteCollection(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return ResponseEntity.ok(collectionService.deleteCollection(id, username(jwt)));
    }

    @DeleteMapping("/admin/{id}")
    @RolesAllowed(Roles.Admin)
    @Tag(name = "Collection", description = "Delete any Collection (Admin)")
    public ResponseEntity<MessageResponse> deleteCollectionAdmin(@PathVariable Long id) {
        return ResponseEntity.ok(collectionService.deleteCollectionAdmin(id));
    }

    @PutMapping("/addCard/{id}")
    @RolesAllowed(Roles.Read)
    @Tag(name = "Collection", description = "Add Card to Collection")
    public ResponseEntity<Collection> addCard(@RequestBody Long cardId, @PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(collectionService.addCard(cardId, id, username(jwt)), HttpStatus.OK);
    }

    @PutMapping("/admin/addCard/{id}")
    @RolesAllowed(Roles.Admin)
    @Tag(name = "Collection", description = "Add Card to any Collection (Admin)")
    public ResponseEntity<Collection> addCardAdmin(@RequestBody Long cardId, @PathVariable Long id) {
        return new ResponseEntity<>(collectionService.addCardAdmin(cardId, id), HttpStatus.OK);
    }

    @PutMapping("/removeCard/{id}")
    @RolesAllowed(Roles.Read)
    @Tag(name = "Collection", description = "Remove Card from Collection")
    public ResponseEntity<Collection> removeCard(@RequestBody Long cardId, @PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(collectionService.removeCard(cardId, id, username(jwt)), HttpStatus.OK);
    }

    @PutMapping("/admin/removeCard/{id}")
    @RolesAllowed(Roles.Admin)
    @Tag(name = "Collection", description = "Remove Card from any Collection (Admin)")
    public ResponseEntity<Collection> removeCardAdmin(@RequestBody Long cardId, @PathVariable Long id) {
        return new ResponseEntity<>(collectionService.removeCardAdmin(cardId, id), HttpStatus.OK);
    }
}