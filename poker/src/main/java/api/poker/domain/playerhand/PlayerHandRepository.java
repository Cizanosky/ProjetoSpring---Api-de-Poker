package api.poker.domain.playerhand;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface PlayerHandRepository extends JpaRepository<PlayerHandEntity, UUID> {
    @Query("SELECT ph FROM PlayerHand ph WHERE ph.player.id = :playerId AND ph.round.id = :roundId")
    Optional<PlayerHandEntity> findByPlayerIdAndRoundId(@Param("playerId") UUID playerId, @Param("roundId") UUID roundId);
}
