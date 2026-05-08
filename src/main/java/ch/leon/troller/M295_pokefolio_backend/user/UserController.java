package ch.leon.troller.M295_pokefolio_backend.user;

import ch.leon.troller.M295_pokefolio_backend.security.Roles;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return userService.userRepository.findAll();
    }
}