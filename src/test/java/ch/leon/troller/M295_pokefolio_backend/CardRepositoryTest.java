package ch.leon.troller.M295_pokefolio_backend;

import ch.leon.troller.M295_pokefolio_backend.card.Card;
import ch.leon.troller.M295_pokefolio_backend.card.CardRepository;
import ch.leon.troller.M295_pokefolio_backend.card.CardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Rollback
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:pokefolio-test;MODE=PostgreSQL;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @BeforeEach
    void cleanDatabase() {
        cardRepository.deleteAll();
    }

    @Test
    void create_shouldPersistCard() {
        Card card = card("Pikachu");

        Card saved = cardRepository.saveAndFlush(card);

        assertThat(saved.getId()).isNotNull();
        assertThat(cardRepository.findAll()).hasSize(1);
    }

    @Test
    void read_shouldFindCardByIdAndOrderedQuery() {
        Card saved = cardRepository.saveAndFlush(card("Bulbasaur"));

        Optional<Card> byId = cardRepository.findById(saved.getId());

        assertThat(byId).isPresent();
        assertThat(byId.get().getName()).isEqualTo("Bulbasaur");

        List<Card> sorted = cardRepository.findByOrderByNameAsc();
        assertThat(sorted).hasSize(1);
        assertThat(sorted.getFirst().getName()).isEqualTo("Bulbasaur");
    }

    @Test
    void read_shouldReturnCardsAlphabeticallySorted() {
        cardRepository.saveAndFlush(card("Pikachu"));
        cardRepository.saveAndFlush(card("Bulbasaur"));
        cardRepository.saveAndFlush(card("Charmander"));

        List<Card> sorted = cardRepository.findByOrderByNameAsc();

        assertThat(sorted).hasSize(3);
        assertThat(sorted.get(0).getName()).isEqualTo("Bulbasaur");
        assertThat(sorted.get(1).getName()).isEqualTo("Charmander");
        assertThat(sorted.get(2).getName()).isEqualTo("Pikachu");
    }

    @Test
    void update_shouldModifyExistingCard() {
        Card saved = cardRepository.saveAndFlush(card("Raichu"));

        saved.setName("Raichu-Alola");
        saved.setHp(110);
        cardRepository.saveAndFlush(saved);

        Card updated = cardRepository.findById(saved.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Raichu-Alola");
        assertThat(updated.getHp()).isEqualTo(110);
    }

    @Test
    void delete_shouldRemoveCard() {
        Card saved = cardRepository.saveAndFlush(card("Mewtwo"));

        cardRepository.deleteById(saved.getId());
        cardRepository.flush();

        assertThat(cardRepository.findById(saved.getId())).isEmpty();
    }

    private Card card(String name) {
        Card card = new Card();
        card.setName(name);
        card.setCardType(CardType.POKEMON);
        card.setType("Electric");
        card.setHp(60);
        card.setRarity("Common");
        card.setSetName("Base Set");
        card.setCardNumber("58/102");
        card.setImageUrl("https://images.pokemontcg.io/base1/58.png");
        return card;
    }
}
