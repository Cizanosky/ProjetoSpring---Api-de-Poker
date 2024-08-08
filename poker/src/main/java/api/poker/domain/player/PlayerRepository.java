package api.poker.domain.player;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface PlayerRepository extends JpaRepository<PlayerEntity, UUID> {
    boolean existsByEmail(String email);
    UserDetails findByEmail(String email);
}
