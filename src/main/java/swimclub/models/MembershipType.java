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
}
