package api.poker.domain.player.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerDetailDto {
    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private BigDecimal fichas;
    private LocalDateTime dt_criacao;
}
