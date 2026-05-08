package ch.leon.troller.M295_pokefolio_backend.collection;

import ch.leon.troller.M295_pokefolio_backend.base.MessageResponse;
import ch.leon.troller.M295_pokefolio_backend.security.Roles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Collection", description = "Verwaltung von Sammlungen. Benutzer sehen nur ihre eigenen Sammlungen, Admins alle.")
public class CollectionController {

    private final CollectionService collectionService;

    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    private String username(Jwt jwt) {
        return jwt.getClaim("preferred_username");
    }

    @Operation(summary = "Eigene Sammlungen abrufen", description = "Gibt alle Sammlungen des eingeloggten Benutzers zurück.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste der eigenen Sammlungen",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Collection.class))),
            @ApiResponse(responseCode = "401", description = "Nicht authentifiziert", content = @Content),
            @ApiResponse(responseCode = "403", description = "Keine Berechtigung", content = @Content)
    })
    @GetMapping
    @RolesAllowed(Roles.Read)
    public ResponseEntity<List<Collection>> getMyCollections(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(collectionService.getMyCollections(username(jwt)), HttpStatus.OK);
    }

    @Operation(summary = "Sammlung nach ID abrufen", description = "Gibt eine eigene Sammlung anhand der ID zurück.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sammlung gefunden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Collection.class))),
            @ApiResponse(responseCode = "403", description = "Nicht deine Sammlung", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sammlung nicht gefunden", content = @Content)
    })
    @GetMapping("/{id}")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<Collection> one(
            @Parameter(description = "ID der Sammlung", required = true, example = "1")
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(collectionService.getCollection(id, username(jwt)), HttpStatus.OK);
    }

    @Operation(summary = "Beliebige Sammlung abrufen (Admin)", description = "Gibt eine beliebige Sammlung anhand der ID zurück. Nur für Admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sammlung gefunden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Collection.class))),
            @ApiResponse(responseCode = "403", description = "Nur Admins erlaubt", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sammlung nicht gefunden", content = @Content)
    })
    @GetMapping("/admin/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<Collection> oneAdmin(
            @Parameter(description = "ID der Sammlung", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(collectionService.getCollectionAdmin(id), HttpStatus.OK);
    }

    @Operation(summary = "Neue Sammlung erstellen", description = "Erstellt eine neue Sammlung für den eingeloggten Benutzer.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sammlung erfolgreich erstellt",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Collection.class))),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten", content = @Content),
            @ApiResponse(responseCode = "401", description = "Nicht authentifiziert", content = @Content)
    })
    @PostMapping
    @RolesAllowed(Roles.Read)
    public ResponseEntity<Collection> create(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "Sammlungsdaten", required = true)
            @RequestBody Collection collection) {
        return new ResponseEntity<>(collectionService.createCollection(username(jwt), collection), HttpStatus.CREATED);
    }

    @Operation(summary = "Eigene Sammlung aktualisieren", description = "Aktualisiert den Namen einer eigenen Sammlung.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sammlung aktualisiert",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Collection.class))),
            @ApiResponse(responseCode = "403", description = "Nicht deine Sammlung", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sammlung nicht gefunden", content = @Content)
    })
    @PutMapping("/{id}")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<Collection> updateCollection(
            @Parameter(description = "Aktualisierte Sammlungsdaten", required = true)
            @Valid @RequestBody Collection collection,
            @Parameter(description = "ID der Sammlung", required = true, example = "1")
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(collectionService.updateCollection(collection, id, username(jwt)), HttpStatus.OK);
    }

    @Operation(summary = "Beliebige Sammlung aktualisieren (Admin)", description = "Aktualisiert eine beliebige Sammlung. Nur für Admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sammlung aktualisiert",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Collection.class))),
            @ApiResponse(responseCode = "403", description = "Nur Admins erlaubt", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sammlung nicht gefunden", content = @Content)
    })
    @PutMapping("/admin/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<Collection> updateCollectionAdmin(
            @Parameter(description = "Aktualisierte Sammlungsdaten", required = true)
            @Valid @RequestBody Collection collection,
            @Parameter(description = "ID der Sammlung", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(collectionService.updateCollectionAdmin(collection, id), HttpStatus.OK);
    }

    @Operation(summary = "Eigene Sammlung löschen", description = "Löscht eine eigene Sammlung und alle dazugehörigen CardInstances.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sammlung gelöscht",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Nicht deine Sammlung", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sammlung nicht gefunden", content = @Content)
    })
    @DeleteMapping("/{id}")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<MessageResponse> deleteCollection(
            @AuthenticationPrincipal Jwt jwt,
            @Parameter(description = "ID der zu löschenden Sammlung", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(collectionService.deleteCollection(id, username(jwt)));
    }

    @Operation(summary = "Beliebige Sammlung löschen (Admin)", description = "Löscht eine beliebige Sammlung. Nur für Admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sammlung gelöscht",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Nur Admins erlaubt", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sammlung nicht gefunden", content = @Content)
    })
    @DeleteMapping("/admin/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<MessageResponse> deleteCollectionAdmin(
            @Parameter(description = "ID der zu löschenden Sammlung", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(collectionService.deleteCollectionAdmin(id));
    }
}