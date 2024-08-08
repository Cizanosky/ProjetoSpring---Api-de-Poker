package api.poker.domain.bet.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BetFoldDetailDto {

    private UUID idPlayer;
    private String nomePlayer;
    private String acao = "Fold";
    private String statusRound;

}
