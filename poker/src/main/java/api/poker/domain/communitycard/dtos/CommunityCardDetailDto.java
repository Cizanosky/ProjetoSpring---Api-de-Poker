package api.poker.domain.communitycard.dtos;

import api.poker.domain.card.dtos.CardDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommunityCardDetailDto {

    private UUID idCommunityCard;
    private CardDetailDto card;
    private boolean visivel;

}
