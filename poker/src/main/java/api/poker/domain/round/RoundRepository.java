package api.poker.domain.round;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoundRepository extends JpaRepository<RoundEntity, UUID> {
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN TRUE ELSE FALSE END FROM Round r WHERE r.id = :id AND r.status = 'Em andamento'")
    boolean findByStatusEmAndamento(@Param("id") UUID id);

    @Query("SELECT r FROM Round r WHERE r.id = :id")
    Optional<RoundEntity> findPlayerWinner(@Param("id") UUID id);

}
