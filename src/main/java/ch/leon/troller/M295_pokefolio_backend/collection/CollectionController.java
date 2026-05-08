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
public class CollectionController {

    // Keine überprüfung nötig für Role.ADMIN


    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @Tag(name = "Collection", description = "Get all Collections")
    @GetMapping("api/collection")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<List<Collection>> getMyCollections(@AuthenticationPrincipal Jwt jwt) {
        List<Collection> result = collectionService.getMyCollections(jwt.getClaim("preferred_username"));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Tag(name = "Collection", description = "Get Collection by ID")
    @GetMapping("api/collection/{id}")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<Collection> one(@PathVariable Long id, @AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaim("preferred_username");
        Collection collection = collectionService.getCollection(id, username);
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Tag(name = "Collection", description = "Get all Collections")
    @PostMapping("api/collection")
    @RolesAllowed(Roles.Read)
    public Collection create(@AuthenticationPrincipal Jwt jwt, @RequestBody Collection collection) {
        String username = jwt.getClaim("preferred_username");
        return collectionService.createCollection(username, collection);
    }

    @Tag(name = "Collection", description = "Get all Collections")
    @PutMapping("api/collection/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<Collection> updateCollection(@Valid @RequestBody Collection collection, @PathVariable Long id) {
        Collection savedCollection = collectionService.updateCollection(collection, id);
        return new ResponseEntity<>(savedCollection, HttpStatus.OK);
    }

    @Tag(name = "Collection", description = "Get all Collections")
    @DeleteMapping("api/collection/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<MessageResponse> deleteCollection(@PathVariable Long id) {
        return ResponseEntity.ok(collectionService.deleteCollection(id));
    }
    @Tag(name = "Collection", description = "Get all Collections")
    @PutMapping("api/collection/addCard/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<Collection> addNewCard(@Valid @RequestBody Long cardId, @PathVariable Long id) {
        Collection savedCollection = collectionService.addCard(cardId, id);
        return new ResponseEntity<>(savedCollection, HttpStatus.OK);
    }

    @Tag(name = "Collection", description = "Get all Collections")
    @PutMapping("api/collection/removeCard/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<Collection> rmCard(@Valid @RequestBody Long cardId, @PathVariable Long id) {
        Collection savedCollection = collectionService.removeCard(cardId, id);
        return new ResponseEntity<>(savedCollection, HttpStatus.OK);
    }
}
