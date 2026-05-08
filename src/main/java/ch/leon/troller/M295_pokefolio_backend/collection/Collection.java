package ch.leon.troller.M295_pokefolio_backend.collection;

import ch.leon.troller.M295_pokefolio_backend.card.Card;
import ch.leon.troller.M295_pokefolio_backend.card.CardType;
import ch.leon.troller.M295_pokefolio_backend.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;

    @OneToMany
    @JoinColumn(name = "collection_id")
    private List<Card> cards;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
