package api.poker.domain.bet.validation;

import api.poker.application.exceptions.TurnBetException;
import api.poker.domain.bet.BetEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class AllInBetValidator implements BetValidator{
    @Override
    public void validate(BetEntity betEntity) {
        BigDecimal valorAtual = betEntity.getRound().getCurrentAmount();
        if (betEntity.getValor().compareTo(valorAtual) != 0) {
            if (betEntity.getPlayer().equals(betEntity.getRound().getMatch().getPlayer1())) {
                if (betEntity.getRound().getMatch().getPlayer2().getFichas().compareTo(BigDecimal.ZERO) == 0) {
                    throw new TurnBetException("Your opponent has gone All-In! Cover the " + valorAtual + " bet or fold.");
                }
            } else {
                if (betEntity.getRound().getMatch().getPlayer1().getFichas().compareTo(BigDecimal.ZERO) == 0) {
                    throw new TurnBetException("Your opponent has gone All-In! Cover the " + valorAtual + " bet or fold.");
                }
            }
        }
    }
}
