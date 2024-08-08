package api.poker.domain.bet.validation;

import api.poker.application.exceptions.InvalidBetValueException;
import api.poker.domain.bet.BetEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PositiveBetValidator implements BetValidator{
    @Override
    public void validate(BetEntity betEntity) {
        if (betEntity.getValor().compareTo(BigDecimal.ZERO) < 0 || betEntity.getValor().compareTo(BigDecimal.ZERO) == 0) {
            throw new InvalidBetValueException("Bet value cannot be equal or less than zero.");
        }
    }
}
