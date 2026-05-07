package ch.leon.troller.M295_pokefolio_backend.user;

import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Gibt den eingeloggten User zurück (oder erstellt ihn)
    @GetMapping("api/me")
    public User getMe(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaim("preferred_username"); // Keycloak standard claim
        return userService.getOrCreateUser(username);
    }

    // Nur ADMIN darf alle User sehen
    @GetMapping("api/admin/users")
    @RolesAllowed()
    public List<User> getAllUsers() {
        return userService.userRepository.findAll();
    }
}
