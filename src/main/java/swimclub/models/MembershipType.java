package swimclub.models;

/**
 * Represents the type of membership a member can have.
 * Memberships are categorized as either Competitive or Exercise swimmers,
 * with additional distinctions between Junior and Senior members.
 */
public class MembershipType {
    private final MembershipCategory category; // Competitive or Exercise
    private final MembershipLevel level;       // Junior or Senior

    /**
     * Constructor for creating a MembershipType.
     *
     * @param category The category of membership (Competitive or Exercise).
     * @param level    The level of membership (Junior or Senior).
     */
    public MembershipType(MembershipCategory category, MembershipLevel level) {
        this.category = category;
        this.level = level;
    }

    /**
     * @return The category of the membership.
     */
    public MembershipCategory getCategory() {
        return category;
    }

    /**
     * @return The level of the membership.
     */
    public MembershipLevel getLevel() {
        return level;
    }

    /**
     * @return A descriptive string of the membership type.
     */
    @Override
    public String toString() {
        return level + " " + category + " Swimmer";
    }

    /**
     * Factory method to create a MembershipType from a string.
     *
     * @param membershipTypeStr A string representing the membership type (e.g., "Junior Competitive").
     * @return A MembershipType object corresponding to the string.
     */
    public static MembershipType fromString(String membershipTypeStr) {
        // Remove "Swimmer" suffix if it exists
        String cleanString = membershipTypeStr.replace(" Swimmer", "").trim();

        String[] parts = cleanString.split(" ");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid membership type format: " + membershipTypeStr);
        }

        MembershipLevel level = MembershipLevel.valueOf(parts[0].toUpperCase());
        MembershipCategory category = MembershipCategory.valueOf(parts[1].toUpperCase());

        return new MembershipType(category, level);
    }
}
