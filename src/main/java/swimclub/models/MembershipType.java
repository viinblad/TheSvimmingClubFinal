package swimclub.models;

/**
 * Represents the type of membership a member can have.
 * Memberships are categorized as either Competitive or Exercise swimmers,
 * with additional distinctions between Junior and Senior members.
 */
public class MembershipType {
    private MembershipCategory category; // Competitive or Exercise
    private MembershipLevel level;       // Junior or Senior

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
     * Gets the category of the membership.
     *
     * @return The category of the membership (Competitive or Exercise).
     */
    public MembershipCategory getCategory() {
        return category;
    }

    /**
     * Gets the level of the membership.
     *
     * @return The level of the membership (Junior or Senior).
     */
    public MembershipLevel getLevel() {
        return level;
    }

    /**
     * Sets the level of the membership (Junior or Senior).
     * This allows the level to be dynamically changed.
     *
     * @param level The new membership level to set.
     */
    public void setLevel(MembershipLevel level) {
        this.level = level;
    }

    /**
     * Provides a descriptive string of the membership type.
     *
     * @return A string representing the membership type in the format: "Junior Competitive Swimmer" or "Senior Exercise Swimmer".
     */
    @Override
    public String toString() {
        return level + " " + category + " Swimmer";
    }

    /**
     * Factory method to create a MembershipType from a string.
     * This will allow you to construct a MembershipType from a descriptive string like "Junior Competitive".
     *
     * @param membershipTypeStr A string representing the membership type (e.g., "Junior Competitive").
     * @return A MembershipType object corresponding to the string.
     * @throws IllegalArgumentException If the string does not match a valid membership type format.
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
