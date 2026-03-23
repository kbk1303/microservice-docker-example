package local.lyngberg.microservice.docker.login.application;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import local.lyngberg.microservice.docker.login.interfaceadapters.persistence.jpa.UserEntity;
import local.lyngberg.microservice.docker.login.interfaceadapters.persistence.jpa.UserJpaRepository;
import local.lyngberg.microservice.docker.login.interfaceadapters.persistence.jpa.UserSessionEntity;
import local.lyngberg.microservice.docker.login.interfaceadapters.persistence.jpa.UserSessionJpaRepository;
import local.lyngberg.microservice.docker.login.interfaceweb.api.LoginResponse;
import local.lyngberg.microservice.docker.login.interfaceweb.api.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LoginManagementService {

    private final UserJpaRepository userJpaRepository;
    private final UserSessionJpaRepository userSessionJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final Clock clock;

    public LoginManagementService(UserJpaRepository userJpaRepository,
                                  UserSessionJpaRepository userSessionJpaRepository,
                                  PasswordEncoder passwordEncoder,
                                  Clock clock) {
        this.userJpaRepository = userJpaRepository;
        this.userSessionJpaRepository = userSessionJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.clock = clock;
    }

    public UUID createUser(String username, String email, String rawPassword) {
        if (userJpaRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userJpaRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        UserEntity user = new UserEntity(username, email, passwordEncoder.encode(rawPassword));
        user.assignRole("USER");

        UserEntity saved = userJpaRepository.save(user);
        return saved.getId();
    }

    public void assignRole(UUID userId, String role) {
        UserEntity user = userJpaRepository.findDetailedById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.assignRole(role.toUpperCase());
        userJpaRepository.save(user);
    }

    public LoginResponse login(String username, String rawPassword) {
        UserEntity user = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!Boolean.TRUE.equals(user.getActive())) {
            throw new IllegalArgumentException("User is inactive");
        }

        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        String sessionToken = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now(clock).plusHours(8);

        user.openSession(sessionToken, expiresAt);
        userJpaRepository.save(user);

        return new LoginResponse(user.getId(), user.getUsername(), sessionToken, expiresAt);
    }

    public void logout(String sessionToken) {
        UserSessionEntity session = userSessionJpaRepository.findBySessionToken(sessionToken)
                .orElseThrow(() -> new IllegalArgumentException("Session not found"));

        UserEntity user = session.getUser();
        user.closeSessionByToken(sessionToken);
        userJpaRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(UUID userId) {
        UserEntity user = userJpaRepository.findDetailedById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<String> roles = user.getUserRoles().stream()
                .map(role -> role.getRole())
                .sorted()
                .toList();

        List<UserResponse.UserSessionResponse> sessions = user.getUserSessions().stream()
                .sorted(Comparator.comparing(UserSessionEntity::getCreatedAt, Comparator.nullsLast(Comparator.naturalOrder())).reversed())
                .map(session -> new UserResponse.UserSessionResponse(
                        session.getId(),
                        session.getSessionToken(),
                        session.getExpiresAt(),
                        session.getCreatedAt(),
                        session.isExpired()
                ))
                .toList();

        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                Boolean.TRUE.equals(user.getActive()),
                roles,
                sessions
        );
    }
}
