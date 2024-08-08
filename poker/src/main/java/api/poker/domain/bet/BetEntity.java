package api.poker.domain.bet;

import api.poker.domain.player.PlayerEntity;
import api.poker.domain.round.RoundEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "Bet")
@Table(name = "TABLE_BETS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class BetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rodada", nullable = false)
    private RoundEntity round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jogador", nullable = false)
    private PlayerEntity player;

    @Column(name = "valor", nullable = false)
    private BigDecimal valor;

    @Column(name = "dt_criacao", nullable = false)
    private LocalDateTime dataCriacao;
}
