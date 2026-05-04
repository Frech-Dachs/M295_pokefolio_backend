package ch.leon.troller.M295_pokefolio_backend.card;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CardType cardType;

    @Column(nullable = false)
    @NotEmpty
    private String name;

    @Column(nullable = false)
    @NotEmpty
    private String type;

    private int hp;

    @Column(nullable = false)
    @NotEmpty
    private String rarity;

    @Column(nullable = false)
    @NotEmpty
    private String setName;

    @Column(nullable = false)
    @NotEmpty
    private String cardNumber;

    @Column(nullable = false)
    @NotEmpty
    private String imageUrl;
}
