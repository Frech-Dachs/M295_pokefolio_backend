package ch.leon.troller.M295_pokefolio_backend;

import ch.leon.troller.M295_pokefolio_backend.base.MessageResponse;
import ch.leon.troller.M295_pokefolio_backend.card.Card;
import ch.leon.troller.M295_pokefolio_backend.card.CardController;
import ch.leon.troller.M295_pokefolio_backend.card.CardService;
import ch.leon.troller.M295_pokefolio_backend.card.CardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardControllerTest {

    @Mock
    private CardService cardService;

    private CardController cardController;

    @BeforeEach
    void setUp() {
        cardController = new CardController(cardService);
    }

    @Test
    void all_shouldReturnAllCards() {
        Card card = card("Pikachu");
        when(cardService.getCards()).thenReturn(List.of(card));

        var response = cardController.all();

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getFirst().getName()).isEqualTo("Pikachu");
    }

    @Test
    void cardId_shouldReturnCardById() {
        Card saved = card("Bulbasaur");
        saved.setId(1L);
        when(cardService.getCard(1L)).thenReturn(saved);

        var response = cardController.one(1L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        var body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getName()).isEqualTo("Bulbasaur");
    }

    @Test
    void newCard_shouldCreateCard() {
        Card requestCard = card("Charmander");
        Card savedCard = card("Charmander");
        savedCard.setId(2L);
        when(cardService.insertCard(any(Card.class))).thenReturn(savedCard);

        var response = cardController.create(requestCard);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        var body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getName()).isEqualTo("Charmander");
    }

    @Test
    void updateCard_shouldUpdateCardById() {
        Card requestCard = card("Raichu");
        requestCard.setHp(90);
        Card updatedCard = card("Raichu");
        updatedCard.setId(3L);
        updatedCard.setHp(90);
        when(cardService.updateCard(any(Card.class), eq(3L))).thenReturn(updatedCard);

        var response = cardController.update(requestCard, 3L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        var body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getName()).isEqualTo("Raichu");
        assertThat(body.getHp()).isEqualTo(90);
    }

    @Test
    void deleteCard_shouldDeleteCardById() {
        when(cardService.deleteCard(4L)).thenReturn(new MessageResponse("Card 4 deleted"));

        var response = cardController.delete(4L);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        var body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getMessage()).isEqualTo("Card 4 deleted");
    }

    private static Card card(String name) {
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
