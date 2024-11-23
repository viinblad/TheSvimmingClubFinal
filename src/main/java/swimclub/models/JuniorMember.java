package swimclub.models;

/**
 * Represents a Junior member in the swim club.
 */
public class JuniorMember extends Member {

    public JuniorMember(String memberId, String name, String email, MembershipType membershipType, int age, int phoneNumber) {
        super(memberId, name, email, membershipType, age, phoneNumber);
    }

    @Override
    public String getMembershipDescription() {
        return "Junior Member: " + getMembershipType().toString();
    }
}
