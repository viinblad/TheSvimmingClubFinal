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
    private ActivityType activityType;   // Members form of activity
    private int age;                     // Age of the member
    private int phoneNumber;             // Phone number of the member
    private List<Payment> payments = new ArrayList<>(); // List of all payments made by the member
    private String teamName;
    private Team team; // Reference to the team this member belongs to
    private double time;
    private String date;

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

    public ActivityType getActivityType() {
        return activityType;
    }

    public PaymentStatus getPaymentStatus() {
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

    /**
     * Gets the team the member belongs to.
     *
     * @return The team or null if not assigned.
     */
    public String getTeamName() {
        return this.teamName;
    }


    public List<Payment> getPayments() {
        return new ArrayList<>(payments); // Return a copy to avoid external modification
    }

    // -----------------------------------------------------------------------------------------------------
    // Setter Methods
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

    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
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
    /**
     * Sets the team for the member.
     *
     * @param teamName The team to assign.
     */
    public void setTeamName(String teamName) {
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