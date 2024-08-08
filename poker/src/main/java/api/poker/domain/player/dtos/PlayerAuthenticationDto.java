package api.poker.domain.player.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerAuthenticationDto {
    private String email;
    private String password;
}
