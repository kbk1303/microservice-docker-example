package local.lyngberg.microservice.docker.login.interfaceweb.api;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AssignRoleRequest(
        @NotBlank
        @Size(min = 2, max = 50)
        String role
) {}