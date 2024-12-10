package swimclub.controllers;

import swimclub.models.Role;
import swimclub.models.User;
import swimclub.services.AuthService;
import swimclub.utilities.Validator;
import swimclub.repositories.UserRepository;
import swimclub.repositories.AuthRepository;

import java.util.List;

/**
 * Controller for managing admin functionalities such as login, user registration, user management, etc.
 */
public class AdminController {
    private final AuthService authService;
    private final UserRepository userRepository;
    private String loggedInUsername;
    private String loggedInPassword;

    public AdminController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    /**
     * Authenticates a user based on their username and password.
     *
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The authenticated User object.
     * @throws IllegalArgumentException if authentication fails.
     */
    public User login(String username, String password) {
        // Authenticate the user
        User user = authService.authenticate(username, password);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password.");
        }

        // Store the logged-in user's details for future reference
        loggedInUsername = username;
        loggedInPassword = password;

        return user;
    }

    /**
     * Registers a new user in the system with the given username, password, and role.
     *
     * @param username The username for the new user.
     * @param password The password for the new user.
     * @param role     The role for the new user.
     */
    public void register(String username, String password, Role role) {
        // Validate input for the new user
        Validator.validateUsername(username); // Validate username
        Validator.validatePassword(password); // Validate password
        Validator.validateRole(role); // Validate role

        // Register the user
        authService.registerUser(username, password, role);
    }

    /**
     * Adds a new user to the system. Only an Admin user can perform this operation.
     *
     * @param loggedInUsername The username of the logged-in admin.
     * @param loggedInPassword The password of the logged-in admin.
     * @param username         The username of the new user to add.
     * @param password         The password of the new user.
     * @param role             The role of the new user.
     * @throws IllegalArgumentException if the logged-in user is not an admin or if the user already exists.
     */
    public void addUser(String loggedInUsername, String loggedInPassword, String username, String password, Role role) {
        // Authenticate the logged-in admin user
        User adminUser = authService.authenticate(loggedInUsername, loggedInPassword);
        if (adminUser == null || !adminUser.getRole().equals(Role.ADMIN)) {
            throw new IllegalArgumentException("Only an Admin can add users.");
        }

        // Validate input for the new user
        Validator.validateUsername(username); // Validate username
        Validator.validatePassword(password); // Validate password
        Validator.validateRole(role); // Validate role

        // Register the new user
        authService.registerUser(username, password, role);
    }

    /**
     * Updates an existing user's details (password or role). Only an Admin can perform this operation.
     *
     * @param loggedInUsername The username of the logged-in admin.
     * @param loggedInPassword The password of the logged-in admin.
     * @param username         The username of the user to update.
     * @param newPassword      The new password for the user (optional).
     * @param newRole          The new role for the user (optional).
     * @throws IllegalArgumentException if the logged-in user is not an admin or if the user does not exist.
     */
    public void updateUser(String loggedInUsername, String loggedInPassword, String username, String newPassword, Role newRole) {
        // Authenticate the logged-in admin user
        User adminUser = authService.authenticate(loggedInUsername, loggedInPassword);
        if (adminUser == null || !adminUser.getRole().equals(Role.ADMIN)) {
            throw new IllegalArgumentException("Only an Admin can update users.");
        }

        // Validate username
        Validator.validateUsername(username);

        // Update user details
        authService.updateUser(username, newPassword, newRole);
    }

    /**
     * Deletes an existing user. Only an Admin can perform this operation.
     *
     * @param loggedInUsername The username of the logged-in admin.
     * @param loggedInPassword The password of the logged-in admin.
     * @param username         The username of the user to delete.
     * @throws IllegalArgumentException if the logged-in user is not an admin or if the user does not exist.
     */
    public void deleteUser(String loggedInUsername, String loggedInPassword, String username) {
        // Authenticate the logged-in admin user
        User adminUser = authService.authenticate(loggedInUsername, loggedInPassword);
        if (adminUser == null || !adminUser.getRole().equals(Role.ADMIN)) {
            throw new IllegalArgumentException("Only an Admin can delete users.");
        }

        // Validate username
        Validator.validateUsername(username);

        // Delete the user from the repository
        userRepository.deleteUser(username);
    }

    /**
     * Lists all users in the system.
     *
     * @return A list of all users.
     */
    public void listUsers() {
        // Load all users from the repository and display them
        for (User user : userRepository.loadUsers()) {
            System.out.println("Username: " + user.getUsername() + ", Role: " + user.getRole());
        }
    }

    /**
     * Fetches a user by their username.
     *
     * @param username The username of the user to fetch.
     * @return The user object if found, null otherwise.
     */
    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    /**
     * Returns the currently logged-in username.
     *
     * @return The logged-in username.
     */
    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    /**
     * Returns the currently logged-in password.
     *
     * @return The logged-in password.
     */
    public String getLoggedInPassword() {
        return loggedInPassword;
    }
}
