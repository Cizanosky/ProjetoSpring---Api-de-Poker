package api.poker.domain.player;

import api.poker.application.exceptions.ResourceNotFoundException;
import api.poker.domain.player.dtos.PlayerRegisterDto;
import api.poker.domain.player.dtos.PlayerUpdateDto;
import api.poker.domain.player.validation.PlayerCreationValidator;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final ModelMapper mapper;
    private final List<PlayerCreationValidator> playerCreationValidators;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, ModelMapper mapper, List<PlayerCreationValidator> playerCreationValidators) {
        this.playerRepository = playerRepository;
        this.mapper = mapper;
        this.playerCreationValidators = playerCreationValidators;
    }

    @Transactional
    public PlayerEntity create(PlayerRegisterDto playerRegisterDto) {
        PlayerEntity playerEntity = mapper.map(playerRegisterDto, PlayerEntity.class);
        playerCreationValidators.forEach(v -> v.validate(playerEntity));
        playerEntity.setSenha(new BCryptPasswordEncoder().encode(playerRegisterDto.getSenha()));
        if (playerRegisterDto.getEmail().equals("admin@admin.com")) {
            playerEntity.setRole(PlayerRole.ADMIN);
        } else {
            playerEntity.setRole(PlayerRole.USER);
        }
        return playerRepository.save(playerEntity);
    }

    public Page<PlayerEntity> getAll(Pageable pageable) {
        return playerRepository.findAll(pageable);
    }

    @PreAuthorize("#playerUpdateDto.id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @Transactional
    public PlayerEntity update(PlayerUpdateDto playerUpdateDto) {
        PlayerEntity playerEntity = playerRepository.findById(playerUpdateDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Player not found with id " + playerUpdateDto.getId()));
        mapper.map(playerUpdateDto, playerEntity);
        return playerRepository.save(playerEntity);
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ROLE_ADMIN')")
    @Transactional
    public void delete(UUID id) {
        playerRepository.delete(playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Player not found with id " + id)));
    }

    public PlayerEntity getById(UUID id) {
        return playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Player not found with id " + id));
    }

}
