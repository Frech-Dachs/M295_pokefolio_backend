package ch.leon.troller.M295_pokefolio_backend.collection;

import ch.leon.troller.M295_pokefolio_backend.base.MessageResponse;
import ch.leon.troller.M295_pokefolio_backend.storage.EntityNotFoundException;
import ch.leon.troller.M295_pokefolio_backend.user.User;
import ch.leon.troller.M295_pokefolio_backend.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CollectionService {

    private final CollectionRepository collectionRepository;
    private final UserService userService;

    public CollectionService(CollectionRepository collectionRepository, UserService userService) {
        this.collectionRepository = collectionRepository;
        this.userService = userService;
    }

    private Collection findById(Long id) {
        return collectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, Collection.class));
    }

    private void checkOwner(Collection collection, String username) {
        if (!collection.getUser().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your collection");
        }
    }

    public List<Collection> getMyCollections(String username) {
        User user = userService.getOrCreateUser(username);
        return collectionRepository.findByUser(user);
    }

    public Collection getCollection(Long id, String username) {
        Collection collection = findById(id);
        checkOwner(collection, username);
        return collection;
    }

    // Admin
    public Collection getCollectionAdmin(Long id) {
        return findById(id);
    }

    public Collection createCollection(String username, Collection collection) {
        User user = userService.getOrCreateUser(username);
        collection.setUser(user);
        return collectionRepository.save(collection);
    }

    public Collection updateCollection(Collection collection, Long id, String username) {
        Collection orig = findById(id);
        checkOwner(orig, username);
        orig.setName(collection.getName());
        return collectionRepository.save(orig);
    }

    // Admin
    public Collection updateCollectionAdmin(Collection collection, Long id) {
        Collection orig = findById(id);
        orig.setName(collection.getName());
        return collectionRepository.save(orig);
    }

    public MessageResponse deleteCollection(Long id, String username) {
        Collection collection = findById(id);
        checkOwner(collection, username);
        collectionRepository.delete(collection);
        return new MessageResponse("Collection " + id + " deleted");
    }

    // Admin
    public MessageResponse deleteCollectionAdmin(Long id) {
        Collection collection = findById(id);
        collectionRepository.delete(collection);
        return new MessageResponse("Collection " + id + " deleted");
    }
}