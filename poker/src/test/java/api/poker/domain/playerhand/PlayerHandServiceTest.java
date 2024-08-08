package api.poker.domain.playerhand;

import api.poker.application.exceptions.ResourceNotFoundException;
import api.poker.domain.card.CardEntity;
import api.poker.domain.match.MatchEntity;
import api.poker.domain.player.PlayerEntity;
import api.poker.domain.round.RoundEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Stack;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerHandServiceTest {

    @Mock
    private PlayerHandRepository playerHandRepository;

    @InjectMocks
    private PlayerHandService playerHandService;

    @Test
    @DisplayName("Deve criar mãos para a partida e salvar no repositório")
    public void deveCriarMaosParaPartida() {
        RoundEntity roundEntity = new RoundEntity();
        PlayerEntity player1 = new PlayerEntity();
        PlayerEntity player2 = new PlayerEntity();
        roundEntity.setMatch(new MatchEntity());
        roundEntity.getMatch().setPlayer1(player1);
        roundEntity.getMatch().setPlayer2(player2);
        Stack<CardEntity> deck = new Stack<>();
        deck.push(new CardEntity());
        deck.push(new CardEntity());
        deck.push(new CardEntity());
        deck.push(new CardEntity());

        playerHandService.createHandsForMatch(roundEntity, deck);

        verify(playerHandRepository, times(2)).save(any(PlayerHandEntity.class));
    }

    @Test
    @DisplayName("Deve retornar a mão do jogador pelo ID do jogador e ID da rodada")
    public void deveRetornarMaosPorIdDoJogadorEIdDaRodada() {
        UUID playerId = UUID.randomUUID();
        UUID roundId = UUID.randomUUID();
        PlayerHandEntity expectedHand = new PlayerHandEntity();
        expectedHand.setPlayer(new PlayerEntity());
        expectedHand.setRound(new RoundEntity());

        when(playerHandRepository.findByPlayerIdAndRoundId(playerId, roundId))
                .thenReturn(Optional.of(expectedHand));

        PlayerHandEntity actualHand = playerHandService.getByPlayerIdAndRoundId(playerId, roundId);

        assertEquals(expectedHand, actualHand);
    }

    @Test
    @DisplayName("Deve lançar exceção quando a mão do jogador não for encontrada")
    public void deveLancarExcecaoQuandoMaosNaoForEncontrada() {
        UUID playerId = UUID.randomUUID();
        UUID roundId = UUID.randomUUID();

        when(playerHandRepository.findByPlayerIdAndRoundId(playerId, roundId))
                .thenReturn(Optional.empty());

        try {
            playerHandService.getByPlayerIdAndRoundId(playerId, roundId);
        } catch (ResourceNotFoundException e) {
            assertEquals("Player hand not found for player with id " + playerId, e.getMessage());
        }
    }
}
