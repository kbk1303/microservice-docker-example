package local.lyngberg.microservice.docker.login.interfaceweb.api;


import jakarta.validation.constraints.NotBlank;

public record LogoutRequest(
        @NotBlank String sessionToken
) {}