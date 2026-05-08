package ch.leon.troller.M295_pokefolio_backend.collection;

import ch.leon.troller.M295_pokefolio_backend.base.MessageResponse;
import ch.leon.troller.M295_pokefolio_backend.card.Card;
import ch.leon.troller.M295_pokefolio_backend.card.CardRepository;
import ch.leon.troller.M295_pokefolio_backend.storage.EntityNotFoundException;
import ch.leon.troller.M295_pokefolio_backend.user.User;
import ch.leon.troller.M295_pokefolio_backend.user.UserRepository;
import ch.leon.troller.M295_pokefolio_backend.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final CardRepository cardRepository;
    private final UserService userService;

    public CollectionService(CollectionRepository collectionRepository, CardRepository cardRepository, UserService userService) {
        this.collectionRepository = collectionRepository;

        this.cardRepository = cardRepository;
        this.userService = userService;
    }

    public List<Collection> getCollections() {
        return collectionRepository.findByOrderByNameAsc();
    }

    public Collection getCollection(Long id) {
        return collectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Collection.class));
    }

    public Collection createCollection(String username, Collection collection) {
        User user = userService.getOrCreateUser(username);
        collection.setUser(user);
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
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        collectionRepository.delete(collection);

        return new MessageResponse("Collection " + id + " deleted");
    }

    public Collection addCard(Long cardId, Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId).orElseThrow(() -> new EntityNotFoundException(collectionId, Collection.class));;
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new EntityNotFoundException(cardId, Card.class));
        List<Card> cards = collection.getCards();

        cards.add(card);
        collection.setCards(cards);
        return collectionRepository.save(collection);
    }


    public List<Collection> getMyCollections(String username) {
        User user = userService.getOrCreateUser(username);
        return collectionRepository.findByUser(user);
    }

    public Collection getCollection(Long id, String username) {
        Collection collection = collectionRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Collection not found"));

        if (!collection.getUser().getUsername().equals(username) && !username.equals("admin")) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your collection");
        }

        return collection;
    }
}