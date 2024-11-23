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
        setName(name);
        setEmail(email);
        setAge(age);
        setPhoneNumber(phoneNumber);
        this.active = active;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        this.name = name;

    }
    public String getEMail() {
        return email;
    }
    public void setEmail(String email) {
        if(email == null || email.isEmpty() || !email.contains("@")){
            throw new IllegalArgumentException("Invalid e-mail address.");
        }
        this.email = email;

    }
    public int getAge() {
        return age;
    }
    public void setAge(int age){
        if(age <0) {
            throw new IllegalArgumentException("Age cannot be negative.");
        }
        this.age = age;

    }
    public int getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(int phoneNumber) {
        String phoneNumberString = String.valueOf(phoneNumber);
        if (phoneNumberString.length() != 8) {
            throw new IllegalArgumentException("Phone number must be 8 digits.");
        }
        this.phoneNumber = phoneNumber;

    }
    public void setActive(boolean active) {
        this.active = active;
    }



}
