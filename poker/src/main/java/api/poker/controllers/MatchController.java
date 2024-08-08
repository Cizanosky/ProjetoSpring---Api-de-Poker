package api.poker.controllers;


import api.poker.domain.match.MatchEntity;
import api.poker.domain.match.MatchService;
import api.poker.domain.match.dtos.*;
import api.poker.domain.round.RoundEntity;
import api.poker.domain.round.RoundService;
import api.poker.domain.round.dtos.RoundDetailDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("match")
public class MatchController {

    @Autowired
    private MatchService matchService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private RoundService roundService;

    @PostMapping
    @Transactional
    public ResponseEntity<MatchDetailDto> registerMatch(@RequestBody @Valid MatchRegisterDto matchRegisterDto, UriComponentsBuilder uriComponentsBuilder){
        MatchEntity matchEntity = matchService.create(matchRegisterDto);
        URI uri = uriComponentsBuilder.path("/player/{id}").buildAndExpand(matchEntity.getId()).toUri();
        return ResponseEntity.created(uri).body(mapper.map(matchEntity, MatchDetailDto.class));
    }

    @GetMapping
    public ResponseEntity<Page<MatchDetailDto>> getAllMatches(@PageableDefault(size = 10) Pageable pageable){
        Page<MatchEntity> matches = matchService.getAll(pageable);
        return ResponseEntity.ok().body(matches.map(m -> mapper.map(m, MatchDetailDto.class)));
    }

    @GetMapping("/progress")
    public ResponseEntity<Page<MatchDetailDto>> getAllMatchesInProgress(@PageableDefault(size = 10) Pageable pageable){
        Page<MatchEntity> matches = matchService.getAllInProgress(pageable);
        return ResponseEntity.ok().body(matches.map(m -> mapper.map(m, MatchDetailDto.class)));
    }

    @GetMapping("/finished")
    public ResponseEntity<Page<MatchFinishedDetailDto>> getAllMatchesFinished(@PageableDefault(size = 10) Pageable pageable){
        Page<MatchEntity> matches = matchService.getAllFinished(pageable);
        return ResponseEntity.ok().body(matches.map(m -> mapper.map(m, MatchFinishedDetailDto.class)));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<MatchDetailDto> updatePlayersInAMatch(@RequestBody @Valid MatchUpdateDto matchUpdateDto){
        MatchEntity matchEntity = matchService.update(matchUpdateDto);
        return ResponseEntity.ok().body(mapper.map(matchEntity, MatchDetailDto.class));
    }

    @DeleteMapping(value = "/delete/{id}")
    @Transactional
    public ResponseEntity<MatchDetailDto> deleteMatch(@PathVariable UUID id){
        matchService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<MatchDetailDto> getMatch(@PathVariable UUID id){
        MatchEntity matchEntity = matchService.getById(id);
        return ResponseEntity.ok().body(mapper.map(matchEntity, MatchDetailDto.class));
    }

    @PutMapping("/start/{id}")
    @Transactional
    public ResponseEntity<RoundDetailDto> startMatch(@PathVariable UUID id){
        RoundEntity roundEntity = roundService.create(matchService.startMatch(id));
        return ResponseEntity.ok().body(mapper.map(roundEntity, RoundDetailDto.class));
    }

    @GetMapping("/playerWinner/{id}")
    @Transactional
    public ResponseEntity<MatchPlayerWinnerDetailDto> getPlayerWinnerInMatch(@PathVariable UUID id){
        MatchEntity matchEntity = matchService.getByIdFinished(id);
        return ResponseEntity.ok().body(mapper.map(matchEntity, MatchPlayerWinnerDetailDto.class));
    }

}
