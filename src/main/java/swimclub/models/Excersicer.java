package swimclub.models;

public class Excersicer extends Member {

    public Excersicer (String memberId, String name, String email, int age, int phoneNumber) {
        super(memberId, name, email,  MembershipType.EXCERSICER, age, phoneNumber); //adds excersicer as a default value for membershipType.

    }
}
