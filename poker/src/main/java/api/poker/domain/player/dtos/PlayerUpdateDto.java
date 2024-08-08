package api.poker.domain.player.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerUpdateDto {
    @NotNull
    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private BigDecimal fichas;
}
