package api.poker.domain.match;

import api.poker.application.exceptions.InvalidDataException;
import api.poker.application.exceptions.MatchInProgressException;
import api.poker.application.exceptions.ResourceNotFoundException;
import api.poker.domain.match.dtos.MatchRegisterDto;
import api.poker.domain.match.dtos.MatchUpdateDto;
import api.poker.domain.player.PlayerEntity;
import api.poker.domain.player.PlayerService;
import api.poker.domain.round.RoundEntity;
import api.poker.domain.round.RoundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @InjectMocks
    private MatchService matchService;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private PlayerService playerService;

    @Mock
    private RoundService roundService;

    private MatchRegisterDto matchRegisterDto;
    private MatchUpdateDto matchUpdateDto;
    private MatchEntity matchEntity;
    private PlayerEntity player1;
    private PlayerEntity player2;

    @BeforeEach
    void setUp() {
        player1 = new PlayerEntity();
        player1.setId(UUID.randomUUID());
        player1.setFichas(BigDecimal.TEN);

        player2 = new PlayerEntity();
        player2.setId(UUID.randomUUID());
        player2.setFichas(BigDecimal.TEN);

        matchRegisterDto = new MatchRegisterDto();
        matchRegisterDto.setPlayer1(player1.getId());
        matchRegisterDto.setPlayer2(player2.getId());
        matchRegisterDto.setDt_criacao(LocalDateTime.now());

        matchUpdateDto = new MatchUpdateDto();
        matchUpdateDto.setId(UUID.randomUUID());
        matchUpdateDto.setPlayer1(player1.getId());
        matchUpdateDto.setPlayer2(player2.getId());

        matchEntity = new MatchEntity();
        matchEntity.setId(UUID.randomUUID());
        matchEntity.setPlayer1(player1);
        matchEntity.setPlayer2(player2);
        matchEntity.setStatus("Criado");
    }

    @Test
    @DisplayName("Testa a criação de uma partida")
    void testCreateMatch() {
        when(playerService.getById(matchRegisterDto.getPlayer1())).thenReturn(player1);
        when(playerService.getById(matchRegisterDto.getPlayer2())).thenReturn(player2);
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(matchEntity);

        MatchEntity createdMatch = matchService.create(matchRegisterDto);

        assertNotNull(createdMatch);
        assertEquals("Criado", createdMatch.getStatus());
        verify(matchRepository).save(any(MatchEntity.class));
    }

    @Test
    @DisplayName("Testa a obtenção de todas as partidas")
    void testGetAllMatches() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MatchEntity> page = new PageImpl<>(Collections.singletonList(matchEntity), pageable, 1);

        when(matchRepository.findAll(pageable)).thenReturn(page);

        Page<MatchEntity> result = matchService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(matchEntity, result.getContent().get(0));
    }

    @Test
    @DisplayName("Testa a obtenção de partidas em andamento")
    void testGetAllInProgressMatches() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MatchEntity> page = new PageImpl<>(Collections.singletonList(matchEntity), pageable, 1);

        when(matchRepository.findAllByStatusEmAndamento(pageable)).thenReturn(page);

        Page<MatchEntity> result = matchService.getAllInProgress(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(matchEntity, result.getContent().get(0));
    }

    @Test
    @DisplayName("Testa a obtenção de partidas finalizadas")
    void testGetAllFinishedMatches() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<MatchEntity> page = new PageImpl<>(Collections.singletonList(matchEntity), pageable, 1);

        when(matchRepository.findAllByStatusFinalizado(pageable)).thenReturn(page);

        Page<MatchEntity> result = matchService.getAllFinished(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(matchEntity, result.getContent().get(0));
    }


    @Test
    @DisplayName("Testa a atualização de uma partida quando a partida não é encontrada")
    void testUpdateMatchNotFound() {
        when(matchRepository.findById(matchUpdateDto.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> matchService.update(matchUpdateDto));
    }

    @Test
    @DisplayName("Testa a atualização de uma partida que já está em andamento")
    void testUpdateMatchInProgress() {
        when(matchRepository.findById(matchUpdateDto.getId())).thenReturn(Optional.of(matchEntity));
        when(matchRepository.findStatusCriadoById(matchEntity.getId())).thenReturn(false);

        assertThrows(InvalidDataException.class, () -> matchService.update(matchUpdateDto));
    }

    @Test
    @DisplayName("Testa a exclusão de uma partida")
    void testDeleteMatch() {
        UUID matchId = UUID.randomUUID();
        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setId(matchId);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(matchEntity));

        when(matchRepository.findByStatusEmAndamento(matchId)).thenReturn(false);

        matchService.delete(matchId);

        verify(matchRepository).delete(matchEntity);
    }


    @Test
    @DisplayName("Testa a exclusão de uma partida que está em andamento")
    void testDeleteMatchInProgress() {
        UUID matchId = UUID.randomUUID();
        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setId(matchId);

        when(matchRepository.findById(matchId)).thenReturn(Optional.of(matchEntity));

        when(matchRepository.findByStatusEmAndamento(matchId)).thenReturn(true);

        assertThrows(MatchInProgressException.class, () -> matchService.delete(matchId));
    }


    @Test
    @DisplayName("Testa o início de uma partida")
    void testStartMatch() {
        when(matchRepository.findById(matchEntity.getId())).thenReturn(Optional.of(matchEntity));
        when(matchRepository.save(any(MatchEntity.class))).thenReturn(matchEntity);

        MatchEntity startedMatch = matchService.startMatch(matchEntity.getId());

        assertNotNull(startedMatch);
        assertEquals("Em andamento", startedMatch.getStatus());
        verify(matchRepository).save(any(MatchEntity.class));
    }

    @Test
    @DisplayName("Testa o início de uma partida com jogador sem fichas")
    void testStartMatchWithPlayerNoChips() {
        player1.setFichas(BigDecimal.ZERO);
        matchEntity.setPlayer1(player1);

        when(matchRepository.findById(matchEntity.getId())).thenReturn(Optional.of(matchEntity));

        assertThrows(InvalidDataException.class, () -> matchService.startMatch(matchEntity.getId()));
    }

    @Test
    @DisplayName("Testa a continuação de uma partida")
    void testContinueMatch() {
        RoundEntity round = new RoundEntity();
        round.setMatch(matchEntity);

        when(roundService.create(any(MatchEntity.class))).thenReturn(round);

        matchService.continueMatch(round);

        verify(roundService).create(matchEntity);
    }

    @Test
    @DisplayName("Testa a continuação de uma partida com um jogador sem fichas")
    void testContinueMatchWithPlayerNoChips() {
        player1.setFichas(BigDecimal.ZERO);
        matchEntity.setPlayer1(player1);
        RoundEntity round = new RoundEntity();
        round.setMatch(matchEntity);

        matchService.continueMatch(round);

        verify(matchRepository).save(matchEntity);
    }

    @Test
    @DisplayName("Testa a obtenção de uma partida finalizada por ID")
    void testGetByIdFinished() {
        UUID matchId = UUID.randomUUID();
        when(matchRepository.findMatchByIdFinalizado(matchId)).thenReturn(Optional.of(matchEntity));

        MatchEntity result = matchService.getByIdFinished(matchId);

        assertNotNull(result);
        assertEquals(matchEntity, result);
    }

    @Test
    @DisplayName("Testa a obtenção de uma partida finalizada que não existe")
    void testGetByIdFinishedNotFound() {
        UUID matchId = UUID.randomUUID();
        when(matchRepository.findMatchByIdFinalizado(matchId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> matchService.getByIdFinished(matchId));
    }

}
