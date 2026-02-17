package local.lyngberg.microservice.docker.login.config.security;


import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "internal.mtls")
public record InternalMtlsProps(List<String> allowedCallers) {

}
