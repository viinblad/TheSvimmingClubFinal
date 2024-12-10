package swimclub.services;

import swimclub.models.Role;
import swimclub.models.User;
import swimclub.repositories.AuthRepository;
import swimclub.utilities.PasswordUtils;

/**
 * Service class for handling authentication and user management.
 * Provides methods for authenticating users, registering new users, and managing roles.
 */
public class AuthService {
    private final AuthRepository authRepository;
    private String loggedInUsername;
    private String loggedInPassword;

    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Authenticates a user based on their username and password.
     * Verifies that the user exists in the repository and the password is correct.
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

        // Store the logged-in user's details
        loggedInUsername = username;
        loggedInPassword = password;

        return user;
    }

    /**
     * Registers a new user with the given username, password, and role.
     * This method generates a salt and hashes the password before saving the user.
     *
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @param role     The role to be assigned to the new user (e.g., ADMIN, COACH, etc.).
     * @throws IllegalArgumentException if the username is already taken.
     */
    public void registerUser(String username, String password, Role role) {
        // Check if the username already exists
        if (authRepository.getUserByUsername(username) != null) {
            throw new IllegalArgumentException("Username is already taken.");
        }

        // Generate a salt and hashed password
        String salt = PasswordUtils.generateSalt();
        String hashedPassword = PasswordUtils.hashPassword(password, salt);

        // Create the new user object with the hashed password and salt
        User newUser = new User(username, hashedPassword, salt, role);

        // Add the user to the repository
        authRepository.addUser(newUser);
    }

    /**
     * Updates an existing user's password and/or role.
     * If a new password is provided, it will be hashed and saved.
     * If a new role is selected, it will be updated.
     *
     * @param username    The username of the user to update.
     * @param newPassword The new password for the user (or null to keep the current password).
     * @param newRole     The new role for the user (or null to keep the current role).
     * @throws IllegalArgumentException if the user doesn't exist.
     */
    public void updateUser(String username, String newPassword, Role newRole) {
        User user = authRepository.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        // Update password if a new one is provided
        if (newPassword != null && !newPassword.isEmpty()) {
            String salt = PasswordUtils.generateSalt();
            String hashedPassword = PasswordUtils.hashPassword(newPassword, salt);
            user = new User(username, hashedPassword, salt, user.getRole()); // Create a new User with updated password
        }

        // Update role if a new one is provided
        if (newRole != null) {
            user = new User(user.getUsername(), user.getHashedPassword(), user.getSalt(), newRole); // Update the role
        }

        // Save updated user information in the repository
        authRepository.saveUsers();
    }

    /**
     * Deletes a user from the repository by their username.
     *
     * @param username The username of the user to delete.
     */
    public void deleteUser(String username) {
        User user = authRepository.getUserByUsername(username);
        if (user == null) {
            throw new IllegalArgumentException("User not found.");
        }

        // Remove the user from the repository
        authRepository.deleteUser(username); // Call the repository method to delete the user
        System.out.println("User '" + username + "' has been deleted.");
    }

    // Getter for logged-in username
    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    // Getter for logged-in password
    public String getLoggedInPassword() {
        return loggedInPassword;
    }
}
