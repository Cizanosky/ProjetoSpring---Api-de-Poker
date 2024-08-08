package api.poker.domain.bet.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BetCheckDetailDto {

    private UUID idPlayer;
    private String nomePlayer;
    private String acao = "Check";
    private String statusRound;

}
