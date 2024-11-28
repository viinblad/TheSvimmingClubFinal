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
    private int age;                     // Age of the member
    private int phoneNumber;             // Phone number of the member
    private List<Payment> payments = new ArrayList<>(); // List of all payments made by the member.

    /**
     * Constructor for initializing a Member object.
     */
    public Member(String memberId, String name, String email, String city, String street,
                  String region, int zipcode, MembershipType membershipType,
                  MembershipStatus membershipStatus, PaymentStatus paymentStatus,
                  int age, int phoneNumber) {
        this.memberId = Integer.parseInt(memberId); // Parse memberId from String to int
        this.name = name;
        this.email = email;
        this.city = city;
        this.street = street;
        this.region = region;
        this.zipcode = zipcode;
        this.membershipType = membershipType;
        this.membershipStatus = membershipStatus;
        this.paymentStatus = paymentStatus;
        this.age = age;
        this.phoneNumber = phoneNumber;
    }

    // -----------------------------------------------------------------------------------------------------
    // Get Methods
    // -----------------------------------------------------------------------------------------------------

    public int getMemberId() {
        return this.memberId;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public String getCity() {
        return this.city;
    }

    public String getStreet() {
        return this.street;
    }

    public String getRegion() {
        return this.region;
    }

    public int getZipcode() {
        return this.zipcode;
    }

    public MembershipType getMembershipType() {
        return this.membershipType;
    }

    public MembershipStatus getMembershipStatus() {
        return this.membershipStatus;
    }

    public PaymentStatus getPaymentStatus() {
        return this.paymentStatus;
    }

    public int getAge() {
        return this.age;
    }

    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    public List<Payment> getPayments() {
        return new ArrayList<>(this.payments); // Return a copy to avoid external modification
    }

    // -----------------------------------------------------------------------------------------------------
    // Set Methods
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

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // -----------------------------------------------------------------------------------------------------
    // Payment Management Methods
    // -----------------------------------------------------------------------------------------------------

    /**
     * Adds a payment to the member's payment list.
     */
    public void addPayment(Payment payment) {
        this.payments.add(payment);
        updatePaymentStatus(); // Update the overall payment status based on the new payment
    }

    /**
     * Calculates the total amount paid by the member.
     *
     * @return The total amount paid.
     */
    public double calculateTotalPaid() {
        return payments.stream()
                .filter(payment -> payment.getPaymentStatus() == PaymentStatus.COMPLETE)
                .mapToDouble(Payment::getAmountPerYear)
                .sum();
    }

    /**
     * Updates the overall payment status of the member based on individual payments.
     */
    private void updatePaymentStatus() {
        if (payments.stream().anyMatch(payment -> payment.getPaymentStatus() == PaymentStatus.PENDING)) {
            this.paymentStatus = PaymentStatus.PENDING;
        } else if (payments.stream().allMatch(payment -> payment.getPaymentStatus() == PaymentStatus.COMPLETE)) {
            this.paymentStatus = PaymentStatus.COMPLETE;
        } else {
            this.paymentStatus = PaymentStatus.FAILED;
        }
    }

    // -----------------------------------------------------------------------------------------------------
    // Abstract Method
    // -----------------------------------------------------------------------------------------------------

    public abstract String getMembershipDescription();
}
