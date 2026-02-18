package local.lyngberg.microservice.docker.auditlog.config.security;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

@Configuration
public class SecurityConfig {
    
    Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    UserDetailsService internalX509UserDetailsService(InternalMtlsProps props) {
        Set<String> allowed = props.allowedCallers() == null ? Set.of() : Set.copyOf(props.allowedCallers());

        return username -> {
        if (!allowed.contains(username)) {
            throw new UsernameNotFoundException("CN not allowed: " + username);
        }
        logger.info("username found: "+username);
        // Password bruges ikke i X509 pre-auth, men User kræver et felt.
        return User.withUsername(username)
            .password("N/A")
            .authorities("ROLE_INTERNAL")
            .build();
        };
    }

    @Bean
    @Order(1)
    SecurityFilterChain internal(HttpSecurity http, InternalMtlsProps props, UserDetailsService internalX509UserDetailsService) throws Exception {
        Set<String> allowed = new HashSet<>(props.allowedCallers());
        AuthorizationManager<RequestAuthorizationContext> onlyAllowedCallers =
            (authentication, context) -> {
                var a = authentication.get();
                if(a == null || !a.isAuthenticated()) return new AuthorizationDecision(false);
                return new AuthorizationDecision(allowed.contains(a.getName()));
            };
        
        http
            .securityMatcher("/internal/**")
            .csrf(csrf-> csrf.disable())
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth.anyRequest().access(onlyAllowedCallers))
            // extract CN from cert subject
            .x509(x -> x.subjectPrincipalRegex("CN=(.*?)(?:,|$)"))
            .userDetailsService(internalX509UserDetailsService);

        return http.build();
    }

    @Bean
    @Order(2)
    SecurityFilterChain actuator(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/actuator/health", "/actuator/info").permitAll()
            .anyRequest().denyAll()
        );
        return http.build();
    }

}
