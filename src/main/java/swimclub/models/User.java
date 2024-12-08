package swimclub.models;

public class User {
    private final String username;
    private final String hashedPassword;
    private final String salt;
    private final Role role;

    public User(String username, String hashedPassword, String salt, Role role) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getSalt() {
        return salt;
    }

    public Role getRole() {
        return role;
    }
}
