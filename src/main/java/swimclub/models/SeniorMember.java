package swimclub.models;

/**
 * Represents a Senior member in the swim club.
 */
public class SeniorMember extends Member {

    public SeniorMember(String memberId, String name, String email, MembershipType membershipType, int age, int phoneNumber) {
        super(memberId, name, email, membershipType, age, phoneNumber);
    }

    @Override
    public String getMembershipDescription() {
        return "Senior Member: " + getMembershipType().toString();
    }
}
