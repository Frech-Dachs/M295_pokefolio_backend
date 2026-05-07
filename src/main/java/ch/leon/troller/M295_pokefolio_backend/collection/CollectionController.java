package ch.leon.troller.M295_pokefolio_backend.collection;

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
public class CollectionController {
    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @Tag(name = "Collection", description = "Get all Collections")
    @GetMapping("api/collection")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<List<Collection>> all() {
        List<Collection> result = collectionService.getCollections();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Tag(name = "Collection", description = "Get all Collections")
    @GetMapping("api/collection/{id}")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<Collection> one(@PathVariable Long id) {
        Collection collection = collectionService.getCollection(id);
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    @Tag(name = "Collection", description = "Get all Collections")
    @PostMapping("api/collection")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<Collection> newDepartment(@Valid @RequestBody Collection collection) {
        Collection savedCollection = collectionService.insertCollection(collection);
        return new ResponseEntity<>(collection, HttpStatus.OK);
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
        try {
            return ResponseEntity.ok(collectionService.deleteCollection(id));
        } catch (Throwable t) {
            return ResponseEntity.internalServerError().build();
        }
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
        Collection savedCollection = collectionService.addCard(cardId, id);
        return new ResponseEntity<>(savedCollection, HttpStatus.OK);
    }
}
