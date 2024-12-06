package swimclub.repositories;

import swimclub.models.Member;
import swimclub.models.Payment;
import swimclub.models.PaymentStatus;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class PaymentRepository {
    private static final Logger LOGGER = Logger.getLogger(PaymentRepository.class.getName());
    private final List<Payment> payments;  // List to store payments
    private final List<String> reminders; // List to store reminders
    private final String reminderFilePath; // Path to the reminders file

    // ===========================
    // Constructor and Initialization
    // ===========================

    /**
     * Constructor for PaymentRepository.
     *
     * @param reminderFilePath The file path where reminders are saved and loaded.
     */
    public PaymentRepository(String reminderFilePath) {
        this.payments = new ArrayList<>();
        this.reminders = new ArrayList<>();
        this.reminderFilePath = reminderFilePath;

        // Load reminders at initialization
        loadReminders();
    }

    // ===========================
    // Reminder Management Methods
    // ===========================

    /**
     * Saves a payment reminder.
     *
     * @param reminder The reminder string to save.
     */
    public void saveReminder(String reminder) {
        if (reminder == null || reminder.isEmpty()) {
            throw new IllegalArgumentException("Reminder cannot be null or empty.");
        }
        reminders.add(reminder);
        saveRemindersToFile();
        LOGGER.info("Reminder saved: " + reminder);
    }

    /**
     * Gets all reminders as a list of strings.
     *
     * @return List of reminders.
     */
    public List<String> getReminders() {
        return new ArrayList<>(reminders); // Return a copy to prevent external modification
    }

    /**
     * Removes a specific reminder.
     *
     * @param reminder The reminder to remove.
     * @return true if the reminder was found and removed, false otherwise.
     */
    public boolean removeReminder(String reminder) {
        boolean removed = reminders.remove(reminder);
        if (removed) {
            saveRemindersToFile();
        }
        return removed;
    }

    /**
     * Clears all reminders.
     */
    public void clearReminders() {
        reminders.clear();
        saveRemindersToFile();
        LOGGER.info("All reminders cleared.");
    }

    // ===========================
    // File Handling for Reminders
    // ===========================

    /**
     * Saves all reminders to the file.
     */
    private void saveRemindersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reminderFilePath))) {
            for (String reminder : reminders) {
                writer.write(reminder);
                writer.newLine();
            }
        } catch (IOException e) {
            LOGGER.severe("Error saving reminders to file: " + e.getMessage());
        }
    }

    /**
     * Loads reminders from the file.
     */
    private void loadReminders() {
        try (BufferedReader reader = new BufferedReader(new FileReader(reminderFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                reminders.add(line);
            }
        } catch (FileNotFoundException e) {
            LOGGER.info("Reminder file not found, starting with an empty list.");
        } catch (IOException e) {
            LOGGER.severe("Error loading reminders from file: " + e.getMessage());
        }
    }

    // ===========================
    // Payment Management Methods
    // ===========================

    /**
     * Saves a payment to the repository.
     *
     * @param payment The payment object to save.
     * @throws IllegalArgumentException If the payment is null or a duplicate Payment ID exists.
     */
    public void save(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null.");
        }

        // Check for duplicate payment ID
        if (payments.stream().anyMatch(p -> p.getPaymentId() == payment.getPaymentId())) {
            LOGGER.warning("Duplicate payment attempt for Payment ID: " + payment.getPaymentId());
            return;
        }

        payments.add(payment);
        LOGGER.info("Payment added successfully with ID: " + payment.getPaymentId());
    }

    /**
     * Loads payments from a file and associates them with members.
     *
     * @param filePath         The path to the payment file.
     * @param memberRepository The member repository to link payments with members.
     */
    public void loadPayments(String filePath, MemberRepository memberRepository) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Payment payment = parsePayment(line, memberRepository);
                if (payment != null) {
                    payments.add(payment);
                    Member member = payment.getMember();
                    if (member != null) {
                        member.setPaymentStatus(payment.getPaymentStatus());
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.severe("Error loading payments: " + e.getMessage());
        }
    }

    /**
     * Parses a payment from a string and associates it with a member.
     *
     * @param line             The string containing payment details.
     * @param memberRepository The repository to find members by ID.
     * @return A Payment object parsed from the string or null if parsing fails.
     */
    private Payment parsePayment(String line, MemberRepository memberRepository) {
        String[] parts = line.split(";");
        try {
            int paymentId = Integer.parseInt(parts[0]);
            int memberId = Integer.parseInt(parts[1]);
            double amount = Double.parseDouble(parts[2]);
            LocalDate paymentDate = LocalDate.parse(parts[3]);
            PaymentStatus status = PaymentStatus.valueOf(parts[4].toUpperCase());

            Member member = memberRepository.findById(memberId);
            if (member == null) {
                throw new IllegalArgumentException("Member not found for ID: " + memberId);
            }

            return new Payment(paymentId, status, member, paymentDate, amount);
        } catch (Exception e) {
            LOGGER.severe("Error parsing payment: " + line + " - " + e.getMessage());
            return null;
        }
    }

    // ===========================
    // Payment Retrieval Methods
    // ===========================

    /**
     * Fetches all payments for a specific member ID.
     *
     * @param memberId The member ID to fetch payments for.
     * @return List of payments for the specified member ID.
     */
    public List<Payment> findPaymentsByMemberId(int memberId) {
        return payments.stream()
                .filter(payment -> payment.getMember().getMemberId() == memberId)
                .toList();
    }

    /**
     * Gets all payments in the repository.
     *
     * @return List of all payments.
     */
    public List<Payment> findAll() {
        return new ArrayList<>(payments); // Return a copy to prevent external modification
    }

    /**
     * Gets the next available payment ID.
     *
     * @return The next available payment ID.
     */
    public int getNextPaymentId() {
        return payments.stream()
                .mapToInt(Payment::getPaymentId)
                .max()
                .orElse(0) + 1;
    }
}