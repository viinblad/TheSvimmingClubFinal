package swimclub.models;

/**
 * Represents a user in the swim club system. A user has a username, hashed password, salt for hashing,
 * and a role assigned to them.
 */
public class User {
    private final String username;   // The username of the user
    private String hashedPassword; // The hashed password for the user
    private String salt;       // The salt used for password hashing
    private Role role;         // The role of the user (e.g., ADMIN, COACH, CHAIRMAN, etc.)

    /**
     * Constructs a new User object with the specified username, hashed password, salt, and role.
     *
     * @param username      The username of the user.
     * @param hashedPassword The hashed password of the user.
     * @param salt          The salt used to hash the password.
     * @param role          The role assigned to the user (e.g., ADMIN, COACH).
     */
    public User(String username, String hashedPassword, String salt, Role role) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.role = role;
    }

    /**
     * Returns the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the hashed password of the user.
     *
     * @return The hashed password of the user.
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * Returns the salt used to hash the user's password.
     *
     * @return The salt used for hashing.
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Returns the role of the user (e.g., ADMIN, COACH, CHAIRMAN).
     *
     * @return The role of the user.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Sets a new password for the user and re-hashes it with the existing salt.
     *
     * @param newHashedPassword The new hashed password for the user.
     */
    public void setPassword(String newHashedPassword) {
        this.hashedPassword = newHashedPassword;
    }

    /**
     * Sets a new salt for the user, typically used when changing the password.
     *
     * @param newSalt The new salt to be used for hashing.
     */
    public void setSalt(String newSalt) {
        this.salt = newSalt;
    }

    /**
     * Sets a new role for the user.
     *
     * @param newRole The new role to assign to the user.
     */
    public void setRole(Role newRole) {
        this.role = newRole;
    }
}
