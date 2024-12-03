package swimclub.models;

/**
 * Enum representing predefined team types in the swim club.
 */
public enum TeamType {
    JUNIOR_COMPETITIVE("Junior Competitive"),
    SENIOR_COMPETITIVE("Senior Competitive");

    private final String displayName;

    /**
     * Constructor for TeamType.
     *
     * @param displayName The display name of the team type.
     */
    TeamType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Gets the display name of the team type.
     *
     * @return The display name.
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Converts a string to a TeamType enum, case insensitive.
     *
     * @param teamTypeName The string representation of the team type.
     * @return The corresponding TeamType.
     * @throws IllegalArgumentException If the team type does not exist.
     */
    public static TeamType fromString(String teamTypeName) {
        for (TeamType type : TeamType.values()) {
            if (type.getDisplayName().equalsIgnoreCase(teamTypeName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid team type: " + teamTypeName);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
