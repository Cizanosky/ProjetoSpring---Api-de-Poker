package api.poker.domain.communitycard;

import api.poker.application.exceptions.ResourceNotFoundException;
import api.poker.domain.card.CardEntity;
import api.poker.domain.round.RoundEntity;
import api.poker.domain.round.RoundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Stack;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommunityCardServiceTest {

    @InjectMocks
    private CommunityCardService communityCardService;

    @Mock
    private CommunityCardRepository communityCardRepository;

    @Mock
    private RoundRepository roundRepository;

    private RoundEntity roundEntity;
    private CommunityCardEntity communityCardEntity;
    private Stack<CardEntity> deck;

    @BeforeEach
    void setUp() {
        deck = new Stack<>();
        CardEntity card1 = new CardEntity();
        CardEntity card2 = new CardEntity();
        CardEntity card3 = new CardEntity();
        deck.push(card1);
        deck.push(card2);
        deck.push(card3);

        roundEntity = new RoundEntity();
        roundEntity.setId(UUID.randomUUID());

        communityCardEntity = new CommunityCardEntity();
        communityCardEntity.setRound(roundEntity);
    }

    @Test
    @DisplayName("Testa a criação de cartas comunitárias")
    void testCreateCommunityCards() {
        when(communityCardRepository.save(any(CommunityCardEntity.class))).thenReturn(communityCardEntity);

        communityCardService.createCommunityCards(roundEntity, deck);

        verify(communityCardRepository, times(3)).save(any(CommunityCardEntity.class));
    }

    @Test
    @DisplayName("Testa a revelação da próxima carta comunitária")
    void testRevealNextCard() {
        when(communityCardRepository.findFirstByRoundAndVisivelFalse(roundEntity)).thenReturn(communityCardEntity);
        when(communityCardRepository.save(any(CommunityCardEntity.class))).thenReturn(communityCardEntity);

        boolean result = communityCardService.revealNextCard(roundEntity);

        assertFalse(result);
        verify(communityCardRepository).save(communityCardEntity);
    }

    @Test
    @DisplayName("Testa a revelação de todas as cartas comunitárias já visíveis")
    void testRevealNextCardWhenAllCardsVisible() {
        when(communityCardRepository.findFirstByRoundAndVisivelFalse(roundEntity)).thenReturn(null);

        boolean result = communityCardService.revealNextCard(roundEntity);

        assertTrue(result);
        verify(communityCardRepository, never()).save(any(CommunityCardEntity.class));
    }

    @Test
    @DisplayName("Testa a recuperação das cartas comunitárias visíveis por ID de rodada")
    void testGetVisibleCardsByRound() {
        UUID roundId = UUID.randomUUID();
        List<CommunityCardEntity> cards = List.of(communityCardEntity);
        when(roundRepository.existsById(roundId)).thenReturn(true);
        when(communityCardRepository.findAllByRoundIdAndVisivelTrue(roundId)).thenReturn(cards);

        Stack<CommunityCardEntity> result = communityCardService.getVisibleCardsByRound(roundId);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(communityCardEntity, result.peek());
    }

    @Test
    @DisplayName("Testa a tentativa de recuperação das cartas comunitárias visíveis para uma rodada inexistente")
    void testGetVisibleCardsByRoundNotFound() {
        UUID roundId = UUID.randomUUID();
        when(roundRepository.existsById(roundId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> communityCardService.getVisibleCardsByRound(roundId));
    }
}
