package swimclub.services;

import swimclub.models.Role;
import swimclub.models.User;
import swimclub.repositories.AuthRepository;
import swimclub.utilities.PasswordUtils;

/**
 * Service class for handling authentication and user management.
 */
public class AuthService {
    private final AuthRepository authRepository;

    /**
     * Constructor to initialize the AuthService.
     *
     * @param authRepository The repository for managing users.
     */
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Authenticates a user based on username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The authenticated User object.
     * @throws IllegalArgumentException if authentication fails.
     */
    public User authenticate(String username, String password) {
        User user = authRepository.authenticate(username, password);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        return user;
    }

    /**
     * Registers a new user.
     *
     * @param username The username for the new user.
     * @param password The password for the new user.
     * @param role     The role for the new user.
     * @throws IllegalArgumentException if the username is already taken.
     */
    public void registerUser(String username, String password, Role role) {
        if (authRepository.getUserByUsername(username) != null) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        // Generate a salt and hashed password
        String salt = PasswordUtils.generateSalt();
        String hashedPassword = PasswordUtils.hashPassword(password, salt);

        // Pass all required arguments to the User constructor
        User newUser = new User(username, hashedPassword, salt, role);

        // Add the user to the repository
        authRepository.addUser(newUser);
    }
}