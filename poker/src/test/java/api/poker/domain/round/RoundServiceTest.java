package api.poker.domain.round;

import api.poker.application.exceptions.ResourceNotFoundException;
import api.poker.domain.card.CardEntity;
import api.poker.domain.card.CardService;
import api.poker.domain.communitycard.CommunityCardService;
import api.poker.domain.match.MatchEntity;
import api.poker.domain.player.PlayerEntity;
import api.poker.domain.playerhand.PlayerHandService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoundServiceTest {

    @Mock
    private RoundRepository roundRepository;

    @Mock
    private CardService cardService;

    @Mock
    private PlayerHandService playerHandService;

    @Mock
    private CommunityCardService communityCardService;

    @InjectMocks
    private RoundService roundService;

    private RoundEntity roundEntity;
    private MatchEntity matchEntity;
    private Stack<CardEntity> deck;

    @BeforeEach
    void setUp() {

        roundEntity = new RoundEntity();
        roundEntity.setId(UUID.randomUUID());
        roundEntity.setPotValue(BigDecimal.ZERO);
        roundEntity.setCreatedDate(LocalDateTime.now());

        matchEntity = new MatchEntity();
        matchEntity.setPlayer1(new PlayerEntity());
        matchEntity.setPlayer2(new PlayerEntity());

        roundEntity.setMatch(matchEntity);

        deck = new Stack<>();
        deck.push(new CardEntity());
        deck.push(new CardEntity());
        deck.push(new CardEntity());
        deck.push(new CardEntity());
    }

    @Test
    @DisplayName("Testa a criação de uma nova rodada")
    void testCreate() {
        when(cardService.getDeck()).thenReturn(deck);
        when(roundRepository.save(any(RoundEntity.class))).thenReturn(roundEntity);

        RoundEntity round = roundService.create(matchEntity);

        ArgumentCaptor<RoundEntity> roundCaptor = ArgumentCaptor.forClass(RoundEntity.class);
        ArgumentCaptor<Stack<CardEntity>> deckCaptor = ArgumentCaptor.forClass(Stack.class);

        verify(roundRepository).save(roundCaptor.capture());
        verify(playerHandService).createHandsForMatch(roundCaptor.capture(), deckCaptor.capture());
        verify(communityCardService).createCommunityCards(roundCaptor.capture(), deckCaptor.capture());

        assertEquals(roundEntity.getMatch(), round.getMatch());
        assertEquals(roundEntity.getPotValue(), round.getPotValue());

        assertEquals(deck, deckCaptor.getValue());

        assertEquals("Em andamento", round.getStatus());
        assertEquals(matchEntity, round.getMatch());
        assertEquals(BigDecimal.ZERO, round.getPotValue());
        assertEquals(matchEntity.getPlayer1(), round.getCurrentPlayer());
        assertEquals(BigDecimal.ZERO, round.getCurrentAmount());
    }

    @Test
    @DisplayName("Testa a obtenção de todas as rodadas")
    void testGetAll() {
        Pageable pageable = Pageable.unpaged();
        Page<RoundEntity> rounds = Page.empty();
        when(roundRepository.findAll(pageable)).thenReturn(rounds);

        Page<RoundEntity> result = roundService.getAll(pageable);

        assertEquals(rounds, result);
    }

    @Test
    @DisplayName("Testa a obtenção de uma rodada por ID")
    void testGetById() {
        UUID roundId = roundEntity.getId();
        when(roundRepository.findById(roundId)).thenReturn(Optional.of(roundEntity));

        RoundEntity foundRound = roundService.getById(roundId);

        assertEquals(roundEntity, foundRound);
    }

    @Test
    @DisplayName("Testa a obtenção de uma rodada por ID quando não encontrada")
    void testGetByIdNotFound() {
        UUID roundId = UUID.randomUUID();
        when(roundRepository.findById(roundId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roundService.getById(roundId));
    }

    @Test
    @DisplayName("Testa a obtenção do vencedor de uma rodada")
    void testGetPlayerWinnerByRound() {
        UUID roundId = roundEntity.getId();
        when(roundRepository.findPlayerWinner(roundId)).thenReturn(Optional.of(roundEntity));

        RoundEntity winnerRound = roundService.getPlayerWinnerByRound(roundId);

        assertEquals(roundEntity, winnerRound);
    }

    @Test
    @DisplayName("Testa a obtenção do vencedor de uma rodada quando não encontrado")
    void testGetPlayerWinnerByRoundNotFound() {
        UUID roundId = UUID.randomUUID();
        when(roundRepository.findPlayerWinner(roundId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> roundService.getPlayerWinnerByRound(roundId));
    }
}
