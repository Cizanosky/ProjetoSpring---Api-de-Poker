package api.poker.domain.bet.validation;

import api.poker.application.exceptions.TurnBetException;
import api.poker.domain.bet.BetEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class MinimumBetValidator implements BetValidator{
    @Override
    public void validate(BetEntity betEntity) {
        BigDecimal valorAtual = betEntity.getRound().getCurrentAmount();
        if (betEntity.getValor().compareTo(valorAtual) < 0 && betEntity.getValor().compareTo(BigDecimal.ZERO) > 0) {
            throw new TurnBetException("You have to cover your opponent's bet: " + valorAtual);
        }
    }
}
