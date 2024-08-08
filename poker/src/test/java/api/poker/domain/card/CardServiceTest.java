package api.poker.domain.card;

import api.poker.application.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepository cardRepository;

    private CardEntity cardEntity;

    @BeforeEach
    void setUp() {
        cardEntity = new CardEntity();
    }

    @Test
    @DisplayName("Testa a obtenção de todas as cartas com paginação")
    void testGetAllCards() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<CardEntity> page = new PageImpl<>(Collections.singletonList(cardEntity), pageable, 1);

        when(cardRepository.findAll(pageable)).thenReturn(page);

        Page<CardEntity> result = cardService.getAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(cardEntity, result.getContent().get(0));
    }

    @Test
    @DisplayName("Testa a obtenção de uma carta por ID")
    void testGetCardById() {
        when(cardRepository.findById("card1")).thenReturn(Optional.of(cardEntity));

        CardEntity result = cardService.getById("card1");

        assertNotNull(result);
        assertEquals(cardEntity, result);
    }

    @Test
    @DisplayName("Testa a obtenção de uma carta por ID quando a carta não é encontrada")
    void testGetCardByIdNotFound() {
        when(cardRepository.findById("card1")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cardService.getById("card1"));
    }

    @Test
    @DisplayName("Testa a obtenção do deck de cartas")
    void testGetDeck() {
        List<CardEntity> cardList = Collections.singletonList(cardEntity);
        when(cardRepository.findAll()).thenReturn(cardList);

        Stack<CardEntity> deck = cardService.getDeck();

        assertNotNull(deck);
        assertEquals(1, deck.size());
        assertEquals(cardEntity, deck.peek());  // Verifica se a carta está no topo do deck
    }

}
