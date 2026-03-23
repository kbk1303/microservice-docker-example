package local.lyngberg.microservice.docker.login.interfaceweb.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String email,
        boolean active,
        List<String> roles,
        List<UserSessionResponse> sessions
) {
    public record UserSessionResponse(
            UUID id,
            String sessionToken,
            LocalDateTime expiresAt,
            LocalDateTime createdAt,
            boolean expired
    ) {}
}