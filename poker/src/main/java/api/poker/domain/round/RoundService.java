package api.poker.domain.round;

import api.poker.application.exceptions.ResourceNotFoundException;
import api.poker.domain.card.CardEntity;
import api.poker.domain.card.CardService;
import api.poker.domain.communitycard.CommunityCardService;
import api.poker.domain.match.MatchEntity;
import api.poker.domain.playerhand.PlayerHandService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Stack;
import java.util.UUID;

@Service
public class RoundService {

    private final RoundRepository roundRepository;
    private final CardService cardService;
    private final PlayerHandService playerHandService;
    private final CommunityCardService communityCardService;

    @Autowired
    public RoundService(RoundRepository roundRepository, CardService cardService, PlayerHandService playerHandService, CommunityCardService communityCardService) {
        this.roundRepository = roundRepository;
        this.cardService = cardService;
        this.playerHandService = playerHandService;
        this.communityCardService = communityCardService;
    }

    @Transactional
    public RoundEntity create(MatchEntity matchEntity) {
        RoundEntity roundEntity = new RoundEntity();
        roundEntity.setMatch(matchEntity);
        roundEntity.setPotValue(BigDecimal.ZERO);
        roundEntity.setCreatedDate(LocalDateTime.now());
        roundEntity.setCurrentPlayer(matchEntity.getPlayer1());
        roundEntity.setCurrentAmount(BigDecimal.ZERO);
        roundEntity.setStatus("Em andamento");
        Stack<CardEntity> deck = cardService.getDeck();
        roundRepository.save(roundEntity);
        playerHandService.createHandsForMatch(roundEntity, deck);
        communityCardService.createCommunityCards(roundEntity, deck);
        return roundEntity;
    }

    public Page<RoundEntity> getAll(Pageable pageable) {
        return roundRepository.findAll(pageable);
    }

    public RoundEntity getById(UUID id) {
        return roundRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Round not found with id " + id));
    }

    public RoundEntity getPlayerWinnerByRound(UUID id){
        return roundRepository.findPlayerWinner(id).orElseThrow(() -> new ResourceNotFoundException("This round doesn't have a winner yet with id: " + id));
    }

}
