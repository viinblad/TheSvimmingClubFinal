package swimclub.models;

import java.time.LocalDate;

public class Payment {
    private int paymentId;                 // Unique ID for the payment
    private PaymentStatus paymentStatus;   // COMPLETE, PENDING, FAILED
    private Member member;                 // Member associated with the payment
    private LocalDate paymentDate;         // Date of payment
    private double amountPerYear;          // Annual membership fee

    /**
     * Constructor for Payment.
     *
     * @param paymentId     Unique payment ID.
     * @param paymentStatus Status of the payment (COMPLETE, PENDING, FAILED).
     * @param member        Member associated with the payment.
     * @param paymentDate   Date of the payment.
     * @param amountPerYear Annual membership fee.
     * @throws IllegalArgumentException If amount is not positive.
     * @throws NullPointerException     If member or paymentDate is null.
     */
    public Payment(int paymentId, PaymentStatus paymentStatus, Member member, LocalDate paymentDate, double amountPerYear) {
        if (amountPerYear <= 0) {
            throw new IllegalArgumentException("Amount per year must be positive.");
        }

        if (member == null) {
            throw new NullPointerException("Member cannot be null");
        }

        if (paymentDate == null) {
            throw new NullPointerException("Payment date cannot be null");
        }

        this.paymentId = paymentId;
        this.paymentStatus = paymentStatus;
        this.member = member;
        this.amountPerYear = amountPerYear;
        this.paymentDate = paymentDate;
    }
    /**
     * Gets the unique payment ID.
     *
     * @return The payment ID.
     */
    public int getPaymentId() {
        return paymentId;
    }
    /**
     * Gets the payment status.
     *
     * @return The payment status (COMPLETE, PENDING, FAILED).
     */
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    /**
     * Gets the member associated with the payment.
     *
     * @return The associated Member object.
     */
    public Member getMember() {
        return member;
    }
    /**
     * Gets the date of the payment.
     *
     * @return The payment date as a LocalDate object.
     */
    public LocalDate getPaymentDate() {
        return paymentDate;
    }
    /**
     * Gets the annual membership fee for the payment.
     *
     * @return The annual fee amount.
     */
    public double getAmountPerYear() {
        return amountPerYear;
    }
    /**
     * Sets the payment ID.
     *
     * @param paymentId The unique payment ID to set.
     */

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
    /**
     * Sets the payment status.
     *
     * @param paymentStatus The payment status to set (COMPLETE, PENDING, FAILED).
     */
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    /**
     * Sets the payment date.
     *
     * @param paymentDate The payment date to set.
     * @throws NullPointerException If the paymentDate is null.
     */
    public void setPaymentDate(LocalDate paymentDate) {
        if (paymentDate == null) {
            throw new NullPointerException("Payment date cannot be null");
        }
        this.paymentDate = paymentDate;
    }
    /**
     * Sets the member associated with the payment.
     *
     * @param member The member to associate with the payment.
     * @throws NullPointerException If the member is null.
     */
    public void setMember(Member member) {
        if (member == null) {
            throw new NullPointerException("Member cannot be null");
        }
        this.member = member;
    }
    /**
     * Sets the annual membership fee.
     *
     * @param amountPerYear The annual fee amount to set.
     * @throws IllegalArgumentException If the amount is not positive.
     */
    public void setAmountPerYear(double amountPerYear) {
        if (amountPerYear <= 0) {
            throw new IllegalArgumentException("Amount per year must be positive");
        }
        this.amountPerYear = amountPerYear;
    }

    /**
     * Provides a detailed string representation of the payment.
     *
     * @return A string with the payment details.
     */
    @Override
    public String toString() {
        return "Payment Details: {" +
                "Payment ID: " + paymentId +
                ", Member: " + member.getName() +
                ", Payment Date: " + paymentDate +
                ", Amount: " + amountPerYear + " DKK " +
                ", Status: " + paymentStatus +
                "}";
    }
}
