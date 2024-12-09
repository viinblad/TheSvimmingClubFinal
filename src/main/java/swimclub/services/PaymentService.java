package swimclub.services;

import swimclub.models.Member;
import swimclub.models.MembershipStatus;
import swimclub.models.Payment;
import swimclub.models.PaymentStatus;
import swimclub.repositories.MemberRepository;
import swimclub.repositories.PaymentRepository;
import swimclub.utilities.FileHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling payment-related operations.
 */
public class PaymentService {
    private final PaymentRepository paymentRepository; // Repository for payment data
    private double juniorRate; // Rate for junior members
    private double seniorRate; // Rate for senior members
    private final FileHandler fileHandler; // File handler to read/write payment rates

    /**
     * Constructs a PaymentService instance with the provided PaymentRepository and FileHandler.
     * Initializes the payment rates by loading from the paymentRates.dat file.
     *
     * @param paymentRepository Repository for handling payment data
     * @param fileHandler       Utility to handle file operations
     */
    public PaymentService(PaymentRepository paymentRepository, FileHandler fileHandler) {
        this.paymentRepository = paymentRepository;
        this.fileHandler = fileHandler;
        updatePaymentRatesFromFile(); // Initialize payment rates from the file
    }

    /**
     * Calculates the annual membership fee based on the member's status and age.
     * The fee is determined by the member's status (Active or Passive) and their age.
     *
     * @param member The member whose membership fee is being calculated
     * @return The calculated membership fee
     */
    public double calculateMembershipFee(Member member) {
        if (member.getMembershipStatus() == MembershipStatus.PASSIVE) {
            return 500; // Passive members pay a fixed fee of 500
        }

        if (member.getMembershipStatus() == MembershipStatus.ACTIVE) {
            if (member.getAge() < 18) {
                return juniorRate; // Active members under 18 pay the junior rate
            } else if (member.getAge() < 60) {
                return seniorRate; // Active members aged 18-59 pay the senior rate
            } else {
                return seniorRate * 0.75; // Active members 60+ receive a 25% discount on senior rate
            }
        }

        return 0; // Default case (unlikely to occur if member status is valid)
    }

    /**
     * Registers a payment for a member.
     * This includes verifying the payment amount, creating a payment object, updating the member's payment status, and saving to the file.
     *
     * @param memberId           The ID of the member making the payment
     * @param amount             The payment amount
     * @param memberRepository   Repository to find the member by ID
     * @param paymentFileHandler File handler to save payments
     * @param filePath           Path to the file where payments are stored
     */
    public void registerPayment(int memberId, double amount, MemberRepository memberRepository, FileHandler paymentFileHandler, String filePath) {
        if (amount <= 0) {
            System.out.println("Payment amount must be greater than 0.");
            return;
        }

        // Get the member
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            System.out.println("Member not found with ID: " + memberId);
            return;
        }

        // Create the payment
        Payment newPayment = new Payment(
                paymentRepository.getNextPaymentId(),
                PaymentStatus.COMPLETE,
                member,
                LocalDate.now(),
                amount
        );

        // Save the payment to the repository (in-memory storage)
        paymentRepository.save(newPayment);

        // Update the member's payment status
        updateMemberPaymentStatus(member, PaymentStatus.COMPLETE);

        // Save payments to file
        savePaymentsToFile(paymentFileHandler, filePath);

        System.out.println("Payment of " + amount + " registered for Member ID: " + memberId);
    }

    /**
     * Updates a member's payment status.
     *
     * @param member        The member whose payment status is being updated
     * @param paymentStatus The new payment status to set
     */
    public void updateMemberPaymentStatus(Member member, PaymentStatus paymentStatus) {
        member.setPaymentStatus(paymentStatus);
    }

    /**
     * Saves all payments to a file using the provided FileHandler.
     *
     * @param paymentFileHandler File handler to save payments
     * @param filePath           The file path where payments are stored
     */
    public void savePaymentsToFile(FileHandler paymentFileHandler, String filePath) {
        // Save all payments to the file
        paymentFileHandler.savePayments(paymentRepository.findAll(), filePath);
    }

    /**
     * Displays all payments made by a specific member.
     *
     * @param memberId The ID of the member whose payments are being displayed
     */
    public void viewPaymentsForMember(int memberId) {
        List<Payment> memberPayments = paymentRepository.findPaymentsByMemberId(memberId);

        if (memberPayments.isEmpty()) {
            System.out.println("No payments found for Member ID: " + memberId);
        } else {
            System.out.println("--- Payments for Member ID: " + memberId + " ---");
            for (Payment payment : memberPayments) {
                System.out.println(payment.toString());
            }
        }
    }

    /**
     * Retrieves the total amount of all payments for a specific member.
     *
     * @param memberId The ID of the member whose total payment is being calculated
     * @return The total payment amount for the member
     */
    public double getTotalPaymentsForMember(int memberId) {
        List<Payment> memberPayments = paymentRepository.findPaymentsByMemberId(memberId);
        double total = 0;
        for (Payment payment : memberPayments) {
            total += payment.getAmountPerYear();
        }
        return total;
    }

    /**
     * Retrieves a summary of payments for all members.
     * This includes the count of paid and pending members and the total amount of payments collected.
     *
     * @param memberList The list of all members
     * @return A string representing the payment summary
     */
    public String getPaymentSummary(List<Member> memberList) {
        int paidCount = getMembersPaidList(memberList).size();
        int pendingCount = getMembersPendingList(memberList).size();
        double totalAmount = 0;

        for (Member member : memberList) {
            if (member.getPaymentStatus() == PaymentStatus.COMPLETE) {
                totalAmount += calculateMembershipFee(member);
            }
        }

        return "Total Members Paid: " + paidCount +
                "\nTotal Members Pending: " + pendingCount +
                "\nTotal Payments Collected: " + totalAmount + " DKK";
    }

    /**
     * Retrieves a list of members who have completed their payment.
     *
     * @param memberList The list of all members
     * @return A list of members with a payment status of COMPLETE
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
     * @param memberList The list of all members
     * @return A list of members with a payment status of PENDING
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
     * Sets a payment reminder for a member.
     *
     * @param memberId        The ID of the member to set the reminder for
     * @param reminderMessage The reminder message
     */
    public void setPaymentReminder(int memberId, String reminderMessage) {
        String reminder = "Reminder for Member ID: " + memberId + ": " + reminderMessage;
        paymentRepository.saveReminder(reminder);
        System.out.println("Payment reminder set for Member ID: " + memberId);
    }

    /**
     * Retrieves all payment reminders.
     *
     * @return A list of payment reminders
     */
    public List<String> getAllReminders() {
        return paymentRepository.getReminders();
    }

    /**
     * Removes a specific reminder for a member.
     *
     * @param memberId The ID of the member whose reminder is to be removed
     * @param message  The message of the reminder to remove
     */
    public void removeReminder(int memberId, String message) {
        String reminder = "Reminder for Member ID: " + memberId + ": " + message;
        if (paymentRepository.removeReminder(reminder)) {
            System.out.println("Reminder removed for Member ID: " + memberId);
        } else {
            System.out.println("No such reminder found for Member ID: " + memberId);
        }
    }

    /**
     * Clears all payment reminders.
     */
    public void clearAllReminders() {
        paymentRepository.clearReminders();
        System.out.println("All reminders cleared.");
    }

    /**
     * Setter to update the junior membership rate.
     *
     * @param juniorRate The new junior rate
     */
    public void setJuniorRate(double juniorRate) {
        this.juniorRate = juniorRate;
        saveRatestoFile(); // Save the updated rates to file
    }

    /**
     * Setter to update the senior membership rate.
     *
     * @param seniorRate The new senior rate
     */
    public void setSeniorRate(double seniorRate) {
        this.seniorRate = seniorRate;
        saveRatestoFile(); // Save the updated rates to file
    }

    /**
     * Saves the junior and senior rates to the paymentRates.dat file.
     */
    private void saveRatestoFile() {
        fileHandler.savePaymentRates(juniorRate, seniorRate);
    }

    /**
     * Updates the payment rates by loading them from the paymentRates.dat file.
     */
    public void updatePaymentRatesFromFile() {
        double[] rates = fileHandler.loadPaymentRates();
        this.juniorRate = rates[0];
        this.seniorRate = rates[1];
    }

    /**
     * Retrieves the current payment rates for junior and senior memberships.
     *
     * @return An array containing the junior and senior rates
     */
    public double[] getPaymentRates() {
        double[] paymentRates = new double[2];
        paymentRates[0] = this.juniorRate;
        paymentRates[1] = this.seniorRate;
        return paymentRates;
    }
}