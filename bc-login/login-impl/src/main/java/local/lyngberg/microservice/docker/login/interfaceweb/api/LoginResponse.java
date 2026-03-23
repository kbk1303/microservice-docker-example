package local.lyngberg.microservice.docker.login.interfaceweb.api;

import java.time.LocalDateTime;
import java.util.UUID;

public record LoginResponse(
        UUID userId,
        String username,
        String sessionToken,
        LocalDateTime expiresAt
) {}