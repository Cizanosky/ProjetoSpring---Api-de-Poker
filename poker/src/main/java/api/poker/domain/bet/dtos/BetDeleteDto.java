package api.poker.domain.bet.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BetDeleteDto {

    private UUID betId;
    private UUID playerId;
}
