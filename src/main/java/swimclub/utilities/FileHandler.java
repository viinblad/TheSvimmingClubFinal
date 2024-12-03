package swimclub.utilities;

import swimclub.models.*;
import swimclub.repositories.MemberRepository;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler handles saving and loading Member, Payment, and Reminder data to and from a file.
 */
public class FileHandler {
    private String memberFilePath;
    private String paymentFilePath;
    private String reminderFilePath;// Added for reminder file
    private String paymentRatesFilePath;

    /**
     * Constructor for FileHandler.
     *
     * @param memberFilePath   Path to the file for saving/loading member data.
     * @param paymentFilePath  Path to the file for saving/loading payment data.
     * @param reminderFilePath Path to the file for saving/loading reminder data.
     */
    public FileHandler(String memberFilePath, String paymentFilePath, String reminderFilePath, String paymentRatesFilePath) {
        this.memberFilePath = memberFilePath;
        this.paymentFilePath = paymentFilePath;
        this.reminderFilePath = reminderFilePath;
        this.paymentRatesFilePath = paymentRatesFilePath;
    }


    // ---------------------------
    // Member Related Methods
    // ---------------------------

    /**
     * Saves all members to the specified file.
     *
     * @param members List of Member objects to save.
     */
    public void saveMembers(List<Member> members) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(memberFilePath))) {
            for (Member member : members) {
                writer.write(formatMember(member)); // Format and save each member
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving members: " + e.getMessage());
        }
    }

    /**
     * Loads members from the specified file.
     *
     * @return List of Member objects loaded from the file.
     */
    public List<Member> loadMembers() {
        List<Member> members = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(memberFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Member member = parseMember(line);
                    if (member != null) {
                        members.add(member);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading members: " + e.getMessage());
        }
        return members;
    }
    /**
     * Deletes a member from the file based on provided memberID.
     *
     * @param memberToDelete The member to delete.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteMember(Member memberToDelete) {
        boolean memberDeleted = false;
        List<Member> members = loadMembers(); // Load all members from file

        List<Member> updatedMembers = new ArrayList<>();
        int id = memberToDelete.getMemberId(); // Get the ID for the member to delete

        for (Member member : members) {
            if (member.getMemberId() == id) {
                memberDeleted = true; // Mark the member as deleted
            } else {
                updatedMembers.add(member); // Keep all other members
            }
        }

        if (memberDeleted) {
            saveMembers(updatedMembers); // Save the updated list without the deleted member
        }
        return memberDeleted;
    }

    // ---------------------------
    // Payment Related Methods
    // ---------------------------

    /**
     * Saves all payments to a file.
     *
     * @param payments List of Payment objects.
     * @param filePath Path to the payment file.
     */
    public void savePayments(List<Payment> payments, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Payment payment : payments) {
                writer.write(formatPayment(payment));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving payments: " + e.getMessage());
        }
    }

    /**
     * Loads payments from a file.
     *
     * @param filePath         Path to the payment file.
     * @param memberRepository Repository to link payments to members.
     * @return List of Payment objects.
     */
    public List<Payment> loadPayments(String filePath, MemberRepository memberRepository) {
        List<Payment> payments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Payment payment = parsePayment(line, memberRepository);
                if (payment != null) {
                    payments.add(payment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading payments: " + e.getMessage());
        }
        return payments;
    }

    // ---------------------------
    // Reminder Related Methods
    // ---------------------------

    /**
     * Saves all reminders to the specified file.
     *
     * @param reminders List of reminders to save.
     */
    public void saveReminders(List<String> reminders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reminderFilePath))) {
            for (String reminder : reminders) {
                writer.write(reminder);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving reminders: " + e.getMessage());
        }
    }

    /**
     * Loads reminders from the specified file.
     *
     * @return List of reminders loaded from the file.
     */
    public List<String> loadReminders() {
        List<String> reminders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(reminderFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    reminders.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading reminders: " + e.getMessage());
        }
        return reminders;
    }

    /**
     * Deletes a specific reminder from the file.
     *
     * @param reminder The reminder to delete.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteReminder(String reminder) {
        List<String> reminders = loadReminders();
        boolean reminderDeleted = reminders.remove(reminder);

        if (reminderDeleted) {
            saveReminders(reminders);
        }
        return reminderDeleted;
    }

    // ---------------------------
    // Helper Methods
    // ---------------------------

    private String formatMember(Member member) {
        return member.getMemberId() + ";" +
                member.getName() + ";" +
                member.getEmail() + ";" +
                member.getCity() + ";" +
                member.getStreet() + ";" +
                member.getRegion() + ";" +
                member.getZipcode() + ";" +
                member.getAge() + ";" +
                member.getPhoneNumber() + ";" +
                member.getMembershipType().getLevel() + " " +
                member.getMembershipType().getCategory() + ";" +
                member.getMembershipStatus() + ";" +
                member.getActivityType() + ";" +  // Added ActivityType
                member.getPaymentStatus();
    }

    private Member parseMember(String line) {
        String[] parts = line.split(";");
        if (parts.length < 13) {  // Ensure the expected number of fields
            System.err.println("Skipping invalid member data: " + line);
            return null;
        }

        try {
            // Parse basic member details
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            String email = parts[2];
            String city = parts[3];
            String street = parts[4];
            String region = parts[5];
            int zipcode = Integer.parseInt(parts[6]);
            int age = Integer.parseInt(parts[7]);
            int phoneNumber = Integer.parseInt(parts[8]);

            // Parse membership type (e.g., "SENIOR COMPETITIVE")
            String membershipDescription = parts[9];
            String[] membershipParts = membershipDescription.split(" ");
            MembershipType membershipType = new MembershipType(
                    MembershipCategory.valueOf(membershipParts[1].toUpperCase()),
                    MembershipLevel.valueOf(membershipParts[0].toUpperCase())
            );

            // Parse membership status (e.g., ACTIVE)
            MembershipStatus membershipStatus = MembershipStatus.valueOf(parts[10].toUpperCase());
            ActivityType activityType = ActivityType.valueOf(parts[11].toUpperCase());
            PaymentStatus paymentStatus = PaymentStatus.valueOf(parts[12].toUpperCase());

            // Validate the data
            Validator.validateMemberData(name, age, membershipDescription, email, city, street, region, zipcode,
                    phoneNumber, membershipStatus, activityType.toString(), paymentStatus);

            // Create the correct subclass of Member based on age (Junior or Senior)
            if (membershipType.getLevel() == MembershipLevel.JUNIOR) {
                return new JuniorMember(String.valueOf(id), name, email, city, street, region, zipcode, membershipType,
                        membershipStatus, activityType, paymentStatus, age, phoneNumber);
            } else {
                return new SeniorMember(String.valueOf(id), name, email, city, street, region, zipcode, membershipType,
                        membershipStatus, activityType, paymentStatus, age, phoneNumber);
            }

        } catch (Exception e) {
            System.err.println("Error parsing member: " + line + " - " + e.getMessage());
            return null;
        }
    }


    private String formatPayment(Payment payment) {
        return payment.getPaymentId() + ";" +
                payment.getMember().getMemberId() + ";" +
                payment.getAmountPerYear() + ";" +
                payment.getPaymentDate() + ";" +
                payment.getPaymentStatus();
    }

    private Payment parsePayment(String line, MemberRepository memberRepository) {
        String[] parts = line.split(";");
        try {
            int paymentId = Integer.parseInt(parts[0]);
            int memberId = Integer.parseInt(parts[1]);
            double amount = Double.parseDouble(parts[2]);
            LocalDate paymentDate = LocalDate.parse(parts[3]);
            PaymentStatus status = PaymentStatus.valueOf(parts[4].toUpperCase());
            Member member = memberRepository.findById(memberId);

            return new Payment(paymentId, status, member, paymentDate, amount);
        } catch (Exception e) {
            System.err.println("Error parsing payment: " + line + " - " + e.getMessage());
            return null;
        }
    }




    public double[] loadPaymentRates() {
        double[] rates = new double[2];

        try (BufferedReader reader = new BufferedReader(new FileReader(paymentRatesFilePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Junior Rate:")) {
                    rates[0] = Double.parseDouble(line.split(":")[1].trim());
                } else if (line.startsWith("Senior Rate:")) {
                    rates[1] = Double.parseDouble(line.split(":")[1].trim());
                }
            }

        } catch (IOException e) {
            System.out.println("Error loading payment rates from file: " + e.getMessage());
            rates[0] = 1000;
            rates[1] = 1600;
        }
        return rates;
    }
    public void savePaymentRates(double juniorRate, double seniorRate) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(paymentRatesFilePath, false))) {
            //false means that it overwrites everything in the file every time.
            writer.write("Junior Rate: " + juniorRate);
            writer.newLine();
            writer.write("Senior Rate: " + seniorRate);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving payment rates: " + e.getMessage());
        }
    }

}
