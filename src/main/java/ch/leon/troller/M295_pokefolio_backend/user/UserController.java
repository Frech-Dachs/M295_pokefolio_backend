package ch.leon.troller.M295_pokefolio_backend.user;

import ch.leon.troller.M295_pokefolio_backend.security.Roles;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("api/me")
    @RolesAllowed(Roles.Read)
    public User getMe(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaim("preferred_username"); // Keycloak standard claim
        return userService.getOrCreateUser(username);
    }

    @GetMapping("api/admin/users")
    @RolesAllowed(Roles.Admin)
    public List<User> getAllUsers() {
        return userService.userRepository.findAll();
    }
}
