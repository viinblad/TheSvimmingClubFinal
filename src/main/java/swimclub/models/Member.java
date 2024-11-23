package swimclub.models;

public abstract class Member {
    private String memberId;
    private String name;
    private String email;
    private MembershipType membershipType;
    private int age;
    private int phoneNumber;

    //private Payment payment

    public Member (String memberId, String name, String email, MembershipType membershiptype, int age, int phoneNumber) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.membershipType = MembershipType.INACTIVE;
    }

// return member.getId() + ";" + member.getName() + ";" + member.getAge() + ";" + member.getMembershipType(); //


    //GetMethods-----------------------------------------------------------------------------------------------------
public String getMemberId () {
        return this.memberId;
}

public String getName () {
        return this.name;
}

public int getAge() {
        return this.age;
}

public String getEmail () {
        return this.email;
}

public MembershipType getMembershipType () {
        return this.membershipType;
}

    //SetMethods-----------------------------------------------------------------------------------------------------





    //Interfaces------------------------------------------------------------------------------------------------------
    class Junior extends Member, Excersicer, EliteSwimmer () {

    }

}
