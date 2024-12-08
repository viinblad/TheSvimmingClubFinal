package swimclub.repositories;

import swimclub.models.Role;
import swimclub.models.User;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final String userFilePath;

    public UserRepository(String userFilePath) {
        this.userFilePath = userFilePath;
    }

    /**
     * Loads users from the .dat file, including username, hashed password, salt, and role.
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
                    // Create the User object and add to the list
                    users.add(new User(username, hashedPassword, salt, role));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
        return users;
    }

    /**
     * Saves a list of users to the .dat file, including username, hashed password, salt, and role.
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
}
