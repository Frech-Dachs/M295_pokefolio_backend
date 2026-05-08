package ch.leon.troller.M295_pokefolio_backend.cardinstance;

import ch.leon.troller.M295_pokefolio_backend.base.MessageResponse;
import ch.leon.troller.M295_pokefolio_backend.card.Card;
import ch.leon.troller.M295_pokefolio_backend.card.CardRepository;
import ch.leon.troller.M295_pokefolio_backend.collection.Collection;
import ch.leon.troller.M295_pokefolio_backend.collection.CollectionRepository;
import ch.leon.troller.M295_pokefolio_backend.storage.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CardInstanceService {

    private final CardInstanceRepository cardInstanceRepository;
    private final CardRepository cardRepository;
    private final CollectionRepository collectionRepository;

    public CardInstanceService(CardInstanceRepository cardInstanceRepository,
                               CardRepository cardRepository,
                               CollectionRepository collectionRepository) {
        this.cardInstanceRepository = cardInstanceRepository;
        this.cardRepository = cardRepository;
        this.collectionRepository = collectionRepository;
    }

    public List<CardInstance> getByCollection(Long collectionId) {
        return cardInstanceRepository.findByCollectionId(collectionId);
    }

    public CardInstance getById(Long id) {
        return cardInstanceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, CardInstance.class));
    }

    public CardInstance addToCollection(Long cardId, Long collectionId, CardInstance cardInstance, String username) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException(collectionId, Collection.class));

        if (!collection.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your collection");
        }

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException(cardId, Card.class));

        cardInstance.setCard(card);
        cardInstance.setCollection(collection);
        return cardInstanceRepository.save(cardInstance);
    }

    public CardInstance addToCollectionAdmin(Long cardId, Long collectionId, CardInstance cardInstance) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new EntityNotFoundException(collectionId, Collection.class));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException(cardId, Card.class));

        cardInstance.setCard(card);
        cardInstance.setCollection(collection);
        return cardInstanceRepository.save(cardInstance);
    }

    public CardInstance updateCardInstance(Long id, CardInstance updated, String username) {
        CardInstance existing = getById(id);

        if (!existing.getCollection().getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your collection");
        }

        existing.setCondition(updated.getCondition());
        existing.setQuantity(updated.getQuantity());
        return cardInstanceRepository.save(existing);
    }

    public CardInstance updateCardInstanceAdmin(Long id, CardInstance updated) {
        CardInstance existing = getById(id);
        existing.setCondition(updated.getCondition());
        existing.setQuantity(updated.getQuantity());
        return cardInstanceRepository.save(existing);
    }

    public MessageResponse deleteCardInstance(Long id, String username) {
        CardInstance instance = getById(id);

        if (!instance.getCollection().getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your collection");
        }

        cardInstanceRepository.delete(instance);
        return new MessageResponse("CardInstance " + id + " deleted");
    }

    public MessageResponse deleteCardInstanceAdmin(Long id) {
        CardInstance instance = getById(id);
        cardInstanceRepository.delete(instance);
        return new MessageResponse("CardInstance " + id + " deleted");
    }
}