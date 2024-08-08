package api.poker.domain.match;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface MatchRepository extends JpaRepository<MatchEntity, UUID> {
    @Query("SELECT m FROM Match m WHERE m.status = 'Em andamento'")
    Page<MatchEntity> findAllByStatusEmAndamento(Pageable pageable);

    @Query("SELECT m FROM Match m WHERE m.status = 'Finalizado'")
    Page<MatchEntity> findAllByStatusFinalizado(Pageable pageable);

    @Query("SELECT CASE WHEN m.status = 'Criado' THEN true ELSE false END FROM Match m WHERE m.id = :id")
    boolean findStatusCriadoById(@Param("id") UUID id);

    @Query("SELECT CASE WHEN m.status = 'Em andamento' THEN true ELSE false END FROM Match m WHERE m.id = :id")
    boolean findByStatusEmAndamento(UUID id);

    @Query("SELECT m FROM Match m WHERE m.status = 'Finalizado' and m.id = :id")
    Optional<MatchEntity> findMatchByIdFinalizado(UUID id);
}
