package api.poker.domain.player;

import api.poker.domain.match.MatchEntity;
import api.poker.domain.round.RoundEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity(name = "Player")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Table(name = "TABLE_PLAYERS")
public class PlayerEntity implements UserDetails {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private BigDecimal fichas;
    private LocalDateTime dt_criacao;

    @Enumerated(EnumType.STRING)
    private PlayerRole role;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "player1", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MatchEntity> matches1;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToMany(mappedBy = "player2", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MatchEntity> matches2;

    @OneToMany(mappedBy = "winPlayer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoundEntity> roundsWinner;

    @OneToMany(mappedBy = "loserPlayer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoundEntity> roundsLosers;

    @OneToMany(mappedBy = "playerWinner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MatchEntity> matchesWinner;

    @OneToMany(mappedBy = "playerLoser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MatchEntity> matchesLosers;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == PlayerRole.ADMIN) {
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));

    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }
}
