package api.poker.domain.playerhand;

import api.poker.application.exceptions.ResourceNotFoundException;
import api.poker.domain.card.CardEntity;
import api.poker.domain.player.PlayerEntity;
import api.poker.domain.round.RoundEntity;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Stack;
import java.util.UUID;

@Service
public class PlayerHandService {

    private final PlayerHandRepository playerHandRepository;

    @Autowired
    public PlayerHandService(PlayerHandRepository playerHandRepository) {
        this.playerHandRepository = playerHandRepository;
    }

    @Transactional
    public void createHandsForMatch(RoundEntity roundEntity, Stack<CardEntity> deck){
        playerHandRepository.save(getPlayerHandEntity(roundEntity, roundEntity.getMatch().getPlayer1(), deck));
        playerHandRepository.save(getPlayerHandEntity(roundEntity, roundEntity.getMatch().getPlayer2(), deck));
    }

    private static PlayerHandEntity getPlayerHandEntity(RoundEntity roundEntity, PlayerEntity player, Stack<CardEntity> deck) {
        PlayerHandEntity hand = new PlayerHandEntity();
        hand.setRound(roundEntity);
        hand.setPlayer(player);
        hand.setCard1(deck.pop());
        hand.setCard2(deck.pop());
        hand.setCreatedDate(LocalDateTime.now());
        return hand;
    }

    public PlayerHandEntity getByPlayerIdAndRoundId(UUID playerId, UUID roundId) {
        return playerHandRepository.findByPlayerIdAndRoundId(playerId, roundId)
                .orElseThrow(() -> new ResourceNotFoundException("Player hand not found for player with id " + playerId));
    }
}
