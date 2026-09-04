package ch.leon.troller.M295_pokefolio_backend.user;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@Tag(name = "User", description = "Benutzerverwaltung. Eigenes Profil abrufen oder alle Benutzer einsehen (Admin).")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Eigenes Profil abrufen", description = "Gibt den eingeloggten Benutzer zurück. Erstellt ihn automatisch, falls er noch nicht in der Datenbank existiert.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Benutzer gefunden oder erstellt",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "401", description = "Nicht authentifiziert", content = @Content),
            @ApiResponse(responseCode = "403", description = "Keine Berechtigung", content = @Content)
    })
    @GetMapping("api/me")
    @RolesAllowed(Roles.Read)
    public User getMe(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaim("preferred_username");
        return userService.getOrCreateUser(username);
    }

    @Operation(summary = "Alle Benutzer abrufen (Admin)", description = "Gibt eine Liste aller registrierten Benutzer zurück. Nur für Admins.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste aller Benutzer",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "403", description = "Nur Admins erlaubt", content = @Content),
            @ApiResponse(responseCode = "401", description = "Nicht authentifiziert", content = @Content)
    })
    @GetMapping("api/admin/users")
    @RolesAllowed(Roles.Admin)
    public List<User> getAllUsers() {
        return userService.getUsers();
    }

    @Operation(summary = "Benutzer nach ID abrufen (Admin)", description = "Gibt einen einzelnen Benutzer anhand der ID zurück.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Benutzer gefunden",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "404", description = "Benutzer nicht gefunden", content = @Content),
            @ApiResponse(responseCode = "403", description = "Nur Admins erlaubt", content = @Content)
    })
    @GetMapping("api/admin/users/{id}")
    @RolesAllowed(Roles.Admin)
    public User getUser(
            @Parameter(description = "ID des gesuchten Benutzers", required = true, example = "1")
            @PathVariable Long id) {
        return userService.getUser(id);
    }

    @Operation(summary = "Benutzer aktualisieren (Admin)", description = "Aktualisiert den Benutzernamen eines bestehenden Benutzers anhand der ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Benutzer aktualisiert",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "Ungültige Eingabedaten", content = @Content),
            @ApiResponse(responseCode = "404", description = "Benutzer nicht gefunden", content = @Content),
            @ApiResponse(responseCode = "403", description = "Nur Admins erlaubt", content = @Content)
    })
    @PutMapping("api/admin/users/{id}")
    @RolesAllowed(Roles.Admin)
    public User updateUser(
            @Parameter(description = "Aktualisierte Benutzerdaten", required = true)
            @Valid @RequestBody User user,
            @Parameter(description = "ID des zu aktualisierenden Benutzers", required = true, example = "1")
            @PathVariable Long id) {
        return userService.updateUser(id, user);
    }

    @Operation(summary = "Benutzer löschen (Admin)", description = "Löscht einen Benutzer sowie dessen Sammlungen anhand der ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Benutzer gelöscht",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageResponse.class))),
            @ApiResponse(responseCode = "404", description = "Benutzer nicht gefunden", content = @Content),
            @ApiResponse(responseCode = "403", description = "Nur Admins erlaubt", content = @Content)
    })
    @DeleteMapping("api/admin/users/{id}")
    @RolesAllowed(Roles.Admin)
    public MessageResponse deleteUser(
            @Parameter(description = "ID des zu löschenden Benutzers", required = true, example = "1")
            @PathVariable Long id) {
        return userService.deleteUser(id);
    }
}