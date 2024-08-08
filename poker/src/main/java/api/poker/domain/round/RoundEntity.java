package api.poker.domain.round;

import api.poker.domain.bet.BetEntity;
import api.poker.domain.communitycard.CommunityCardEntity;
import api.poker.domain.match.MatchEntity;
import api.poker.domain.player.PlayerEntity;
import api.poker.domain.playerhand.PlayerHandEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "Round")
@Table(name = "TABLE_ROUNDS")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class RoundEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_partida", nullable = false)
    private MatchEntity match;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jogador_vencedor")
    private PlayerEntity winPlayer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jogador_perdedor")
    private PlayerEntity loserPlayer;

    @Column(name = "valor_pote", nullable = false)
    private BigDecimal potValue;

    @Column(name = "dt_criacao", nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "current_player_id")
    private PlayerEntity currentPlayer;

    @Column(name = "current_amount")
    private BigDecimal currentAmount;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PlayerHandEntity> playerHands;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CommunityCardEntity> communityCards;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BetEntity> bets;
}
