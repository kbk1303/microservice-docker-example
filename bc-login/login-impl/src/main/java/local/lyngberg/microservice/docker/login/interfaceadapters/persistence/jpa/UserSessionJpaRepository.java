package local.lyngberg.microservice.docker.login.interfaceadapters.persistence.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSessionJpaRepository extends JpaRepository<UserSessionEntity, UUID> {

    Optional<UserSessionEntity> findBySessionToken(String sessionToken);

    boolean existsBySessionToken(String sessionToken);
}