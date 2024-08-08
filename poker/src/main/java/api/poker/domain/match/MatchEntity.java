package api.poker.domain.match;

import api.poker.domain.player.PlayerEntity;
import api.poker.domain.round.RoundEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity(name = "Match")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "TABLE_MATCHES")
public class MatchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String status;
    private LocalDateTime dt_criacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player1")
    private PlayerEntity player1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player2")
    private PlayerEntity player2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_winner")
    private PlayerEntity playerWinner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_loser")
    private PlayerEntity playerLoser;

    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoundEntity> rounds;

}
