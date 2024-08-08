package api.poker.domain.card.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDetailDto {

    private String idCard;
    private String rank;
    private String suit;

}
