package swimclub.repositories;

import swimclub.models.Role;
import swimclub.models.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Repository class for managing authentication and authorization.
 */
public class AuthRepository {
    private final Map<String, User> userDatabase; // In-memory storage for users (username -> User)

    /**
     * Constructor to initialize the AuthRepository.
     */
    public AuthRepository() {
        this.userDatabase = new HashMap<>();
        loadDefaultUsers(); // Optionally load some default users
    }

    /**
     * Loads default users into the repository for demonstration or testing purposes.
     */
    private void loadDefaultUsers() {
        addUser(new User("chairman", "password123", Role.CHAIRMAN));
        addUser(new User("treasurer", "password123", Role.TREASURER));
        addUser(new User("coach", "password123", Role.COACH));
    }

    /**
     * Adds a new user to the repository.
     *
     * @param user The user to add.
     */
    public void addUser(User user) {
        userDatabase.put(user.getUsername(), user);
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username of the user.
     * @return The User object if found, or null if not found.
     */
    public User getUserByUsername(String username) {
        return userDatabase.get(username);
    }

    /**
     * Authenticates a user by their username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The authenticated User object, or null if authentication fails.
     */
    public User authenticate(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
