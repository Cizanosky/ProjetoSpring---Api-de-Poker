package api.poker.domain.match.dtos;

import api.poker.domain.player.dtos.PlayerDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchPlayerWinnerDetailDto {

    private PlayerDetailDto player1;
    private PlayerDetailDto player2;
    private PlayerDetailDto playerWinner;
    private PlayerDetailDto playerLoser;

}
