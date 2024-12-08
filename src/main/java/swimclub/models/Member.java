package swimclub.models;

import java.util.ArrayList;
import java.util.List;

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
    private ActivityType activityType;   // Member's form of activity
    private int age;                     // Age of the member
    private int phoneNumber;             // Phone number of the member
    private List<Payment> payments = new ArrayList<>(); // List of all payments made by the member
    private String teamName;                   // Reference to the team this member belongs to

    // -----------------------------------------------------------------------------------------------------
    // Constructor
    // -----------------------------------------------------------------------------------------------------
    /**
     * Constructor for initializing a Member object.
     */
    public Member(String memberId, String name, String email, String city, String street,
                  String region, int zipcode, MembershipType membershipType,
                  MembershipStatus membershipStatus, ActivityType activityType, PaymentStatus paymentStatus,
                  int age, int phoneNumber, String teamName) {
        this.memberId = Integer.parseInt(memberId);
        this.name = name;
        this.email = email;
        this.city = city;
        this.street = street;
        this.region = region;
        this.zipcode = zipcode;
        this.membershipType = membershipType;
        this.membershipStatus = membershipStatus;
        this.activityType = activityType;
        this.paymentStatus = paymentStatus;
        this.age = age;
        this.phoneNumber = phoneNumber;

        // Initialize teamName to "no team" if not provided
        this.teamName = (teamName != null && !teamName.trim().isEmpty()) ? teamName : "no team";
    }

    // -----------------------------------------------------------------------------------------------------
    // Getter Methods
    // -----------------------------------------------------------------------------------------------------

    public int getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getRegion() {
        return region;
    }

    public int getZipcode() {
        return zipcode;
    }

    public MembershipType getMembershipType() {
        return membershipType;
    }

    public MembershipStatus getMembershipStatus() {
        return membershipStatus;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public int getAge() {
        return age;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public String getTeamName() {
        return this.teamName;
    }

    public List<Payment> getPayments() {
        return new ArrayList<>(payments); // Return a copy to avoid external modification
    }

    // -----------------------------------------------------------------------------------------------------
    // Setter Methods
    // -----------------------------------------------------------------------------------------------------

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    public void setMembershipType(MembershipType membershipType) {
        this.membershipType = membershipType;
    }

    public void setMembershipStatus(MembershipStatus membershipStatus) {
        this.membershipStatus = membershipStatus;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setTeamName (String teamName) {
        this.teamName = teamName;
    }

    // -----------------------------------------------------------------------------------------------------
    // Payment Management Methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Adds a payment to the member's payment list.
     * Updates the payment status automatically.
     */
    public void addPayment(Payment payment) {
        payments.add(payment);
        updatePaymentStatus(); // Update the payment status based on the current payments
    }

    /**
     * Calculates the total amount paid by the member.
     * @return The total amount paid.
     */
    public double calculateTotalPaid() {
        return payments.stream()
                .filter(payment -> payment.getPaymentStatus() == PaymentStatus.COMPLETE)
                .mapToDouble(Payment::getAmountPerYear)
                .sum();
    }

    /**
     * Updates the overall payment status of the member.
     * If any payment is pending, the status is set to PENDING.
     * If all payments are complete, the status is set to COMPLETE.
     * Otherwise, the status is set to FAILED.
     */
    private void updatePaymentStatus() {
        if (payments.stream().anyMatch(payment -> payment.getPaymentStatus() == PaymentStatus.PENDING)) {
            paymentStatus = PaymentStatus.PENDING;
        } else if (payments.stream().allMatch(payment -> payment.getPaymentStatus() == PaymentStatus.COMPLETE)) {
            paymentStatus = PaymentStatus.COMPLETE;
        } else {
            paymentStatus = PaymentStatus.FAILED;
        }
    }

    // -----------------------------------------------------------------------------------------------------
    // Abstract Method
    // -----------------------------------------------------------------------------------------------------

    /**
     * Abstract method to be implemented by subclasses to provide specific membership descriptions.
     * @return A string describing the member's type and attributes.
     */
    public abstract String getMembershipDescription();
}