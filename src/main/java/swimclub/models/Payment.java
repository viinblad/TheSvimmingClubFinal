package swimclub.models;

import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private PaymentStatus paymentStatus; //Status can either be COMPLETE, PENDING or FAILED.
    private Member member;
    private LocalDate paymentDate; // LOCALDATE would be 25-11-2024 as an example, and does not include hours, mins or seconds.
    private double amountPerYear; // the contingent that a person has to pay for remaining in the swimming club.

    public Payment(int paymentId, PaymentStatus paymentStatus, Member member, LocalDate paymentDate, int amountPerYear) {
        if (amountPerYear <= 0) {
            throw new IllegalArgumentException("Amount per year must be positive.");
        }

        if (member == null) {
            throw new NullPointerException("Member cannot be null");
        }

        this.paymentId = paymentId;
        this.paymentStatus = paymentStatus;
        this.member = member;
        this.amountPerYear = amountPerYear;
        this.paymentDate = paymentDate;
    }

    //GetMethods---------------------------------------------------------------------------------------------------------------
    public int getPaymentId() {
        return paymentId;
    }

    public Member getMember() {
        return member;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public double getAmountPerYear() {
        return amountPerYear;
    }

    //SetMethods---------------------------------------------------------------------------------------------------------------
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setAmountPerYear(double amountPerYear) {
        if (amountPerYear <= 0) {
            throw new IllegalArgumentException("Amount per year must be positive");
        }
        this.amountPerYear = amountPerYear;
    }

    @Override
    public String toString() {
        return "Payment (" + member.getName() + ")" + ":" +
                "paymentId = " + paymentId +
                ", member = " + member +
                ", paymentDate = " + paymentDate +
                ", amountPerYear = " + amountPerYear + ".";
    }
}
