package api.poker.controllers;

import api.poker.domain.playerhand.PlayerHandEntity;
import api.poker.domain.playerhand.PlayerHandService;
import api.poker.domain.playerhand.dtos.PlayerHandDetailDto;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("playerhand")
public class PlayerHandController {

    @Autowired
    PlayerHandService playerHandService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/{playerId}")
    @Transactional
    @PreAuthorize("#playerId == authentication.principal.id or hasRole('ROLE_ADMIN')")
    public ResponseEntity<PlayerHandDetailDto> getPlayerHand(@PathVariable UUID playerId, @RequestParam UUID roundId){
        PlayerHandEntity playerHandEntity = playerHandService.getByPlayerIdAndRoundId(playerId, roundId);
        return ResponseEntity.ok().body(mapper.map(playerHandEntity, PlayerHandDetailDto.class));
    }

}
