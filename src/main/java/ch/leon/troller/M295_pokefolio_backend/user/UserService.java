package ch.leon.troller.M295_pokefolio_backend.user;

import ch.leon.troller.M295_pokefolio_backend.base.MessageResponse;
import ch.leon.troller.M295_pokefolio_backend.collection.CollectionRepository;
import ch.leon.troller.M295_pokefolio_backend.storage.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CollectionRepository collectionRepository;

    public UserService(UserRepository userRepository, CollectionRepository collectionRepository) {
        this.userRepository = userRepository;
        this.collectionRepository = collectionRepository;
    }

    public User getOrCreateUser(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.save(new User(username)));
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, User.class));
    }

    public User updateUser(Long id, User updated) {
        User orig = getUser(id);
        orig.setUsername(updated.getUsername());
        return userRepository.save(orig);
    }

    public MessageResponse deleteUser(Long id) {
        User user = getUser(id);
        collectionRepository.deleteAll(collectionRepository.findByUser(user));
        userRepository.delete(user);
        return new MessageResponse("User " + id + " deleted");
    }
}
