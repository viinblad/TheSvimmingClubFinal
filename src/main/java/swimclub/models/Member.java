package swimclub.models;

public abstract class Member {
    private String memberId;
    private String name;
    private String email;
    private int age;
    private int phoneNumber;
    private boolean active;

    //private Payment payment

    public Member (String memberId, String name, String email, int age, int phoneNumber, boolean active) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.active = active;
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

public boolean getActive () {
        return this.active;
}
    //SetMethods-----------------------------------------------------------------------------------------------------

}
