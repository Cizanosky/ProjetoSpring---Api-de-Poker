package api.poker.domain.bet.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BetCreationDto {

    private UUID playerId;
    private UUID roundId;
    private BigDecimal valor;

}
