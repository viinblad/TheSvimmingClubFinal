package swimclub.controllers;

import swimclub.models.Role;

/**
 * Controller class for handling administrative actions and role-based validation.
 */
public class AdminController {

    private Role currentRole;

    /**
     * Sets the current role for the logged-in user.
     * This role determines the permissions for the session.
     *
     * @param role The role of the current user.
     */
    public void setRole(Role role) {
        this.currentRole = role;
    }

    /**
     * Gets the current role for the logged-in user.
     *
     * @return The current role.
     */
    public Role getRole() {
        return currentRole;
    }

    /**
     * Validates if the current role is allowed to perform a specific action.
     * Throws a SecurityException if the role is unauthorized.
     *
     * @param allowedRoles The roles allowed to perform the action.
     */
    public void validateRole(Role... allowedRoles) {
        for (Role allowedRole : allowedRoles) {
            if (currentRole == allowedRole) {
                return; // Authorized
            }
        }
        throw new SecurityException("You do not have permission to perform this action.");
    }
}
