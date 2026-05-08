package ch.leon.troller.M295_pokefolio_backend.cardinstance;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("api/cardinstance")
@Tag(name = "CardInstance", description = "Verwaltung von Karten-Instanzen in Sammlungen. Eine CardInstance verknüpft eine Karte (Typ) mit einer Sammlung.")
public class CardInstanceController {

    private final CardInstanceService cardInstanceService;

    public CardInstanceController(CardInstanceService cardInstanceService) {
        this.cardInstanceService = cardInstanceService;
    }

    private String username(Jwt jwt) {
        return jwt.getClaim("preferred_username");
    }

    @Operation(summary = "Alle CardInstances einer Sammlung abrufen", description = "Gibt alle Karten-Instanzen zurück, die einer bestimmten Sammlung gehören.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste der CardInstances",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardInstance.class))),
            @ApiResponse(responseCode = "401", description = "Nicht authentifiziert", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sammlung nicht gefunden", content = @Content)
    })
    @GetMapping("/collection/{collectionId}")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<List<CardInstance>> getByCollection(
            @Parameter(description = "ID der Sammlung", required = true, example = "1")
            @PathVariable Long collectionId) {
        return new ResponseEntity<>(cardInstanceService.getByCollection(collectionId), HttpStatus.OK);
    }

    @Operation(summary = "CardInstance nach ID abrufen", description = "Gibt eine einzelne Karten-Instanz anhand ihrer ID zurück.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CardInstance gefunden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardInstance.class))),
            @ApiResponse(responseCode = "404", description = "CardInstance nicht gefunden", content = @Content),
            @ApiResponse(responseCode = "401", description = "Nicht authentifiziert", content = @Content)
    })
    @GetMapping("/{id}")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<CardInstance> getById(
            @Parameter(description = "ID der CardInstance", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(cardInstanceService.getById(id), HttpStatus.OK);
    }

    @Operation(summary = "Karte zur eigenen Sammlung hinzufügen", description = "Erstellt eine neue CardInstance und fügt sie der angegebenen Sammlung des eingeloggten Benutzers hinzu.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CardInstance erfolgreich erstellt",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardInstance.class))),
            @ApiResponse(responseCode = "403", description = "Nicht deine Sammlung", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sammlung oder Karte nicht gefunden", content = @Content)
    })
    @PostMapping("/collection/{collectionId}/card/{cardId}")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<CardInstance> addToCollection(
            @Parameter(description = "ID der Sammlung", required = true, example = "1")
            @PathVariable Long collectionId,
            @Parameter(description = "ID der Karte (Typ)", required = true, example = "1")
            @PathVariable Long cardId,
            @Parameter(description = "CardInstance-Daten (condition, quantity)", required = true)
            @RequestBody CardInstance cardInstance,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(
                cardInstanceService.addToCollection(cardId, collectionId, cardInstance, username(jwt)),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Karte zu beliebiger Sammlung hinzufügen (Admin)", description = "Erstellt eine neue CardInstance in einer beliebigen Sammlung. Nur für Admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "CardInstance erfolgreich erstellt",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardInstance.class))),
            @ApiResponse(responseCode = "403", description = "Nur Admins erlaubt", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sammlung oder Karte nicht gefunden", content = @Content)
    })
    @PostMapping("/admin/collection/{collectionId}/card/{cardId}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<CardInstance> addToCollectionAdmin(
            @Parameter(description = "ID der Sammlung", required = true, example = "1")
            @PathVariable Long collectionId,
            @Parameter(description = "ID der Karte (Typ)", required = true, example = "1")
            @PathVariable Long cardId,
            @Parameter(description = "CardInstance-Daten (condition, quantity)", required = true)
            @RequestBody CardInstance cardInstance) {
        return new ResponseEntity<>(
                cardInstanceService.addToCollectionAdmin(cardId, collectionId, cardInstance),
                HttpStatus.CREATED);
    }

    @Operation(summary = "Eigene CardInstance aktualisieren", description = "Aktualisiert Zustand (condition) und Anzahl (quantity) einer eigenen CardInstance.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CardInstance aktualisiert",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardInstance.class))),
            @ApiResponse(responseCode = "403", description = "Nicht deine Sammlung", content = @Content),
            @ApiResponse(responseCode = "404", description = "CardInstance nicht gefunden", content = @Content)
    })
    @PutMapping("/{id}")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<CardInstance> update(
            @Parameter(description = "ID der CardInstance", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Aktualisierte Daten (condition, quantity)", required = true)
            @RequestBody CardInstance cardInstance,
            @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(
                cardInstanceService.updateCardInstance(id, cardInstance, username(jwt)),
                HttpStatus.OK);
    }

    @Operation(summary = "Beliebige CardInstance aktualisieren (Admin)", description = "Aktualisiert condition und quantity einer beliebigen CardInstance. Nur für Admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CardInstance aktualisiert",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CardInstance.class))),
            @ApiResponse(responseCode = "403", description = "Nur Admins erlaubt", content = @Content),
            @ApiResponse(responseCode = "404", description = "CardInstance nicht gefunden", content = @Content)
    })
    @PutMapping("/admin/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<CardInstance> updateAdmin(
            @Parameter(description = "ID der CardInstance", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Aktualisierte Daten (condition, quantity)", required = true)
            @RequestBody CardInstance cardInstance) {
        return new ResponseEntity<>(
                cardInstanceService.updateCardInstanceAdmin(id, cardInstance),
                HttpStatus.OK);
    }

    @Operation(summary = "Eigene CardInstance löschen", description = "Entfernt eine CardInstance aus der eigenen Sammlung.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CardInstance gelöscht",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Nicht deine Sammlung", content = @Content),
            @ApiResponse(responseCode = "404", description = "CardInstance nicht gefunden", content = @Content)
    })
    @DeleteMapping("/{id}")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<MessageResponse> delete(
            @Parameter(description = "ID der zu löschenden CardInstance", required = true, example = "1")
            @PathVariable Long id,
            @AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(cardInstanceService.deleteCardInstance(id, username(jwt)));
    }

    @Operation(summary = "Beliebige CardInstance löschen (Admin)", description = "Löscht eine beliebige CardInstance. Nur für Admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "CardInstance gelöscht",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "403", description = "Nur Admins erlaubt", content = @Content),
            @ApiResponse(responseCode = "404", description = "CardInstance nicht gefunden", content = @Content)
    })
    @DeleteMapping("/admin/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<MessageResponse> deleteAdmin(
            @Parameter(description = "ID der zu löschenden CardInstance", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(cardInstanceService.deleteCardInstanceAdmin(id));
    }
}