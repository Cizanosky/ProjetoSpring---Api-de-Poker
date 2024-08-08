package api.poker.controllers;

import api.poker.domain.bet.BetEntity;
import api.poker.domain.bet.BetService;
import api.poker.domain.bet.dtos.*;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("bet")
public class BetController {

    @Autowired
    private BetService betService;

    @Autowired
    private ModelMapper mapper;

    @PostMapping
    @Transactional
    public ResponseEntity<BetDetailDto> toBet(@RequestBody BetCreationDto betCreationDto, UriComponentsBuilder uriComponentsBuilder) {
        BetEntity betEntity = betService.createBet(betCreationDto);
        URI uri = uriComponentsBuilder.path("/bet/{id}").buildAndExpand(betEntity.getId()).toUri();
        return ResponseEntity.created(uri).body(mapper.map(betEntity, BetDetailDto.class));
    }

    @PostMapping("check/{playerId}")
    @Transactional
    public ResponseEntity<BetCheckDetailDto> check(@PathVariable UUID playerId, @RequestParam UUID roundId) {
        BetEntity betEntity = betService.check(playerId, roundId);
        return ResponseEntity.ok(mapper.map(betEntity, BetCheckDetailDto.class));
    }

    @PostMapping("fold/{playerId}")
    @Transactional
    public ResponseEntity<BetFoldDetailDto> fold(@PathVariable UUID playerId, @RequestParam UUID roundId){
        BetEntity betEntity = betService.fold(playerId, roundId);
        return ResponseEntity.ok(mapper.map(betEntity, BetFoldDetailDto.class));
    }

    @PostMapping("allin/{playerId}")
    @Transactional
    public ResponseEntity<BetAllInDto> allin(@PathVariable UUID playerId, @RequestParam UUID roundId){
        BetEntity betEntity = betService.allIn(playerId, roundId);
        return ResponseEntity.ok(mapper.map(betEntity, BetAllInDto.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BetDetailDto> getBet(@PathVariable UUID id) {
        BetEntity betEntity = betService.findById(id);
        return ResponseEntity.ok().body(mapper.map(betEntity, BetDetailDto.class));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteBet(@RequestBody BetDeleteDto betDeleteDto) {
        betService.deleteById(betDeleteDto);
        return ResponseEntity.noContent().build();
    }

}
