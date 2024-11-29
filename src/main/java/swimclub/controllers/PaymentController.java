package swimclub.controllers;

import swimclub.models.Member;
import swimclub.models.PaymentStatus;
import swimclub.services.PaymentService;
import swimclub.repositories.MemberRepository;
import swimclub.Utilities.FileHandler;
import swimclub.Utilities.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for handling payment-related operations.
 */
public class PaymentController {
    private final PaymentService paymentService;
    private final MemberRepository memberRepository;
    private final FileHandler paymentFileHandler; // FileHandler for payments
    private final String paymentFilePath; // Path to the payment file

    /**
     * Constructor to initialize the PaymentController.
     *
     * @param paymentService     The service handling payment-related logic.
     * @param memberRepository   The repository for accessing member data.
     * @param paymentFileHandler The file handler for payments.
     * @param paymentFilePath    Path to the payment file.
     */
    public PaymentController(PaymentService paymentService, MemberRepository memberRepository,
                             FileHandler paymentFileHandler, String paymentFilePath) {
        this.paymentService = paymentService;
        this.memberRepository = memberRepository;
        this.paymentFileHandler = paymentFileHandler;
        this.paymentFilePath = paymentFilePath;
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
     * Registers a new payment for a member by entering member ID and payment amount.
     *
     * @param memberId The ID of the member making the payment.
     * @param amount   The amount of the payment.
     */
    public void registerPayment(int memberId, double amount) {
        // Validate payment amount
        try {
            Validator.validatePayment(amount, PaymentStatus.COMPLETE); // Defaulting to COMPLETE
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
            return;
        }

        // Get the member by ID
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            System.out.println("Member not found with ID: " + memberId);
            return;
        }

        // Register the payment through the service
        paymentService.registerPayment(memberId, amount, memberRepository, paymentFileHandler, paymentFilePath);

        // Update the member's payment status to COMPLETE after payment registration
        member.setPaymentStatus(PaymentStatus.COMPLETE);
        memberRepository.update(member);  // Save changes to the repository

        System.out.println("Payment of " + amount + " registered for Member ID: " + memberId);
    }

    /**
     * Displays all payments made by a specific member.
     *
     * @param memberId The ID of the member whose payments are being viewed.
     */
    public void viewPaymentsForMember(int memberId) {
        // Validate if the member exists
        if (memberRepository.findById(memberId) == null) {
            System.out.println("Member not found with ID: " + memberId);
            return;
        }

        // Call PaymentService to display payments
        paymentService.viewPaymentsForMember(memberId);
    }

    /**
     * Retrieves all members who have completed their payment.
     *
     * @return A list of members with a payment status of COMPLETE.
     */
    public List<Member> getMembersPaidList() {
        List<Member> members = memberRepository.findAll();
        return paymentService.getMembersPaidList(members);
    }

    /**
     * Retrieves a list of members whose payment status is either COMPLETE or PENDING.
     *
     * @param paymentStatus The payment status to filter by.
     * @return A list of members with the specified payment status.
     */
    public List<Member> getMembersByPaymentStatus(PaymentStatus paymentStatus) {
        List<Member> members = memberRepository.findAll();
        if (paymentStatus == PaymentStatus.COMPLETE) {
            return paymentService.getMembersPaidList(members);
        } else if (paymentStatus == PaymentStatus.PENDING) {
            return paymentService.getMembersPendingList(members);
        } else {
            System.out.println("Invalid payment status.");
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves and displays the payment summary.
     */
    public void viewPaymentSummary() {
        List<Member> members = memberRepository.findAll();  // Get all members
        String summary = paymentService.getPaymentSummary(members);  // Get the summary from the service
        System.out.println("\n--- Payment Summary ---");
        System.out.println(summary);  // Display the summary
    }

    /**
     * Set a payment reminder for a member.
     *
     * @param memberId         The ID of the member.
     * @param reminderMessage  The reminder message.
     */
    public void setPaymentReminder(int memberId, String reminderMessage) {
        // Pass the reminder logic to the PaymentService for setting reminders
        paymentService.setPaymentReminder(memberId, reminderMessage);
        System.out.println("Reminder set for Member ID: " + memberId);
    }

    /**
     * View all reminders for payments.
     */
    public void viewAllReminders() {
        List<String> reminders = paymentService.getAllReminders();
        if (reminders.isEmpty()) {
            System.out.println("No reminders set.");
        } else {
            System.out.println("--- Payment Reminders ---");
            reminders.forEach(System.out::println);
        }
    }

    /**
     * Remove a specific reminder for a member.
     *
     * @param memberId    The ID of the member.
     * @param reminderMessage The reminder message to remove.
     */
    public void removePaymentReminder(int memberId, String reminderMessage) {
        paymentService.removeReminder(memberId, reminderMessage);
    }

    /**
     * Clear all reminders.
     */
    public void clearAllReminders() {
        paymentService.clearAllReminders();
        System.out.println("All reminders cleared.");
    }
}
