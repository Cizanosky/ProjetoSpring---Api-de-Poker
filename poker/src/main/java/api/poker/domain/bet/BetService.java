package api.poker.domain.bet;

import api.poker.application.exceptions.ResourceNotFoundException;
import api.poker.application.exceptions.TurnBetException;
import api.poker.domain.bet.dtos.BetCreationDto;
import api.poker.domain.bet.dtos.BetDeleteDto;
import api.poker.domain.bet.validation.BetValidator;
import api.poker.domain.card.CardEntity;
import api.poker.domain.communitycard.CommunityCardEntity;
import api.poker.domain.communitycard.CommunityCardRepository;
import api.poker.domain.communitycard.CommunityCardService;
import api.poker.domain.match.MatchService;
import api.poker.domain.player.PlayerEntity;
import api.poker.domain.player.PlayerRepository;
import api.poker.domain.player.PlayerService;
import api.poker.domain.playerhand.PlayerHandEntity;
import api.poker.domain.playerhand.PlayerHandService;
import api.poker.domain.round.RoundEntity;
import api.poker.domain.round.RoundRepository;
import api.poker.domain.round.RoundService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BetService {

    private final BetRepository betRepository;
    private final List<BetValidator> betValidators;
    private final RoundService roundService;
    private final PlayerService playerService;
    private final CommunityCardService communityCardService;
    private final PlayerRepository playerRepository;
    private final RoundRepository roundRepository;
    private final MatchService matchService;
    private final CommunityCardRepository communityCardRepository;
    private final PlayerHandService playerHandService;
    private final HandsClient handsClient;

    public BetService(BetRepository betRepository, List<BetValidator> betValidators, RoundService roundService, PlayerService playerService, CommunityCardService communityCardService, PlayerRepository playerRepository, RoundRepository roundRepository, MatchService matchService, CommunityCardRepository communityCardRepository, PlayerHandService playerHandService, HandsClient handsClient) {
        this.betRepository = betRepository;
        this.betValidators = betValidators;
        this.roundService = roundService;
        this.playerService = playerService;
        this.communityCardService = communityCardService;
        this.playerRepository = playerRepository;
        this.roundRepository = roundRepository;
        this.matchService = matchService;
        this.communityCardRepository = communityCardRepository;
        this.playerHandService = playerHandService;
        this.handsClient = handsClient;
    }

    @Transactional
    @PreAuthorize("#betCreationDto.playerId == authentication.principal.id")
    public BetEntity createBet(BetCreationDto betCreationDto) {
        checkRoundStatus(betCreationDto.getRoundId());
        BetEntity betEntity = new BetEntity();
        betEntity.setPlayer(playerService.getById(betCreationDto.getPlayerId()));
        betEntity.setRound(roundService.getById(betCreationDto.getRoundId()));
        betEntity.setValor(betCreationDto.getValor());
        betEntity.setDataCriacao(LocalDateTime.now());
        PlayerEntity player = betEntity.getPlayer();
        RoundEntity round = betEntity.getRound();

        if (isTurnOfPlayer(betEntity.getRound(), betEntity.getPlayer())) {
            throw new TurnBetException("It's not this player's turn.");
        }

        betValidators.forEach(v -> v.validate(betEntity));

        if (!verificarAllIn(player, round)) {
            if (betEntity.getValor().compareTo(betEntity.getRound().getCurrentAmount()) > 0) {
                updateRoundAndPlayer(player, betEntity, round, betEntity.getValor());
                atualizarVez(betEntity.getRound());
            } else if (betEntity.getValor().compareTo(betEntity.getRound().getCurrentAmount()) == 0) {
                updateRoundAndPlayer(player, betEntity, round, BigDecimal.ZERO);
                if (communityCardService.revealNextCard(round)) {
                    concluirRoundShowDown(round);
                }
                round.setCurrentPlayer(round.getMatch().getPlayer1());
                roundRepository.save(round);
            }
        } else {
            updateRoundAndPlayer(player, betEntity, round, BigDecimal.ZERO);
            concluirRoundShowDown(round);
        }

        return betRepository.save(betEntity);
    }

    private boolean verificarAllIn(PlayerEntity player, RoundEntity round) {
        if (player.equals(round.getMatch().getPlayer1())){
            return round.getMatch().getPlayer2().getFichas().compareTo(BigDecimal.ZERO) == 0;
        } else {
            return round.getMatch().getPlayer1().getFichas().compareTo(BigDecimal.ZERO) == 0;
        }

    }

    @Transactional
    private void concluirRoundShowDown(RoundEntity round) {
        PlayerEntity player1 = round.getMatch().getPlayer1();
        PlayerEntity player2 = round.getMatch().getPlayer2();
        PlayerHandEntity player1Hand = playerHandService.getByPlayerIdAndRoundId(player1.getId(), round.getId());
        PlayerHandEntity player2Hand = playerHandService.getByPlayerIdAndRoundId(player2.getId(), round.getId());

        List<CommunityCardEntity> communityCardsEntity = communityCardRepository.findAllByRound(round.getId());
        List<CardEntity> communityCards = communityCardsEntity.stream()
                .map(CommunityCardEntity::getCard)
                .collect(Collectors.toList());

        List<Map<String, String>> player1Cards = convertCards(List.of(player1Hand.getCard1(), player1Hand.getCard2()));
        List<Map<String, String>> player2Cards = convertCards(List.of(player2Hand.getCard1(), player2Hand.getCard2()));
        List<Map<String, String>> communityCardsConverted = convertCards(communityCards);

        player1Cards.addAll(communityCardsConverted);
        player2Cards.addAll(communityCardsConverted);

        List<Map<String, Object>> requestBody = List.of(
                Map.of(
                        "jugadas", List.of(
                                Map.of("jugador", player1.getNome(), "cartas", player1Cards),
                                Map.of("jugador", player2.getNome(), "cartas", player2Cards)
                        )
                )
        );

        ResponseEntity<String> response = handsClient.compareHands(requestBody);

        String result = response.getBody();
        if (result == null || result.isEmpty()) {
            throw new RuntimeException("Error when comparing players' hands.");
        }

        PlayerEntity winner = result.contains(player1.getNome()) ? player1 : player2;
        PlayerEntity loser = result.contains(player1.getNome()) ? player2 : player1;

        concluirRound(winner, loser, round);
    }

    @Transactional
    private void concluirRound(PlayerEntity playerWinner, PlayerEntity playerLoser, RoundEntity round) {
        playerWinner.setFichas(playerWinner.getFichas().add(round.getPotValue()));
        playerRepository.save(playerWinner);
        round.setWinPlayer(playerWinner);
        round.setLoserPlayer(playerLoser);
        round.setCurrentAmount(BigDecimal.ZERO);
        round.setStatus("Finalizado");
        roundRepository.save(round);
        matchService.continueMatch(round);
    }

    @Transactional
    private void updateRoundAndPlayer(PlayerEntity player, BetEntity betEntity, RoundEntity round, BigDecimal valor) {
        player.setFichas(player.getFichas().subtract(betEntity.getValor()));
        playerRepository.save(player);
        round.setCurrentAmount(valor);
        round.setPotValue(round.getPotValue().add(betEntity.getValor()));
        roundRepository.save(round);
    }

    public BetEntity findById(UUID id) {
        return betRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Bet not found with id " + id));
    }

    @PreAuthorize("#betDeleteDto.playerId == authentication.principal.id")
    public void deleteById(BetDeleteDto betDeleteDto) {
        BetEntity betEntity = betRepository.findById(betDeleteDto.getBetId())
                .orElseThrow(() -> new ResourceNotFoundException("Bet not found with id " + betDeleteDto.getBetId()));

        betRepository.delete(betEntity);
    }

    private boolean isTurnOfPlayer(RoundEntity roundEntity, PlayerEntity player) {
        return !roundEntity.getCurrentPlayer().equals(player);
    }

    void atualizarVez(RoundEntity round) {
        List<PlayerEntity> players = round.getPlayerHands().stream()
                .map(PlayerHandEntity::getPlayer)
                .toList();
        int currentIndex = players.indexOf(round.getCurrentPlayer());
        int nextIndex = (currentIndex + 1) % players.size();
        round.setCurrentPlayer(players.get(nextIndex));
    }

    @PreAuthorize("#playerId == authentication.principal.id")
    public BetEntity check(UUID playerId, UUID roundId) {
        checkRoundStatus(roundId);
        PlayerEntity player = playerService.getById(playerId);
        RoundEntity round = roundService.getById(roundId);

        if (isTurnOfPlayer(round, player)) {
            throw new TurnBetException("It's not this player's turn.");
        }

        if (round.getCurrentAmount().compareTo(BigDecimal.ZERO) > 0){
            throw new TurnBetException("You don't check if your opponent bets.");
        }

        if (player.equals(round.getMatch().getPlayer1())){
            atualizarVez(round);
        } else {
            if (communityCardService.revealNextCard(round)){
                concluirRoundShowDown(round);
            }
            round.setCurrentPlayer(round.getMatch().getPlayer1());
        }
        BetEntity betEntity = new BetEntity();
        betEntity.setPlayer(player);
        betEntity.setRound(round);
        return betEntity;
    }

    @PreAuthorize("#playerId == authentication.principal.id")
    public BetEntity fold(UUID playerId, UUID roundId) {
        checkRoundStatus(roundId);
        RoundEntity round = roundRepository.getReferenceById(roundId);
        PlayerEntity player = playerService.getById(playerId);
        atualizarVez(round);
        concluirRound(round.getCurrentPlayer(), player, round);
        BetEntity betEntity = new BetEntity();
        betEntity.setPlayer(player);
        betEntity.setRound(round);
        return betEntity;
    }

    private void checkRoundStatus(UUID roundId) {
        if (!roundRepository.findByStatusEmAndamento(roundId)){
            throw new TurnBetException("This round is already finished.");
        }
    }

    @PreAuthorize("#playerId == authentication.principal.id")
    public BetEntity allIn(UUID playerId, UUID roundId) {
        checkRoundStatus(roundId);
        RoundEntity round = roundRepository.getReferenceById(roundId);
        PlayerEntity player = playerService.getById(playerId);
        BetEntity bet = new BetEntity();
        bet.setValor(player.getFichas());
        bet.setPlayer(player);
        bet.setRound(round);
        bet.setDataCriacao(LocalDateTime.now());
        
        if (isTurnOfPlayer(round, player)) {
            throw new TurnBetException("It's not this player's turn.");
        }
        
        if (round.getCurrentAmount().compareTo(BigDecimal.ZERO) == 0){
            updateRoundAndPlayer(player, bet, round, bet.getValor());
            atualizarVez(round);
        } else {
            if (player.getFichas().compareTo(round.getCurrentAmount()) > 0){
                updateRoundAndPlayer(player, bet, round, bet.getValor());
                atualizarVez(round);
            } else {
                updateAllInPlayers(round, player);
                concluirRoundShowDown(round);
            }
        }

        return betRepository.save(bet);
    }

    void updateAllInPlayers(RoundEntity round, PlayerEntity player) {
        BigDecimal playerFichas = player.getFichas();
        BigDecimal roundCurrentAmount = round.getCurrentAmount();
        BigDecimal valorDiferenca = roundCurrentAmount.subtract(playerFichas);

        player.setFichas(BigDecimal.ZERO);
        playerRepository.save(player);

        round.setPotValue(round.getPotValue().add(playerFichas));

        round.setCurrentAmount(valorDiferenca.max(BigDecimal.ZERO));
        roundRepository.save(round);

        if (valorDiferenca.compareTo(BigDecimal.ZERO) > 0) {
            PlayerEntity otherPlayer = round.getCurrentPlayer().equals(player) ? round.getMatch().getPlayer2() : round.getMatch().getPlayer1();
            otherPlayer.setFichas(otherPlayer.getFichas().add(valorDiferenca));
            playerRepository.save(otherPlayer);
        }

        atualizarVez(round);
    }

    private List<Map<String, String>> convertCards(List<CardEntity> cards) {
        return cards.stream().map(card -> Map.of(
                "valor", card.getRank(),
                "palo", card.getSuit()
        )).collect(Collectors.toList());
    }

    
}

