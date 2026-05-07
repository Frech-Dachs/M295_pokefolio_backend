package ch.leon.troller.M295_pokefolio_backend.card;

import ch.leon.troller.M295_pokefolio_backend.base.MessageResponse;
import ch.leon.troller.M295_pokefolio_backend.storage.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardService {
    private final CardRepository repository;

    public CardService(CardRepository repository) {
        this.repository = repository;
    }

    public List<Card> getCards() {
        return repository.findByOrderByNameAsc();
    }

    public Card getCard(Long id){
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, Card.class));
    }

    public Card insertCard(Card card) {
        return repository.save(card);
    }

    public Card updateCard(Card card, Long id) {
        return repository.findById(id)
                .map(cardOrig -> {
                    cardOrig.setName(card.getName());
                    return repository.save(cardOrig);
                })
                .orElseGet(() -> repository.save(card));
    }

    public MessageResponse deleteCard(Long id) {
        repository.deleteById(id);
        return new MessageResponse("Card " + id + " deleted");
    }
}
