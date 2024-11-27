package swimclub.controllers;

import swimclub.models.Member;
import swimclub.models.PaymentStatus;
import swimclub.repositories.MemberRepository;
import swimclub.services.PaymentService;

import java.util.List;

/**
 * Controller class for handling payment-related operations.
 */
public class PaymentController {
    private final PaymentService paymentService;
    private final MemberRepository memberRepository;

    /**
     * Constructor to initialize the PaymentController.
     *
     * @param paymentService   The service handling payment-related logic.
     * @param memberRepository The repository for accessing member data.
     */
    public PaymentController(PaymentService paymentService, MemberRepository memberRepository) {
        this.paymentService = paymentService;
        this.memberRepository = memberRepository;
    }


    /**
     * Calculates the membership fee for a specific member by ID.
     *
     * @param memberId The ID of the member whose fee is being calculated.
     * @return The calculated membership fee, or -1 if the member is not found.
     */
    public double calculateMembershipFeeForMember(int memberId) {
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            System.out.println("Member not found with ID: " + memberId);
            return -1;
        }
        return paymentService.calculateMembershipFee(member);
    }

    /**
     * Calculates the total expected payments for all members.
     *
     * @return The total expected payments across all members.
     */
    public double calculateTotalExpectedPayments() {
        List<Member> members = memberRepository.findAll();
        return paymentService.calculateTotalExpectedPayments(members);
    }

    /**
     * Retrieves a list of members who have completed their payments.
     *
     * @return A list of members with a payment status of COMPLETE.
     */
    public List<Member> getMembersPaidList() {
        List<Member> members = memberRepository.findAll();
        return paymentService.getMembersPaidList(members);
    }

    /**
     * Retrieves a list of members whose payments are pending.
     *
     * @return A list of members with a payment status of PENDING.
     */
    public List<Member> getMembersPendingList() {
        List<Member> members = memberRepository.findAll();
        return paymentService.getMembersPendingList(members);
    }

    /**
     * Updates the payment status for a specific member by ID.
     *
     * @param memberId      The ID of the member whose payment status is being updated.
     * @param paymentStatus The new payment status to set.
     */
    public void updateMemberPaymentStatus(int memberId, PaymentStatus paymentStatus) {
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            System.out.println("Member not found with ID: " + memberId);
            return;
        }
        paymentService.updateMemberPaymentStatus(member, paymentStatus);
        memberRepository.update(member); // Save changes to the repository.
        System.out.println("Payment status updated for member ID: " + memberId);
    }

    /**
     * Registers a new payment for a member by entering member ID and payment amount.
     */
    public void registerPayment(int memberId, double amount) {
        // Call the PaymentService to register the payment
        paymentService.registerPayment(memberId, amount, memberRepository); // Register payment using PaymentService
    }

    /**
     * Displays all payments made by a specific member.
     */
    public void viewPaymentsForMember(int memberId) {
        // Call the PaymentService to get payments for a member
        paymentService.viewPaymentsForMember(memberId); // View all payments for the specified member
    }
}
