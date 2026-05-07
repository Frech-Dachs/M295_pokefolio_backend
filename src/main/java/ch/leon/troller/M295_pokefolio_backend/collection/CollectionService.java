package ch.leon.troller.M295_pokefolio_backend.collection;

import ch.leon.troller.M295_pokefolio_backend.base.MessageResponse;
import ch.leon.troller.M295_pokefolio_backend.card.Card;
import ch.leon.troller.M295_pokefolio_backend.card.CardRepository;
import ch.leon.troller.M295_pokefolio_backend.storage.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final CardRepository cardRepository;

    public CollectionService(CollectionRepository collectionRepository, CardRepository cardRepository) {
        this.collectionRepository = collectionRepository;

        this.cardRepository = cardRepository;
    }

    public List<Collection> getCollections() {
        return collectionRepository.findByOrderByNameAsc();
    }

    public Collection getCollection(Long id) {
        return collectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Collection.class));
    }

    public Collection insertCollection(Collection collection) {
        return collectionRepository.save(collection);
    }

    public Collection updateCollection(Collection collection, Long id) {
        return collectionRepository.findById(id)
                .map(collectionOrig -> {
                    collectionOrig.setName(collection.getName());
                    collectionOrig.setCards(collection.getCards());
                    return collectionRepository.save(collectionOrig);
                })
                .orElseThrow(() -> new EntityNotFoundException(id, Collection.class));
    }

    public MessageResponse deleteCollection(Long id) {
        collectionRepository.deleteById(id);
        return new MessageResponse("Collection " + id + " deleted");
    }

    public Collection addCard(Long cardId, Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId).orElseThrow(() -> new EntityNotFoundException(collectionId, Collection.class));;
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new EntityNotFoundException(cardId, Card.class));
        List<Card> cards = collection.getCards();


        // überprüfung dazuuuu machen das nichtb 2 gleiche karten drinn sinnddd¨!!!!!
        cards.add(card);
        collection.setCards(cards);
        return collectionRepository.save(collection);
    }
}