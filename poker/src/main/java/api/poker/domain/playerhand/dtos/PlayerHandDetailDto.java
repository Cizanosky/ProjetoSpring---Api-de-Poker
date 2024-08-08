package api.poker.domain.playerhand.dtos;

import api.poker.domain.card.dtos.CardDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerHandDetailDto {
    private UUID idPlayerHand;
    private CardDetailDto card1;
    private CardDetailDto card2;
}
