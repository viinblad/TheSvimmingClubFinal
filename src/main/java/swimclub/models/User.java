package swimclub.models;


import javax.management.relation.Role;


public class User {
    private final String username;
    private final String password;
    private final Staff staff; // Association with Staff

    public User(String username, String password, Staff staff) {
        this.username = username;
        this.password = password;
        this.staff = staff;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Staff getStaff() {
        return staff;
    }

    public Role getRole() {
        return staff.getRole(); // Delegate role to associated Staff object
    }
}
