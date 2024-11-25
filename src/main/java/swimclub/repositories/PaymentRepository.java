package swimclub.repositories;

import swimclub.models.Payment;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository for managing Payment entities.
 */
public class PaymentRepository {
    private final List<Payment> payments;

    // Constructor initializes an empty list of payments
    public PaymentRepository() {
        this.payments = new ArrayList<>();
    }

    /**
     * Adds a new payment to the repository.
     *
     * @param payment The payment to add.
     */
    public void save(Payment payment) {
        payments.add(payment);
    }

    /**
     * Retrieves a payment by its ID.
     *
     * @param paymentId The ID of the payment to retrieve.
     * @return The payment if found, or null otherwise.
     */
    public Payment findById(int paymentId) {
        return payments.stream()
                .filter(p -> p.getPaymentId() == paymentId)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all payments in the repository.
     *
     * @return A list of all payments.
     */
    public List<Payment> findAll() {
        return new ArrayList<>(payments); // Return a copy to avoid external modification
    }

    /**
     * Retrieves all payments for a specific member by their ID.
     *
     * @param memberId The member ID to search for payments.
     * @return A list of payments for the specified member.
     */
    public List<Payment> findPaymentsByMemberId(int memberId) {
        List<Payment> memberPayments = new ArrayList<>();
        for (Payment payment : payments) {
            if (payment.getMember().getMemberId() == memberId) {
                memberPayments.add(payment);
            }
        }
        return memberPayments;
    }

    /**
     * Get the next available payment ID (similar to getNextMemberId)
     *
     * @return The next available payment ID.
     */
    public int getNextPaymentId() {
        return payments.size() + 1;  // Assuming payment IDs are sequential
    }
}
