package swimclub.models;

/**
 * Represents a Senior member in the swim club.
 * This class extends the base Member class and is used to represent members who are 18 years old or older.
 */
public class SeniorMember extends Member {

    /**
     * Constructor for SeniorMember.
     * This constructor initializes a SeniorMember instance with the provided details.
     *
     * @param memberId      The unique ID of the member.
     * @param name          The full name of the member.
     * @param email         The email address of the member.
     * @param membershipType The type of membership (e.g., Competitive Senior, Exercise Senior).
     * @param age           The age of the member (used to determine membership level).
     * @param phoneNumber   The phone number of the member.
     */
    public SeniorMember(String memberId, String name, String email, MembershipType membershipType, int age, int phoneNumber) {
        super(memberId, name, email, membershipType, age, phoneNumber); // Call the parent constructor
    }

    /**
     * Returns the membership description for this Senior member.
     * This includes the membership level and category.
     *
     * @return A string describing the member's type (e.g., "Senior Member: Senior Competitive Swimmer").
     */
    @Override
    public String getMembershipDescription() {
        return "Senior Member: " + getMembershipType().toString();
    }
}
