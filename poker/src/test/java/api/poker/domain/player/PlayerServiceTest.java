package api.poker.domain.player;

import api.poker.application.exceptions.ResourceNotFoundException;
import api.poker.domain.player.dtos.PlayerRegisterDto;
import api.poker.domain.player.dtos.PlayerUpdateDto;
import api.poker.domain.player.validation.PlayerCreationValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {
    @InjectMocks
    private PlayerService playerService;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private List<PlayerCreationValidator> playerCreationValidators;

    private PlayerRegisterDto playerRegisterDto;
    private PlayerUpdateDto playerUpdateDto;
    private PlayerEntity playerEntity;

    @BeforeEach
    void setUp() {
        playerRegisterDto = new PlayerRegisterDto();
        playerRegisterDto.setEmail("user@domain.com");
        playerRegisterDto.setSenha("password");

        playerUpdateDto = new PlayerUpdateDto();
        playerUpdateDto.setId(UUID.randomUUID());
        playerUpdateDto.setEmail("updated@domain.com");

        playerEntity = new PlayerEntity();
        playerEntity.setId(UUID.randomUUID());
        playerEntity.setEmail("user@domain.com");

    }


    @Test
    @DisplayName("Testa a criação de um jogador com role USER")
    void testCreatePlayer() {
        when(mapper.map(playerRegisterDto, PlayerEntity.class)).thenReturn(playerEntity);
        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(playerEntity);

        PlayerEntity createdPlayer = playerService.create(playerRegisterDto);

        verify(playerRepository).save(playerEntity);

        assertEquals(PlayerRole.USER, createdPlayer.getRole());
    }

    @Test
    @DisplayName("Testa a criação de um jogador com role ADMIN")
    void testCreatePlayerAsAdmin() {
        playerRegisterDto.setEmail("admin@admin.com");
        playerEntity.setEmail("admin@admin.com");

        when(mapper.map(playerRegisterDto, PlayerEntity.class)).thenReturn(playerEntity);
        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(playerEntity);

        PlayerEntity createdPlayer = playerService.create(playerRegisterDto);

        verify(playerRepository).save(playerEntity);

        assertEquals(PlayerRole.ADMIN, createdPlayer.getRole());
    }

    @Test
    @DisplayName("Testa a recuperação de todos os jogadores paginados")
    void testGetAllPlayers() {
        Pageable pageable = PageRequest.of(0, 10);
        List<PlayerEntity> playerList = Collections.singletonList(playerEntity);
        Page<PlayerEntity> page = new PageImpl<>(playerList, pageable, playerList.size());

        when(playerRepository.findAll(pageable)).thenReturn(page);

        Page<PlayerEntity> result = playerService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(playerEntity, result.getContent().get(0));
    }

    @Test
    @DisplayName("Testa a atualização de um jogador existente")
    void testUpdatePlayer() {
        when(playerRepository.findById(playerUpdateDto.getId())).thenReturn(Optional.of(playerEntity));
        when(playerRepository.save(any(PlayerEntity.class))).thenReturn(playerEntity);

        PlayerEntity updatedPlayer = playerService.update(playerUpdateDto);

        verify(mapper).map(playerUpdateDto, playerEntity);
        verify(playerRepository).save(playerEntity);

        assertEquals(playerEntity, updatedPlayer);
    }

    @Test
    @DisplayName("Testa a atualização de um jogador não encontrado")
    void testUpdatePlayerNotFound() {
        when(playerRepository.findById(playerUpdateDto.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.update(playerUpdateDto));
    }

    @Test
    @DisplayName("Testa a exclusão de um jogador existente")
    void testDeletePlayer() {
        UUID playerId = UUID.randomUUID();
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(playerEntity));

        playerService.delete(playerId);

        verify(playerRepository).delete(playerEntity);
    }

    @Test
    @DisplayName("Testa a exclusão de um jogador não encontrado")
    void testDeletePlayerNotFound() {
        UUID playerId = UUID.randomUUID();
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.delete(playerId));
    }

    @Test
    @DisplayName("Testa a recuperação de um jogador pelo ID")
    void testGetPlayerById() {
        UUID playerId = UUID.randomUUID();
        when(playerRepository.findById(playerId)).thenReturn(Optional.of(playerEntity));

        PlayerEntity result = playerService.getById(playerId);

        assertNotNull(result);
        assertEquals(playerEntity, result);
    }

    @Test
    @DisplayName("Testa a recuperação de um jogador pelo ID não encontrado")
    void testGetPlayerByIdNotFound() {
        UUID playerId = UUID.randomUUID();
        when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.getById(playerId));
    }


}