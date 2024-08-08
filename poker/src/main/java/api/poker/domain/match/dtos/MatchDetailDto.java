package api.poker.domain.match.dtos;

import api.poker.domain.player.dtos.PlayerDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchDetailDto {

    private UUID idMatch;
    private String status;
    private LocalDateTime dt_criacao;
    private PlayerDetailDto player1;
    private PlayerDetailDto player2;

}
