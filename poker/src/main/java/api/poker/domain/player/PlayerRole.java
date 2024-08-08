package api.poker.domain.player;

import lombok.Getter;

@Getter
public enum PlayerRole {
    ADMIN("admin"),
    USER("user");

    private final String role;
    PlayerRole(String role) {
        this.role = role;
    }

}
