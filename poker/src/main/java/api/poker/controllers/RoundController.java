package api.poker.controllers;

import api.poker.domain.communitycard.CommunityCardService;
import api.poker.domain.round.RoundEntity;
import api.poker.domain.round.RoundService;
import api.poker.domain.round.dtos.RoundDetailDto;
import api.poker.domain.round.dtos.RoundDetailWinnerPlayerDto;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("round")
public class RoundController {

    @Autowired
    private RoundService roundService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CommunityCardService communityCardService;

    @GetMapping
    public ResponseEntity<Page<RoundDetailDto>> getAllRounds(@PageableDefault(size = 10) Pageable pageable){
        Page<RoundEntity> rounds = roundService.getAll(pageable);
        return ResponseEntity.ok().body(rounds.map(r -> mapper.map(r, RoundDetailDto.class)));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<RoundDetailDto> getRound(@PathVariable UUID id){
        RoundEntity roundEntity = roundService.getById(id);
        return ResponseEntity.ok().body(mapper.map(roundEntity, RoundDetailDto.class));
    }

    @GetMapping("/playerWinner/{id}")
    @Transactional
    public ResponseEntity<RoundDetailWinnerPlayerDto> getPlayerWinnerByRound(@PathVariable UUID id){
        RoundEntity roundEntity = roundService.getPlayerWinnerByRound(id);
        return ResponseEntity.ok().body(mapper.map(roundEntity, RoundDetailWinnerPlayerDto.class));
    }


}
