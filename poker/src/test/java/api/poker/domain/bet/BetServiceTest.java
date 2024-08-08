package api.poker.domain.bet;

import api.poker.application.exceptions.ResourceNotFoundException;
import api.poker.application.exceptions.TurnBetException;
import api.poker.domain.bet.dtos.BetCreationDto;
import api.poker.domain.bet.dtos.BetDeleteDto;
import api.poker.domain.bet.validation.BetValidator;
import api.poker.domain.match.MatchEntity;
import api.poker.domain.match.MatchService;
import api.poker.domain.player.PlayerEntity;
import api.poker.domain.player.PlayerRepository;
import api.poker.domain.player.PlayerService;
import api.poker.domain.playerhand.PlayerHandEntity;
import api.poker.domain.playerhand.PlayerHandService;
import api.poker.domain.round.RoundEntity;
import api.poker.domain.round.RoundRepository;
import api.poker.domain.round.RoundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BetServiceTest {

    @Mock
    private BetRepository betRepository;

    @Mock
    private RoundService roundService;

    @Mock
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private RoundRepository roundRepository;

    @Mock
    private MatchService matchService;

    @Mock
    private PlayerHandService playerHandService;

    @Mock
    private List<BetValidator> betValidators;

    @InjectMocks
    private BetService betService;

    private RoundEntity roundEntity;
    private PlayerEntity playerEntity;
    private BetCreationDto betCreationDto;
    private BetDeleteDto betDeleteDto;

    @BeforeEach
    void setUp() {

        playerEntity = new PlayerEntity();
        playerEntity.setId(UUID.randomUUID());
        playerEntity.setFichas(BigDecimal.valueOf(1000));

        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setPlayer1(playerEntity);

        roundEntity = new RoundEntity();
        roundEntity.setId(UUID.randomUUID());
        roundEntity.setCurrentAmount(BigDecimal.ZERO);
        roundEntity.setPotValue(BigDecimal.ZERO);
        roundEntity.setCurrentPlayer(playerEntity);
        roundEntity.setMatch(matchEntity);

        betCreationDto = new BetCreationDto();
        betCreationDto.setPlayerId(playerEntity.getId());
        betCreationDto.setRoundId(roundEntity.getId());
        betCreationDto.setValor(BigDecimal.valueOf(100));

        betDeleteDto = new BetDeleteDto();
        betDeleteDto.setBetId(UUID.randomUUID());
        betDeleteDto.setPlayerId(playerEntity.getId());

        PlayerHandEntity playerHand = new PlayerHandEntity();
        playerHand.setPlayer(playerEntity);
        Set<PlayerHandEntity> playerHands = new HashSet<>();
        playerHands.add(playerHand);
        roundEntity.setPlayerHands(playerHands);
    }

    @Test
    void testCreateBet() {
        when(roundService.getById(betCreationDto.getRoundId())).thenReturn(roundEntity);
        when(playerService.getById(betCreationDto.getPlayerId())).thenReturn(playerEntity);
        when(betRepository.save(any(BetEntity.class))).thenReturn(new BetEntity());
        when(roundRepository.findByStatusEmAndamento(roundEntity.getId())).thenReturn(true);

        BetEntity bet = betService.createBet(betCreationDto);

        assertNotNull(bet);
        verify(betRepository, times(1)).save(any(BetEntity.class));
    }

    @Test
    void testCreateBet_NotPlayerTurn() {
        roundEntity.setCurrentPlayer(new PlayerEntity());
        when(roundService.getById(betCreationDto.getRoundId())).thenReturn(roundEntity);
        when(playerService.getById(betCreationDto.getPlayerId())).thenReturn(playerEntity);
        when(roundRepository.findByStatusEmAndamento(roundEntity.getId())).thenReturn(true);

        assertThrows(TurnBetException.class, () -> betService.createBet(betCreationDto));
    }

    @Test
    void testDeleteBet() {
        BetEntity betEntity = new BetEntity();
        betEntity.setId(betDeleteDto.getBetId());

        when(betRepository.findById(betDeleteDto.getBetId())).thenReturn(Optional.of(betEntity));

        betService.deleteById(betDeleteDto);

        verify(betRepository, times(1)).delete(betEntity);
    }

    @Test
    void testDeleteBet_NotFound() {
        when(betRepository.findById(betDeleteDto.getBetId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> betService.deleteById(betDeleteDto));
    }

    @Test
    void testCheck() {
        when(roundService.getById(betCreationDto.getRoundId())).thenReturn(roundEntity);
        when(playerService.getById(betCreationDto.getPlayerId())).thenReturn(playerEntity);
        when(roundRepository.findByStatusEmAndamento(roundEntity.getId())).thenReturn(true);

        BetEntity bet = betService.check(playerEntity.getId(), roundEntity.getId());

        assertNotNull(bet);
    }

    @Test
    void testCheck_NotPlayerTurn() {
        roundEntity.setCurrentPlayer(new PlayerEntity());
        when(roundService.getById(betCreationDto.getRoundId())).thenReturn(roundEntity);
        when(playerService.getById(betCreationDto.getPlayerId())).thenReturn(playerEntity);
        when(roundRepository.findByStatusEmAndamento(roundEntity.getId())).thenReturn(true);

        assertThrows(TurnBetException.class, () -> betService.check(playerEntity.getId(), roundEntity.getId()));
    }

    @Test
    void testFold() {
        when(roundRepository.getReferenceById(roundEntity.getId())).thenReturn(roundEntity);
        when(playerService.getById(betCreationDto.getPlayerId())).thenReturn(playerEntity);
        when(roundRepository.findByStatusEmAndamento(roundEntity.getId())).thenReturn(true);

        BetEntity bet = betService.fold(playerEntity.getId(), roundEntity.getId());

        assertNotNull(bet);
        verify(roundRepository, times(1)).save(roundEntity);
        verify(playerRepository, times(1)).save(playerEntity);
    }

    @Test
    void testAllIn() {
        when(roundRepository.getReferenceById(roundEntity.getId())).thenReturn(roundEntity);
        when(playerService.getById(betCreationDto.getPlayerId())).thenReturn(playerEntity);
        when(betRepository.save(any(BetEntity.class))).thenReturn(new BetEntity());
        when(roundRepository.findByStatusEmAndamento(roundEntity.getId())).thenReturn(true);

        BetEntity bet = betService.allIn(playerEntity.getId(), roundEntity.getId());

        assertNotNull(bet);
        verify(betRepository, times(1)).save(any(BetEntity.class));
    }

    @Test
    void testUpdateAllInPlayers() {
        PlayerEntity otherPlayer = new PlayerEntity();
        otherPlayer.setFichas(BigDecimal.valueOf(1000));
        roundEntity.setCurrentPlayer(playerEntity);
        roundEntity.setMatch(new MatchEntity());
        roundEntity.getMatch().setPlayer1(playerEntity);
        roundEntity.getMatch().setPlayer2(otherPlayer);

        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(otherPlayer);
        when(roundRepository.save(any(RoundEntity.class))).thenReturn(roundEntity);

        betService.updateAllInPlayers(roundEntity, playerEntity);

        verify(playerRepository, times(1)).save(playerEntity);
        verify(roundRepository, times(1)).save(roundEntity);
    }


}
