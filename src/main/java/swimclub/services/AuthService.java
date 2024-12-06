package swimclub.services;

import swimclub.models.Staff;

import javax.management.relation.Role;

public class AuthService {
    private Staff loggedInUser; // Den bruger, der er logget ind

    // Simpel login-metode
    public boolean login(Staff staff) {
        if (staff != null) {
            this.loggedInUser = staff;
            return true;
        }
        return false;
    }

    // Tjek adgangsrettigheder
    public boolean hasAccess(Role requiredRole) {
        if (loggedInUser == null) {
            return false;
        }
        return loggedInUser.getRole() == requiredRole;
    }

    public Staff getLoggedInUser() {
        return loggedInUser;
    }
}
