package api.poker.domain.player.dtos;

import api.poker.domain.player.PlayerRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerRegisterDto {
        @NotBlank
        private String nome;
        @NotBlank
        @Email
        private String email;
        @NotBlank
        private String senha;
        @NotNull
        private BigDecimal fichas;
        private LocalDateTime dt_criacao;
}
