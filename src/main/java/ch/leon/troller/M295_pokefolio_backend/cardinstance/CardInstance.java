package ch.leon.troller.M295_pokefolio_backend.cardinstance;

import ch.leon.troller.M295_pokefolio_backend.card.Card;
import ch.leon.troller.M295_pokefolio_backend.collection.Collection;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class CardInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(optional = false)
    @JoinColumn(name = "collection_id", nullable = false)
    private Collection collection;

    private String condition;

    private int quantity = 1;
}