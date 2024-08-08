package api.poker.domain.bet.validation;

import api.poker.application.exceptions.TurnBetException;
import api.poker.domain.bet.BetEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class SizeBetValidator implements BetValidator {
    @Override
    public void validate(BetEntity betEntity) {
        BigDecimal totalFichasPlayer = betEntity.getPlayer().getFichas();
        if (betEntity.getValor().compareTo(totalFichasPlayer) > 0) {
            throw new TurnBetException("You cannot bet more chips than you have, your amount of chips is: " + totalFichasPlayer);
        }
    }
}
