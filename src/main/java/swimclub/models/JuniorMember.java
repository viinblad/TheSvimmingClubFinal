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
     * @param memberId         The unique ID of the member as a String.
     * @param name             The full name of the member.
     * @param email            The email address of the member.
     * @param city             The city where the member lives.
     * @param street           The street address of the member.
     * @param region           The region where the member lives.
     * @param zipcode          The zip code of the member's address.
     * @param membershipType   The type of membership (e.g., Competitive Senior, Exercise Senior).
     * @param membershipStatus The membership status of the member (e.g., ACTIVE, PASSIVE).
     * @param activityType     The preferred activity type of the member (e.g., Breaststroke, Crawl).
     * @param paymentStatus    The payment status of the member (e.g., PENDING, COMPLETE).
     * @param age              The age of the member (must be 18 or older).
     * @param phoneNumber      The phone number of the member (8 digits).
     */
    public JuniorMember(String memberId, String name, String email, String city, String street, String region, int zipcode,
                        MembershipType membershipType, MembershipStatus membershipStatus,ActivityType activityType, PaymentStatus paymentStatus, int age, int phoneNumber, String teamName) {
        super(memberId, name, email, city, street, region, zipcode, membershipType,membershipStatus,activityType,paymentStatus, age, phoneNumber, teamName); // Call the parent constructor
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
