package ch.leon.troller.M295_pokefolio_backend.card;

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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("api/card")
@Tag(name = "Card", description = "Verwaltung von Pokémon-Karten-Typen (Templates). Nur Admins können Karten erstellen/ändern/löschen.")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @Operation(summary = "Alle Karten abrufen", description = "Gibt alle verfügbaren Karten alphabetisch sortiert zurück.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste aller Karten",
                    content = @Content(schema = @Schema(implementation = Card.class))),
            @ApiResponse(responseCode = "401", description = "Nicht authentifiziert", content = @Content),
            @ApiResponse(responseCode = "403", description = "Keine Berechtigung", content = @Content)
    })
    @GetMapping
    @RolesAllowed(Roles.Read)
    public ResponseEntity<List<Card>> all() {
        return new ResponseEntity<>(cardService.getCards(), HttpStatus.OK);
    }

    @Operation(summary = "Karte nach ID abrufen", description = "Gibt eine einzelne Karte anhand der ID zurück.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Karte gefunden",
                    content = @Content(schema = @Schema(implementation = Card.class))),
            @ApiResponse(responseCode = "404", description = "Karte nicht gefunden", content = @Content),
            @ApiResponse(responseCode = "401", description = "Nicht authentifiziert", content = @Content)
    })
    @GetMapping("/{id}")
    @RolesAllowed(Roles.Read)
    public ResponseEntity<Card> one(
            @Parameter(description = "ID der gesuchten Karte", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(cardService.getCard(id), HttpStatus.OK);
    }

    @Operation(summary = "Neue Karte erstellen", description = "Erstellt eine neue Karte (nur Admin).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Karte erfolgreich erstellt",
                    content = @Content(schema = @Schema(implementation = Card.class))),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten", content = @Content),
            @ApiResponse(responseCode = "403", description = "Nur Admins erlaubt", content = @Content)
    })
    @PostMapping
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<Card> create(
            @Parameter(description = "Kartendaten", required = true)
            @Valid @RequestBody Card card) {
        return new ResponseEntity<>(cardService.insertCard(card), HttpStatus.OK);
    }

    @Operation(summary = "Karte aktualisieren", description = "Aktualisiert eine bestehende Karte anhand der ID (nur Admin).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Karte aktualisiert",
                    content = @Content(schema = @Schema(implementation = Card.class))),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten", content = @Content),
            @ApiResponse(responseCode = "404", description = "Karte nicht gefunden", content = @Content),
            @ApiResponse(responseCode = "403", description = "Nur Admins erlaubt", content = @Content)
    })
    @PutMapping("/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<Card> update(
            @Parameter(description = "Aktualisierte Kartendaten", required = true)
            @Valid @RequestBody Card card,
            @Parameter(description = "ID der zu aktualisierenden Karte", required = true, example = "1")
            @PathVariable Long id) {
        return new ResponseEntity<>(cardService.updateCard(card, id), HttpStatus.OK);
    }

    @Operation(summary = "Karte löschen", description = "Löscht eine Karte anhand der ID (nur Admin).")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Karte gelöscht",
                    content = @Content(schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Karte nicht gefunden", content = @Content),
            @ApiResponse(responseCode = "403", description = "Nur Admins erlaubt", content = @Content)
    })
    @DeleteMapping("/{id}")
    @RolesAllowed(Roles.Admin)
    public ResponseEntity<MessageResponse> delete(
            @Parameter(description = "ID der zu löschenden Karte", required = true, example = "1")
            @PathVariable Long id) {
        try {
            return ResponseEntity.ok(cardService.deleteCard(id));
        } catch (Throwable t) {
            return ResponseEntity.internalServerError().build();
        }
    }
}