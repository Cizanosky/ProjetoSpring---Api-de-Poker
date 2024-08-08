package api.poker.controllers;

import api.poker.domain.card.CardEntity;
import api.poker.domain.card.CardService;
import api.poker.domain.card.dtos.CardDetailDto;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("card")
public class CardController {

    @Autowired
    CardService cardService;

    @Autowired
    private ModelMapper mapper;

    @GetMapping("/all")
    public ResponseEntity<Page<CardDetailDto>> getAllCards(@PageableDefault(size = 4, sort = "rankValue", direction = Sort.Direction.ASC) Pageable pageable){
        Page<CardEntity> cards = cardService.getAll(pageable);
        return ResponseEntity.ok().body(cards.map(c -> mapper.map(c, CardDetailDto.class)));
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<CardDetailDto> getCard(@PathVariable String id){
        CardEntity cardEntity = cardService.getById(id);
        return ResponseEntity.ok().body(mapper.map(cardEntity, CardDetailDto.class));
    }

}
