package local.lyngberg.microservice.docker.login.interfaceweb;
import java.net.URI;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import jakarta.validation.Valid;
import local.lyngberg.microservice.docker.login.application.LoginManagementService;
import local.lyngberg.microservice.docker.login.interfaceweb.api.AssignRoleRequest;
import local.lyngberg.microservice.docker.login.interfaceweb.api.CreateUserRequest;
import local.lyngberg.microservice.docker.login.interfaceweb.api.LoginRequest;
import local.lyngberg.microservice.docker.login.interfaceweb.api.LoginResponse;
import local.lyngberg.microservice.docker.login.interfaceweb.api.LogoutRequest;
import local.lyngberg.microservice.docker.login.interfaceweb.api.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class LoginController {

    private final LoginManagementService loginManagementService;

    public LoginController(LoginManagementService loginManagementService) {
        this.loginManagementService = loginManagementService;
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, UUID>> createUser(@Valid @RequestBody CreateUserRequest request) {
        UUID userId = loginManagementService.createUser(
                request.username(),
                request.email(),
                request.password()
        );

        URI location = URI.create("/api/users/" + userId);
        return ResponseEntity
                .created(Objects.requireNonNull(location))
                .body(Map.of("userId", userId));
    }

    @PostMapping("/users/{userId}/roles")
    public ResponseEntity<Void> assignRole(@PathVariable("userId") UUID userId,
                                           @Valid @RequestBody AssignRoleRequest request) {
        loginManagementService.assignRole(userId, request.role());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(loginManagementService.getUser(userId));
    }

    @PostMapping("/sessions/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(loginManagementService.login(request.username(), request.password()));
    }

    @PostMapping("/sessions/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequest request) {
        loginManagementService.logout(request.sessionToken());
        return ResponseEntity.noContent().build();
    }
}
