package swimclub.repositories;

import swimclub.models.Role;
import swimclub.models.User;
import swimclub.utilities.PasswordUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Repository class responsible for handling authentication and user management.
 * This class stores users in memory and persists them to a file. It provides methods for
 * adding users, authenticating users, and loading/saving the user database.
 */
public class AuthRepository {
    private final Map<String, User> userDatabase; // In-memory storage for users
    private final String filePath; // Path to the .dat file for storing users

    /**
     * Constructor for AuthRepository.
     * Initializes the repository by loading existing users from the provided file path.
     * If no users exist with the ADMIN role, it prompts for the creation of an admin user.
     *
     * @param filePath The path to the file for storing and loading users.
     */
    public AuthRepository(String filePath) {
        this.filePath = filePath;
        this.userDatabase = new HashMap<>();
        loadUsers(); // Load users from the file on initialization

        // If no admin user exists, prompt to create one
        if (!isAdminExist()) {
            System.out.println("No admin user found. Please create one.");
            createAdminUser();
        }
    }

    /**
     * Checks if an admin user exists in the database.
     *
     * @return true if an admin user exists, false otherwise.
     */
    private boolean isAdminExist() {
        for (User user : userDatabase.values()) {
            if (user.getRole() == Role.ADMIN) {  // Check for the admin role
                return true;
            }
        }
        return false;
    }

    /**
     * Prompts the user to create an admin user with username and password.
     * The password is hashed and stored securely.
     */
    private void createAdminUser() {
        Scanner scanner = new Scanner(System.in);

        // Prompt for admin username and password
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();

        // Ensure a valid password (you can add more complex validation if needed)
        String password;
        while (true) {
            System.out.print("Enter admin password: ");
            password = scanner.nextLine();
            if (password.length() < 6) {
                System.out.println("Password must be at least 6 characters long.");
            } else {
                break;
            }
        }

        // Generate salt
        String salt = PasswordUtils.generateSalt();

        // Hash the password with the generated salt
        String hashedPassword = PasswordUtils.hashPassword(password, salt);

        // Create the admin user with the ADMIN role
        User admin = new User(username, hashedPassword, salt, Role.ADMIN);

        // Add the admin user to the repository
        addUser(admin);
        System.out.println("Admin user created successfully.");
    }


    /**
     * Adds a new user to the repository.
     * If the username already exists, throws an IllegalArgumentException.
     *
     * @param user The User object to add.
     * @throws IllegalArgumentException If the username already exists.
     */
    public void addUser(User user) {
        if (userDatabase.containsKey(user.getUsername())) {
            throw new IllegalArgumentException("Username is already taken.");
        }
        userDatabase.put(user.getUsername(), user);
        saveUsers(); // Save users to the file after adding a new one
    }

    /**
     * Authenticates a user by their username and password.
     * Verifies that the password matches the stored hashed password using a salt.
     *
     * @param username The username of the user to authenticate.
     * @param password The password entered by the user.
     * @return The authenticated User object if authentication is successful, null otherwise.
     */
    public User authenticate(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && PasswordUtils.validatePassword(password, user.getSalt(), user.getHashedPassword())) {
            return user; // Authentication success
        }
        return null; // Authentication failed
    }

    /**
     * Retrieves a user by their username.
     *
     * @param username The username to search for.
     * @return The User object if found, null otherwise.
     */
    public User getUserByUsername(String username) {
        return userDatabase.get(username);
    }

    /**
     * Loads users from the .dat file into the in-memory database.
     * Each line in the file is expected to contain a username, hashed password, salt, and role.
     */
    private void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) { // Ensure 4 fields exist: username, hashedPassword, salt, and role
                    String username = parts[0];
                    String hashedPassword = parts[1];
                    String salt = parts[2];
                    Role role = Role.valueOf(parts[3].toUpperCase()); // Assuming the role is stored as a string
                    userDatabase.put(username, new User(username, hashedPassword, salt, role));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("User file not found. Starting with an empty user database.");
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    /**
     * Saves the in-memory user database to the .dat file.
     * Each user's data is saved in a line with the following format:
     * username;hashedPassword;salt;role
     */
    private void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (User user : userDatabase.values()) {
                writer.write(user.getUsername() + ";" + user.getHashedPassword() + ";" + user.getSalt() + ";" + user.getRole());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
}
