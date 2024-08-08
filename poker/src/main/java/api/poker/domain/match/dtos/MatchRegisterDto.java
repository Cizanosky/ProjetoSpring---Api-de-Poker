package api.poker.domain.match.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatchRegisterDto {

    private LocalDateTime dt_criacao;
    @NotNull
    private UUID player1;
    @NotNull
    private UUID player2;

}
