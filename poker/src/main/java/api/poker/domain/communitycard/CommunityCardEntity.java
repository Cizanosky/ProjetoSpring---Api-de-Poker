package api.poker.domain.communitycard;

import api.poker.domain.card.CardEntity;
import api.poker.domain.round.RoundEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "CommunityCard")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "TABLE_COMMUNITY_CARDS")
public class CommunityCardEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rodada", nullable = false)
    private RoundEntity round;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carta", nullable = false)
    private CardEntity card;

    @Column(name = "visivel", nullable = false)
    private boolean visivel;

}
