package swimclub.repositories;

import swimclub.models.Member;
import swimclub.models.Payment;
import swimclub.models.PaymentStatus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class PaymentRepository {
    private static final Logger LOGGER = Logger.getLogger(PaymentRepository.class.getName());
    private final List<Payment> payments;

    public PaymentRepository() {
        this.payments = new ArrayList<>();
    }

    public void save(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null.");
        }

        if (payments.stream().anyMatch(p -> p.getPaymentId() == payment.getPaymentId())) {
            LOGGER.warning("Duplicate payment attempt for Payment ID: " + payment.getPaymentId());
            return;
        }

        payments.add(payment);
        LOGGER.info("Payment added successfully with ID: " + payment.getPaymentId());
    }

    // In PaymentRepository.java
    public void loadPayments(String filePath, MemberRepository memberRepository) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Payment payment = parsePayment(line, memberRepository); // Associate payment with member
                if (payment != null) {
                    payments.add(payment); // Add payment to the in-memory list
                    // Update the member's payment status based on the loaded payment
                    Member member = payment.getMember(); // Get the member from the payment
                    if (member != null) {
                        member.setPaymentStatus(payment.getPaymentStatus());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading payments: " + e.getMessage());
        }
    }

    private Payment parsePayment(String line, MemberRepository memberRepository) {
        String[] parts = line.split(";");
        try {
            int paymentId = Integer.parseInt(parts[0]);
            int memberId = Integer.parseInt(parts[1]);
            double amount = Double.parseDouble(parts[2]);
            LocalDate paymentDate = LocalDate.parse(parts[3]);
            PaymentStatus status = PaymentStatus.valueOf(parts[4].toUpperCase());

            // Find the member associated with this payment
            Member member = memberRepository.findById(memberId);
            if (member == null) {
                throw new IllegalArgumentException("Member not found for ID: " + memberId);
            }

            return new Payment(paymentId, status, member, paymentDate, amount);
        } catch (Exception e) {
            System.err.println("Error parsing payment: " + line + " - " + e.getMessage());
            return null;
        }
    }

    // Fetch payments by memberId
    public List<Payment> findPaymentsByMemberId(int memberId) {
        return payments.stream()
                .filter(payment -> payment.getMember().getMemberId() == memberId)
                .toList();
    }

    // Return all payments in the repository
    public List<Payment> findAll() {
        return new ArrayList<>(payments);
    }

    // Get next available payment ID
    public int getNextPaymentId() {
        return payments.stream()
                .mapToInt(Payment::getPaymentId)
                .max()
                .orElse(0) + 1;
    }
}
