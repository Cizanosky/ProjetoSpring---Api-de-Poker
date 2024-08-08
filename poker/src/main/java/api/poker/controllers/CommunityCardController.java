package api.poker.controllers;

import api.poker.domain.communitycard.CommunityCardEntity;
import api.poker.domain.communitycard.CommunityCardService;
import api.poker.domain.communitycard.dtos.CommunityCardDetailDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Stack;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("communitycard")
public class CommunityCardController {

    @Autowired
    private CommunityCardService communityCardService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/round/{roundId}")
    public ResponseEntity<Stack<CommunityCardDetailDto>> getVisibleCommunityCards(@PathVariable UUID roundId) {
        Stack<CommunityCardEntity> visibleCards = communityCardService.getVisibleCardsByRound(roundId);
        Stack<CommunityCardDetailDto> communityCardDetailDtos = visibleCards.stream()
                .map(CommunityCardEntity -> mapper.map(CommunityCardEntity, CommunityCardDetailDto.class))
                .collect(Collectors.toCollection(Stack::new));
        return ResponseEntity.ok().body(communityCardDetailDtos);
    }
}
