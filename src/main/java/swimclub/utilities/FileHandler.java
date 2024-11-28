package swimclub.utilities;

import swimclub.models.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler handles saving and loading Member data to and from a file.
 */
public class FileHandler {
    private final String filePath;

    /**
     * Constructor for FileHandler.
     *
     * @param filePath Path to the file for saving/loading member data.
     */
    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    // ---------------------------
    // Member Relaterede Metoder
    // ---------------------------

    /**
     * Saves all members to the specified file.
     *
     * @param members List of Member objects to save.
     */
    public void saveMembers(List<Member> members) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
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
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
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
     * Deletes a member from the file based on the provided memberID.
     *
     * @param memberToDelete The member to delete.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteMember(Member memberToDelete) {
        boolean memberDeleted = false;
        List<Member> members = loadMembers(); // Load all members from file
        boolean memberDeleted = members.removeIf(member -> member.getMemberId() == memberToDelete.getMemberId());

        if (memberDeleted) {
            saveMembers(members); // Save updated members
        }
        return memberDeleted;
    }

    /**
     * Converts a Member object into a string format for saving to the file.
     *
     * @param member The Member object to format.
     * @return A string representation of the Member object.
     */
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
                member.getPaymentStatus();
    }

    /**
     * Parses a string from the file into a Member object.
     *
     * @param line A string representing a Member.
     * @return A Member object (either JuniorMember or SeniorMember).
     */
    private Member parseMember(String line) {
        String[] parts = line.split(";");

        if (parts.length < 13) { // Check for all fields, including new ones
            System.err.println("Skipping invalid member data (not enough fields): " + line);
            return null; // Skip invalid member data
        }

        // Parse fields
        int id = parseIntOrDefault(parts[0]);
        String name = parts[1];
        String email = parts[2];
        String city = parts[3];
        String street = parts[4];
        String region = parts[5];
        int zipcode = parseIntOrDefault(parts[6]);
        int age = parseIntOrDefault(parts[7]);
        int phoneNumber = parseIntOrDefault(parts[8]);
        String membershipDescription = parts[9];
        MembershipStatus membershipStatus = MembershipStatus.valueOf(parts[10].toUpperCase());
        String activitytypeDescription = parts[11];
        PaymentStatus paymentStatus = PaymentStatus.valueOf(parts[12].toUpperCase());

            String[] membershipParts = membershipDescription.split(" ");
            MembershipType membershipType = new MembershipType(
                    MembershipCategory.valueOf(membershipParts[1].toUpperCase()),
                    MembershipLevel.valueOf(membershipParts[0].toUpperCase())
            );

            Validator.validateMemberData(name, age, membershipType.toString(), email, city, street, region, zipcode, phoneNumber, membershipStatus, paymentStatus);

            if (membershipType.getLevel() == MembershipLevel.JUNIOR) {
                return new JuniorMember(String.valueOf(id), name, email, city, street, region, zipcode, membershipType, membershipStatus, paymentStatus, age, phoneNumber);
            } else {
                return new SeniorMember(String.valueOf(id), name, email, city, street, region, zipcode, membershipType, membershipStatus, paymentStatus, age, phoneNumber);
            }
        } catch (Exception e) {
            System.err.println("Error parsing member: " + line + " - " + e.getMessage());
            return null;
        }

        MembershipCategory category = MembershipCategory.valueOf(membershipParts[1].toUpperCase());
        MembershipLevel level = MembershipLevel.valueOf(membershipParts[0].toUpperCase());
        MembershipType membershipType = new MembershipType(category, level);

        ActivityType activity = ActivityType.valueOf(activitytypeDescription.toUpperCase());
        ActivityTypeData activityType = new ActivityTypeData(activity);

        // Create the correct subclass based on membership level
        if (level == MembershipLevel.JUNIOR) {
            return new JuniorMember(String.valueOf(id), name, email, city, street, region, zipcode,
                    membershipType, membershipStatus,activityType.toActivityType(), paymentStatus, age, phoneNumber);
        } else {
            return new SeniorMember(String.valueOf(id), name, email, city, street, region, zipcode,
                    membershipType, membershipStatus,activityType.toActivityType(), paymentStatus, age, phoneNumber);
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
     * @param filePath          Path to the payment file.
     * @param memberRepository  Repository to link payments to members.
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

            if (member == null) {
                throw new IllegalArgumentException("Member not found for ID: " + memberId);
            }

            return new Payment(paymentId, status, member, paymentDate, amount);
        } catch (Exception e) {
            System.err.println("Error parsing payment: " + line + " - " + e.getMessage());
            return null;
        }
    }
}
