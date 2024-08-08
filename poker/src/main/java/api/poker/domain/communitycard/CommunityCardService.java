package api.poker.domain.communitycard;

import api.poker.application.exceptions.ResourceNotFoundException;
import api.poker.domain.card.CardEntity;
import api.poker.domain.round.RoundEntity;
import api.poker.domain.round.RoundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Stack;
import java.util.UUID;

@Service
public class CommunityCardService {

    private final CommunityCardRepository communityCardRepository;
    private final RoundRepository roundRepository;

    @Autowired
    public CommunityCardService(CommunityCardRepository communityCardRepository, RoundRepository roundRepository) {
        this.communityCardRepository = communityCardRepository;
        this.roundRepository = roundRepository;
    }

    @Transactional
    public void createCommunityCards(RoundEntity roundEntity, Stack<CardEntity> deck) {
        for (int i = 0; i < 3; i++) {
            CommunityCardEntity communityCard = new CommunityCardEntity();
            communityCard.setRound(roundEntity);
            communityCard.setCard(deck.pop());
            communityCard.setVisivel(false);
            communityCardRepository.save(communityCard);
        }
    }

    @Transactional
    public boolean revealNextCard(RoundEntity roundEntity) {
        CommunityCardEntity nextCard = communityCardRepository.findFirstByRoundAndVisivelFalse(roundEntity);
        if (nextCard != null) {
            nextCard.setVisivel(true);
            communityCardRepository.save(nextCard);
            return false;
        }
        return true;
    }

    public Stack<CommunityCardEntity> getVisibleCardsByRound(UUID roundId) {
        if (!roundRepository.existsById(roundId)){
            throw new ResourceNotFoundException("Round not found with id " + roundId);
        }
        List<CommunityCardEntity> cards = communityCardRepository.findAllByRoundIdAndVisivelTrue(roundId);
        Stack<CommunityCardEntity> stack = new Stack<>();
        stack.addAll(cards);
        return stack;
    }
}
