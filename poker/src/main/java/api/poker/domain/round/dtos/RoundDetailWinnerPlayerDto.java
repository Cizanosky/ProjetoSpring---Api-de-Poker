package api.poker.domain.round.dtos;

import api.poker.domain.player.dtos.PlayerDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundDetailWinnerPlayerDto {

    private PlayerDetailDto playerWinner;
    private BigDecimal earnedValue;
    private PlayerDetailDto playerLoser;

}
