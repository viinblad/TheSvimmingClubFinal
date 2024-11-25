package swimclub.models;

/**
 * Abstract base class representing a member.
 * Contains common attributes and methods for all members in the swim club.
 */
public abstract class Member {
    private int memberId;                // Unique identifier for the member
    private String name;                 // Full name of the member
    private String email;                // Email address of the member
    private String city;                 // Living city of the member
    private String street;               // Street Address of the member
    private String region;               // Living region of the member
    private int zipcode;                 // Zip code number of the member
    private MembershipType membershipType; // Membership type (e.g., Competitive Junior)
    private MembershipStatus membershipStatus; // Membership status (active/passive)
    private PaymentStatus paymentStatus; // Payment status
    private int age;                     // Age of the member
    private int phoneNumber;             // Phone number of the member

    /**
     * Constructor for initializing a Member object.
     *
     * @param memberId       Unique ID of the member (parsed as int).
     * @param name           Full name of the member.
     * @param city           Living city of the member
     * @param region         Living region of the member
     * @param street         Living street of the member
     * @param zipcode        Living zip code of the member
     * @param email          Email address of the member.
     * @param membershipType The membership type of the member.
     * @param membershipStatus The membership status of the member.
     * @param paymentStatus  Payment status type of the member.
     * @param age            Age of the member.
     * @param phoneNumber    Phone number of the member.
     */
    public Member(String memberId, String name, String email, String city, String street,
                  String region, int zipcode,MembershipType membershipType, MembershipStatus membershipStatus, PaymentStatus paymentStatus, int age, int phoneNumber) {
        this.memberId = Integer.parseInt(memberId); // Parse memberId from String to int
        this.name = name;
        this.email = email;
        this.city = city;
        this.street = street;
        this.region = region;
        this.zipcode =  zipcode;
        this.membershipType = membershipType;
        this.membershipStatus = membershipStatus;
        this.paymentStatus = paymentStatus;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    // -----------------------------------------------------------------------------------------------------
    // Get Methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * @return The unique ID of the member.
     */
    public int getMemberId() {
        return this.memberId;
    }



    /**
     * @return The full name of the member.
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return The email address of the member.
     */
    public String getEmail() {
        return this.email;
    }

    /**
     *
     * @return the city of the member
     */
    public String getCity() {
        return this.city;
    }
    /**
     *
     * @return the street of the member
     */
    public String getStreet() {
        return this.street;
    }
    /**
     *
     * @return the region of the member
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * @return The zip code of the member.
     */
    public int getZipcode() {
        return this.zipcode;
    }

    /**
     * @return The membership type of the member as a descriptive string.
     */
    public MembershipType getMembershipType() {
        return this.membershipType;
    }


    public MembershipStatus getMembershipStatus() {
        return this.membershipStatus;
    }

    public PaymentStatus getPaymentStatus () {
        return this.paymentStatus;
    }


    /**
     * @return The age of the member.
     */
    public int getAge() {
        return this.age;
    }

    /**
     * @return The phone number of the member.
     */
    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    // -----------------------------------------------------------------------------------------------------
    // Set Methods
    // -----------------------------------------------------------------------------------------------------
    /**
     * @param memberId  The unique ID of the member.
     */
    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    /**
     * Updates the full name of the member.
     *
     * @param name The new name of the member.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Updates the email address of the member.
     *
     * @param email The new email address of the member.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Updates the street of the member.
     *
     * @param city The new address of the member
     */
    public void setCity(String city) {
        this.city = city;
    }


    /**
     * Updates the street of the member.
     *
     * @param street The new address of the member
     */
    public void setStreet(String street) {
        this.street = street;
    }
    /**
     * Updates the street of the member.
     *
     * @param region The new address of the member
     */
    public void setRegion(String region) {
        this.region = region;
    }

    /**
     * Updates the age of the member.
     *
     * @param zipcode The new age of the member.
     */
    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * Updates the membership type of the member.
     *
     * @param membershipType The new membership type of the member.
     */
    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public void setMembershipStatus(MembershipStatus membershipStatus) {
        this.membershipStatus = membershipStatus;

    } public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }



    /**
     * Updates the age of the member.
     *
     * @param age The new age of the member.
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Updates the phone number of the member.
     *
     * @param phoneNumber The new phone number of the member.
     */
    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // -----------------------------------------------------------------------------------------------------
    // Abstract Method
    // -----------------------------------------------------------------------------------------------------

    /**
     * Abstract method to be implemented by subclasses to provide specific membership descriptions.
     *
     * @return A string describing the member's type and attributes.
     */
    public abstract String getMembershipDescription();
}
