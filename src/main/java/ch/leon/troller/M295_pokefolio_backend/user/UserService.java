package ch.leon.troller.M295_pokefolio_backend.user;

public class UserService {

    public final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getOrCreateUser(String username) {
        return userRepository.findByUsername(username)
                .orElseGet(() -> userRepository.save(new User(username)));
    }
}
