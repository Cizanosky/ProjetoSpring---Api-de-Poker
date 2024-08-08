package api.poker.domain.player.validation;

import api.poker.application.exceptions.InvalidDataException;
import api.poker.domain.player.PlayerEntity;
import api.poker.domain.player.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailValidator implements PlayerCreationValidator{
    @Autowired
    private PlayerRepository playerRepository;


    @Override
    public void validate(PlayerEntity playerEntity) {
        if (playerRepository.existsByEmail(playerEntity.getEmail())){
            throw new InvalidDataException("Already used email");
        }
    }
}
