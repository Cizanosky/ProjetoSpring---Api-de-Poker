package api.poker.domain.match.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchUpdateDto {

    @NotNull
    private UUID id;
    private UUID player1;
    private UUID player2;

}
