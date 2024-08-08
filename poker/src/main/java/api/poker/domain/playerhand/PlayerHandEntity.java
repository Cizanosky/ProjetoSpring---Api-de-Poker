package api.poker.domain.playerhand;

import api.poker.domain.card.CardEntity;
import api.poker.domain.player.PlayerEntity;
import api.poker.domain.round.RoundEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "PlayerHand")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "TABLE_PLAYER_HANDS")
public class PlayerHandEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_mao_jogador")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "id_rodada", nullable = false)
    private RoundEntity round;

    @ManyToOne
    @JoinColumn(name = "id_jogador", nullable = false)
    private PlayerEntity player;

    @ManyToOne
    @JoinColumn(name = "id_carta1", nullable = false)
    private CardEntity card1;

    @ManyToOne
    @JoinColumn(name = "id_carta2", nullable = false)
    private CardEntity card2;

    @Column(name = "dt_criacao")
    private LocalDateTime createdDate;
}
