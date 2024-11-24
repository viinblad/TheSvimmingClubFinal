package swimclub.models;

/**
 * Represents a Junior member in the swim club.
 * This class extends the base Member class and is used to represent members who are under 18 years old.
 */
public class JuniorMember extends Member {

    /**
     * Constructor for JuniorMember.
     * This constructor initializes a JuniorMember instance with the provided details.
     *
     * @param memberId      The unique ID of the member.
     * @param name          The full name of the member.
     * @param email         The email address of the member.
     * @param membershipType The type of membership (e.g., Competitive Junior, Exercise Junior).
     * @param age           The age of the member (used to determine membership level).
     * @param phoneNumber   The phone number of the member.
     */
    public JuniorMember(String memberId, String name, String email, MembershipType membershipType, int age, int phoneNumber) {
        super(memberId, name, email, membershipType, age, phoneNumber); // Call the parent constructor
    }

    /**
     * Returns the membership description for this Junior member.
     * This includes the membership level and category.
     *
     * @return A string describing the member's type (e.g., "Junior Member: Junior Competitive Swimmer").
     */
    @Override
    public String getMembershipDescription() {
        return "Junior Member: " + getMembershipType().toString();
    }
}
