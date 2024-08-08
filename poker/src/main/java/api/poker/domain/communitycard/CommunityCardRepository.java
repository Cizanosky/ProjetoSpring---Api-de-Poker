package api.poker.domain.communitycard;

import api.poker.domain.round.RoundEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface CommunityCardRepository extends JpaRepository<CommunityCardEntity, UUID> {

    @Query("SELECT c FROM CommunityCard c WHERE c.round.id = :roundId AND c.visivel = true")
    List<CommunityCardEntity> findAllByRoundIdAndVisivelTrue(@Param("roundId") UUID roundId);

    @Query("SELECT c FROM CommunityCard c WHERE c.round = :round AND c.visivel = false ORDER BY c.id ASC LIMIT 1")
    CommunityCardEntity findFirstByRoundAndVisivelFalse(@Param("round") RoundEntity round);

    @Query("SELECT c FROM CommunityCard c WHERE c.round.id = :roundId")
    List<CommunityCardEntity> findAllByRound(@Param("roundId") UUID roundId);
}
