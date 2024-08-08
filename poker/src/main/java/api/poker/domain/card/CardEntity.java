package api.poker.domain.card;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity(name = "Card")
@Getter
@EqualsAndHashCode(of = "id")
@Table(name = "TABLE_CARDS")
public class CardEntity {

    @Id
    private String id;
    private String rank;
    private String suit;
    @Column(name = "rank_value")
    private Integer rankValue;

}
