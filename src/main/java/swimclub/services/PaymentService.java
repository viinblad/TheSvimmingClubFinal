package swimclub.services;

import swimclub.models.Member;
import swimclub.models.MembershipStatus;
import swimclub.models.Payment;
import swimclub.models.PaymentStatus;
import swimclub.repositories.MemberRepository;
import swimclub.repositories.PaymentRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling payment-related operations.
 */
public class PaymentService {
    private final PaymentRepository paymentRepository; // Add PaymentRepository reference

    // Constructor accepts a PaymentRepository to interact with payment data
    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    /**
     * Calculates the annual membership fee based on the member's status and age.
     *
     * @param member The member whose membership fee is being calculated.
     * @return The calculated membership fee.
     */
    public double calculateMembershipFee(Member member) {
        if (member.getMembershipStatus() == MembershipStatus.PASSIVE) {
            return 500; // Passive members pay a fixed fee of 500.
        }

        if (member.getMembershipStatus() == MembershipStatus.ACTIVE) {
            if (member.getAge() < 18) {
                return 1000; // Active members under 18 pay 1000.
            } else if (member.getAge() < 60) {
                return 1600; // Active members aged 18-59 pay 1600.
            } else {
                return 1600 * 0.75; // Active members 60+ receive a 25% discount.
            }
        }

        return 0; // Default case (unlikely to occur if member status is valid).
    }

    /**
     * Calculates the total expected payment across all members.
     *
     * @param memberList The list of all members.
     * @return The total expected payment.
     */
    public double calculateTotalExpectedPayments(List<Member> memberList) {
        double total = 0;
        for (Member member : memberList) {
            total += calculateMembershipFee(member);
        }
        return total;
    }

    /**
     * Retrieves a list of members who have completed their payment.
     *
     * @param memberList The list of all members.
     * @return A list of members with a payment status of COMPLETE.
     */
    public List<Member> getMembersPaidList(List<Member> memberList) {
        List<Member> paidMembers = new ArrayList<>();
        for (Member member : memberList) {
            if (member.getPaymentStatus() == PaymentStatus.COMPLETE) {
                paidMembers.add(member);
            }
        }
        return paidMembers;
    }

    /**
     * Retrieves a list of members whose payment status is pending.
     *
     * @param memberList The list of all members.
     * @return A list of members with a payment status of PENDING.
     */
    public List<Member> getMembersPendingList(List<Member> memberList) {
        List<Member> pendingMembers = new ArrayList<>();
        for (Member member : memberList) {
            if (member.getPaymentStatus() == PaymentStatus.PENDING) {
                pendingMembers.add(member);
            }
        }
        return pendingMembers;
    }

    /**
     * Updates a member's payment status.
     *
     * @param member       The member whose payment status is being updated.
     * @param paymentStatus The new payment status to set.
     */
    public void updateMemberPaymentStatus(Member member, PaymentStatus paymentStatus) {
        member.setPaymentStatus(paymentStatus);
    }

    // Method to register a payment for a member
    public void registerPayment(int memberId, double amount, MemberRepository memberRepository) {
        // Find the member by ID
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            System.out.println("Member not found with ID: " + memberId);
            return;
        }

        // Create a new payment instance with a unique ID and status set to PENDING
        Payment newPayment = new Payment(paymentRepository.getNextPaymentId(), PaymentStatus.PENDING, member, LocalDate.now(), amount);

        // Save the payment to the repository
        savePayment(newPayment);

        // Update the member's payment status
        updateMemberPaymentStatus(member, PaymentStatus.PENDING);
        System.out.println("Payment of " + amount + " registered for member ID: " + memberId);
    }

    // Method to save a payment to the repository
    public void savePayment(Payment payment) {
        paymentRepository.save(payment); // Save the payment using PaymentRepository
    }

    // Method to view payments for a member
    public void viewPaymentsForMember(int memberId) {
        List<Payment> memberPayments = paymentRepository.findPaymentsByMemberId(memberId);

        if (memberPayments.isEmpty()) {
            System.out.println("No payments found for member ID: " + memberId);
        } else {
            System.out.println("--- Payments for Member ID: " + memberId + " ---");
            for (Payment payment : memberPayments) {
                System.out.println(payment.toString());
            }
        }
    }
}
