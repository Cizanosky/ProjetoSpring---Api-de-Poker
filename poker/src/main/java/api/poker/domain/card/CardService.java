package api.poker.domain.card;

import api.poker.application.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Stack;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public Page<CardEntity> getAll(Pageable pageable) {
        return cardRepository.findAll(pageable);
    }

    public CardEntity getById(String id) {
        return cardRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Card not found with id " + id));
    }

    public Stack<CardEntity> getDeck(){
        final Stack<CardEntity> deckCards = new Stack<>();
        cardRepository.findAll().forEach(deckCards::push);
        Collections.shuffle(deckCards);
        return deckCards;
    }
}
