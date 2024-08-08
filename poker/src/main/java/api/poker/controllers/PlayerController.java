package api.poker.controllers;

import api.poker.application.authentications.TokenService;
import api.poker.domain.player.PlayerEntity;
import api.poker.domain.player.PlayerService;
import api.poker.domain.player.dtos.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("player")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;


    @PostMapping("/login")
    public ResponseEntity<PlayerLoginDto> loginPlayer(@RequestBody @Valid PlayerAuthenticationDto dto) {
        UsernamePasswordAuthenticationToken userNamePassword = new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword());
        Authentication auth = this.authenticationManager.authenticate(userNamePassword);
        String token = tokenService.generateToken((PlayerEntity) auth.getPrincipal());
        return ResponseEntity.ok(new PlayerLoginDto(token));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<PlayerDetailDto> registerPlayer(@RequestBody @Valid PlayerRegisterDto playerRegisterDto, UriComponentsBuilder uriComponentsBuilder){
        PlayerEntity playerEntity = playerService.create(playerRegisterDto);
        URI uri = uriComponentsBuilder.path("/player/{id}").buildAndExpand(playerEntity.getId()).toUri();
        return ResponseEntity.created(uri).body(mapper.map(playerEntity, PlayerDetailDto.class));
    }

    @GetMapping
    public ResponseEntity<Page<PlayerDetailDto>> getAllPlayers(@PageableDefault(size = 10, sort = "nome")Pageable pageable){
        Page<PlayerEntity> players = playerService.getAll(pageable);
        return ResponseEntity.ok().body(players.map(p -> mapper.map(p, PlayerDetailDto.class)));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<PlayerDetailDto> updatePlayer(@RequestBody @Valid PlayerUpdateDto playerUpdateDto){
        PlayerEntity playerEntity = playerService.update(playerUpdateDto);
        return ResponseEntity.ok().body(mapper.map(playerEntity, PlayerDetailDto.class));
    }

    @DeleteMapping(value = "/delete/{id}")
    @Transactional
    public ResponseEntity<PlayerDetailDto> deletePlayer(@PathVariable UUID id){
        playerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<PlayerDetailDto> getPlayer(@PathVariable UUID id){
        PlayerEntity playerEntity = playerService.getById(id);
        return ResponseEntity.ok().body(mapper.map(playerEntity, PlayerDetailDto.class));
    }

}
