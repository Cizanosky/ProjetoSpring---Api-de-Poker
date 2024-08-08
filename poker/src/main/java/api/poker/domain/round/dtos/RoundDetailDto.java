package api.poker.domain.round.dtos;

import api.poker.domain.match.dtos.MatchDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoundDetailDto {

    private UUID roundId;
    private MatchDetailDto match;
    private BigDecimal potValue;
    private LocalDateTime createdDate;
    private String status;
}
