package swimclub.repositories;

import swimclub.models.Role;
import swimclub.models.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for handling user data, including loading, saving, and modifying users.
 * Users are stored in a file and managed in memory.
 */
public class UserRepository {
    private final String userFilePath;

    /**
     * Constructor to initialize the UserRepository.
     *
     * @param userFilePath The path to the file where users are stored.
     */
    public UserRepository(String userFilePath) {
        this.userFilePath = userFilePath;
    }

    /**
     * Loads users from the .dat file into memory.
     * The file is expected to contain users in the format: username;hashedPassword;salt;role.
     *
     * @return A list of users loaded from the file.
     */
    public List<User> loadUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(userFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 4) {
                    String username = parts[0];
                    String hashedPassword = parts[1];
                    String salt = parts[2];
                    Role role = Role.valueOf(parts[3].toUpperCase());
                    // Create the User object and add it to the list
                    users.add(new User(username, hashedPassword, salt, role));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Saves the list of users back to the .dat file, storing each user's username, hashed password, salt, and role.
     *
     * @param users The list of users to save.
     */
    public void saveUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(userFilePath))) {
            for (User user : users) {
                // Write username, hashed password, salt, and role
                writer.write(user.getUsername() + ";" + user.getHashedPassword() + ";" + user.getSalt() + ";" + user.getRole());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    /**
     * Gets a user by their username.
     * Searches the list of users in memory and returns the User object if found.
     *
     * @param username The username of the user to retrieve.
     * @return The User object if found, or null if no user is found with the specified username.
     */
    public User getUserByUsername(String username) {
        List<User> users = loadUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user; // Return the user if found
            }
        }
        return null; // Return null if user not found
    }

    /**
     * Deletes a user by their username.
     * The user will be removed from the in-memory list and the updated list will be saved back to the file.
     *
     * @param username The username of the user to delete.
     */
    public void deleteUser(String username) {
        List<User> users = loadUsers();
        users.removeIf(user -> user.getUsername().equals(username)); // Remove user by username
        saveUsers(users); // Save the updated list of users to the file
    }

    /**
     * Adds a new user to the repository.
     * The user is added to the in-memory list and the updated list is saved to the file.
     *
     * @param user The User object to add to the repository.
     */
    public void addUser(User user) {
        List<User> users = loadUsers();
        users.add(user); // Add the new user to the list
        saveUsers(users); // Save the updated list of users to the file
    }
}
