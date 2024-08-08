package api.poker;

import api.poker.domain.bet.BetEntity;
import api.poker.domain.bet.dtos.BetAllInDto;
import api.poker.domain.bet.dtos.BetCheckDetailDto;
import api.poker.domain.bet.dtos.BetDetailDto;
import api.poker.domain.bet.dtos.BetFoldDetailDto;
import api.poker.domain.communitycard.CommunityCardEntity;
import api.poker.domain.communitycard.dtos.CommunityCardDetailDto;
import api.poker.domain.match.MatchEntity;
import api.poker.domain.match.dtos.MatchDetailDto;
import api.poker.domain.match.dtos.MatchFinishedDetailDto;
import api.poker.domain.match.dtos.MatchPlayerWinnerDetailDto;
import api.poker.domain.playerhand.PlayerHandEntity;
import api.poker.domain.playerhand.dtos.PlayerHandDetailDto;
import api.poker.domain.round.RoundEntity;
import api.poker.domain.round.dtos.RoundDetailDto;
import api.poker.domain.round.dtos.RoundDetailWinnerPlayerDto;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ModelMapperConfig {

    PropertyMap<MatchEntity, MatchDetailDto> convertIdToEntityMatchPlayer = new PropertyMap<>() {
        @Override
        protected void configure() {
            map(source.getPlayer1()).setPlayer1(null);
            map(source.getPlayer2()).setPlayer2(null);
        }
    };

    PropertyMap<RoundEntity, RoundDetailDto> convertIdToEntityRoundMatch = new PropertyMap<>() {
        @Override
        protected void configure() {
            map(source.getMatch()).setMatch(null);
        }
    };

    PropertyMap<PlayerHandEntity, PlayerHandDetailDto> convertPlayerHandEntity = new PropertyMap<>() {
        @Override
        protected void configure() {
            map(source.getCard1()).setCard1(null);
            map(source.getCard2()).setCard2(null);
        }
    };

    Converter<LocalDateTime, String> localDateTimeToStringConverter =
            context -> context.getSource() == null ? null : context.getSource().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);

    PropertyMap<CommunityCardEntity, CommunityCardDetailDto> convertCommunityCardEntity = new PropertyMap<>() {
        @Override
        protected void configure() {
            map(source.getCard()).setCard(null);
        }
    };

    PropertyMap<BetEntity, BetDetailDto> convertBetEntity = new PropertyMap<>() {
        @Override
        protected void configure() {
            map(source.getPlayer().getNome()).setNomePlayer(null);
            map(source.getValor()).setValor(null);
            map(source.getRound().getStatus()).setStatusRound(null);
        }
    };

    PropertyMap<BetEntity, BetCheckDetailDto> convertCheckEntity = new PropertyMap<>() {
        @Override
        protected void configure() {
            map(source.getPlayer().getNome()).setNomePlayer(null);
            map(source.getRound().getStatus()).setStatusRound(null);
            map(source.getPlayer().getId()).setIdPlayer(null);
        }
    };

    PropertyMap<BetEntity, BetFoldDetailDto> convertFoldEntity = new PropertyMap<>() {
        @Override
        protected void configure() {
            map(source.getPlayer().getNome()).setNomePlayer(null);
            map(source.getRound().getStatus()).setStatusRound(null);
            map(source.getPlayer().getId()).setIdPlayer(null);
        }
    };

    PropertyMap<RoundEntity, RoundDetailWinnerPlayerDto> convertWinnerPlayerRound = new PropertyMap<>() {
        @Override
        protected void configure() {
            map(source.getWinPlayer()).setPlayerWinner(null);
            map(source.getPotValue()).setEarnedValue(null);
            map(source.getLoserPlayer()).setPlayerLoser(null);
        }
    };

    PropertyMap<BetEntity, BetAllInDto> convertAllInEntity = new PropertyMap<>() {
        @Override
        protected void configure() {
            map(source.getPlayer().getNome()).setNomePlayer(null);
            map(source.getRound().getStatus()).setStatusRound(null);
            map(source.getValor()).setValor(null);
            map(source.getPlayer().getId()).setIdPlayer(null);
        }
    };

    PropertyMap<MatchEntity, MatchPlayerWinnerDetailDto> convertMatchPlayerWinner = new PropertyMap<>() {
        @Override
        protected void configure() {
            map(source.getPlayer1()).setPlayer1(null);
            map(source.getPlayer2()).setPlayer2(null);
            map(source.getPlayerWinner()).setPlayerWinner(null);
            map(source.getPlayerLoser()).setPlayerLoser(null);
        }
    };

    PropertyMap<MatchEntity, MatchFinishedDetailDto> convertIdToEntityMatchPlayerWinnerAndLoser = new PropertyMap<>() {
        @Override
        protected void configure() {
            map(source.getPlayerWinner()).setPlayerWinner(null);
            map(source.getPlayerLoser()).setPlayerLoser(null);
        }
    };

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setPropertyCondition(context -> context.getSource() != null);
        mapper.addMappings(convertIdToEntityMatchPlayer);
        mapper.addMappings(convertIdToEntityRoundMatch);
        mapper.addMappings(convertPlayerHandEntity);
        mapper.addMappings(convertCommunityCardEntity);
        mapper.addConverter(localDateTimeToStringConverter);
        mapper.addMappings(convertBetEntity);
        mapper.addMappings(convertCheckEntity);
        mapper.addMappings(convertFoldEntity);
        mapper.addMappings(convertWinnerPlayerRound);
        mapper.addMappings(convertAllInEntity);
        mapper.addMappings(convertMatchPlayerWinner);
        mapper.addMappings(convertIdToEntityMatchPlayerWinnerAndLoser);
        return mapper;
    }
}

