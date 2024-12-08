package swimclub.controllers;

import swimclub.models.Role;
import swimclub.models.User;
import swimclub.services.AuthService;
import swimclub.utilities.Validator;

public class AdminController {
    private final AuthService authService;

    public AdminController(AuthService authService) {
        this.authService = authService;
    }

    // Method to login
    public User login(String username, String password) {
        User user = authService.authenticate(username, password);
        if (user == null) {
            throw new IllegalArgumentException("Invalid username or password.");
        }
        return user;
    }

    // Register a new user
    public void register(String username, String password, Role role) {
        Validator.validateUsername(username); // Validate username
        Validator.validatePassword(password); // Validate password
        Validator.validateRole(role); // Validate role

        authService.registerUser(username, password, role);
    }

    // Method to add a new user (only if logged-in user is an Admin)
    public void addUser(String loggedInUsername, String loggedInPassword, String username, String password, Role role) {
        User adminUser = authService.authenticate(loggedInUsername, loggedInPassword);  // Ensure admin is authenticated
        if (adminUser == null || !adminUser.getRole().equals(Role.CHAIRMAN)) {
            throw new IllegalArgumentException("Only an Admin can add users.");
        }

        // Admin authenticated, proceed to add user
        Validator.validateUsername(username); // Validate username
        Validator.validatePassword(password); // Validate password
        Validator.validateRole(role); // Validate role

        authService.registerUser(username, password, role);
    }
}
