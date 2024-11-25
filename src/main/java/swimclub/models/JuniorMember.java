package swimclub.models;

/**
 * Represents a Junior member in the swim club.
 */
public class JuniorMember extends Member {

    // Constructor for JuniorMember
    public JuniorMember(String memberId, String name, String email, MembershipType membershipType, MembershipStatus membershipStatus, PaymentStatus paymentStatus, int age, int phoneNumber) {
        super(memberId, name, email, membershipType, membershipStatus, paymentStatus, age, phoneNumber); // Call the parent constructor
    }

    @Override
    public String getMembershipDescription() {
        return "Junior Member: " + getMembershipType().toString();
    }
}
