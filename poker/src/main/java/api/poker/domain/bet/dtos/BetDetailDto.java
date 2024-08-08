package api.poker.domain.bet.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BetDetailDto {

    private UUID idBet;
    private BigDecimal valor;
    private String nomePlayer;
    private String acao = "Bet";
    private String statusRound;
}
