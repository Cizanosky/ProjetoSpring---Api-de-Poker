package api.poker.domain.bet.validation;

import api.poker.application.exceptions.TurnBetException;
import api.poker.domain.bet.BetEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class IncreaseBetValidator implements BetValidator{
    @Override
    public void validate(BetEntity betEntity) {
        BigDecimal valorAtual = betEntity.getRound().getCurrentAmount();
        if (betEntity.getValor().compareTo(valorAtual) > 0) {
            if (betEntity.getValor().compareTo(valorAtual.multiply(BigDecimal.valueOf(2))) < 0) {
                throw new TurnBetException("If you want to increase the bet, it has to be at least double the opponent's: " + valorAtual);
            }
        }
    }
}
