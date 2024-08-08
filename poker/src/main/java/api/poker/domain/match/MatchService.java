package api.poker.domain.match;

import api.poker.application.exceptions.InvalidDataException;
import api.poker.application.exceptions.MatchInProgressException;
import api.poker.application.exceptions.ResourceNotFoundException;
import api.poker.domain.match.dtos.MatchRegisterDto;
import api.poker.domain.match.dtos.MatchUpdateDto;
import api.poker.domain.player.PlayerEntity;
import api.poker.domain.player.PlayerService;
import api.poker.domain.round.RoundEntity;
import api.poker.domain.round.RoundService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final PlayerService playerService;
    private final RoundService roundService;

    @Autowired
    public MatchService(MatchRepository matchRepository, PlayerService playerService, RoundService roundService) {
        this.matchRepository = matchRepository;
        this.playerService = playerService;
        this.roundService = roundService;
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MatchEntity create(MatchRegisterDto matchRegisterDto) {
        MatchEntity matchEntity = new MatchEntity();
        matchEntity.setPlayer1(playerService.getById(matchRegisterDto.getPlayer1()));
        matchEntity.setPlayer2(playerService.getById(matchRegisterDto.getPlayer2()));
        matchEntity.setStatus("Criado");
        matchEntity.setDt_criacao(matchRegisterDto.getDt_criacao());
        return matchRepository.save(matchEntity);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Page<MatchEntity> getAll(Pageable pageable) {
        return matchRepository.findAll(pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Page<MatchEntity> getAllInProgress(Pageable pageable){
        return matchRepository.findAllByStatusEmAndamento(pageable);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public Page<MatchEntity> getAllFinished(Pageable pageable){
        return matchRepository.findAllByStatusFinalizado(pageable);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MatchEntity update(MatchUpdateDto matchUpdateDto) {
        MatchEntity matchEntity = matchRepository.findById(matchUpdateDto.getId()).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + matchUpdateDto.getId()));

        if(!matchRepository.findStatusCriadoById(matchEntity.getId())){
            throw new InvalidDataException("Cannot update players in a match that is already in progress or finished");
        }

        matchEntity.setPlayer1(playerService.getById(matchUpdateDto.getPlayer1()));
        matchEntity.setPlayer2(playerService.getById(matchUpdateDto.getPlayer2()));

        return matchRepository.save(matchEntity);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    public MatchEntity getById(UUID id) {
        return matchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + id));
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void delete(UUID id) {
        MatchEntity matchEntity = matchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + id));
//        if (matchRepository.findByStatusEmAndamento(matchEntity.getId())) {
//            throw new MatchInProgressException("Cannot delete match that is currently in progress");
//        }
        matchRepository.delete(matchEntity);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public MatchEntity startMatch(UUID id){
        MatchEntity matchEntity = matchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + id));
        if (matchEntity.getPlayer1().getFichas().compareTo(BigDecimal.ZERO) == 0 || matchEntity.getPlayer2().getFichas().compareTo(BigDecimal.ZERO) == 0){
            throw new InvalidDataException("You cannot create a game with a player with zero chips.");
        }
        matchEntity.setStatus("Em andamento");
        return matchRepository.save(matchEntity);
    }

    public void continueMatch(RoundEntity round){
        MatchEntity matchEntity = round.getMatch();
        if (matchEntity.getPlayer1().getFichas().compareTo(BigDecimal.ZERO) == 0){
            updateMatchAndRound(matchEntity, matchEntity.getPlayer2(), matchEntity.getPlayer1());
        } else if (matchEntity.getPlayer2().getFichas().compareTo(BigDecimal.ZERO) == 0){
            updateMatchAndRound(matchEntity, matchEntity.getPlayer1(), matchEntity.getPlayer2());
        } else {
            roundService.create(matchEntity);
        }
    }

    private void updateMatchAndRound(MatchEntity matchEntity, PlayerEntity playerWinner, PlayerEntity playerLoser) {
        matchEntity.setPlayerWinner(playerWinner);
        matchEntity.setPlayerLoser(playerLoser);
        matchEntity.setStatus("Finalizado");
        matchRepository.save(matchEntity);
    }

    public MatchEntity getByIdFinished(UUID id) {
        return matchRepository.findMatchByIdFinalizado(id).orElseThrow(() -> new ResourceNotFoundException("Match not found with id " + id));
    }
}
