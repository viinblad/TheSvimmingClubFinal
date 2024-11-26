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
                if (!line.trim().isEmpty()) { // Skip empty lines
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

    /**
     * Converts a Member object into a string format for saving to the file.
     *
     * @param member The Member object to format.
     * @return A string representation of the Member object.
     */
    private String formatMember(Member member) {
        String membershipDescription = member.getMembershipType().getLevel() + " " +
                member.getMembershipType().getCategory() + " Swimmer";

        return member.getMemberId() + ";" + member.getName() + ";" + member.getEmail() + ";" +
                member.getCity() + ";" + member.getStreet() + ";" + member.getRegion() + ";" +
                member.getZipcode() + ";" + member.getAge() + ";" + member.getPhoneNumber() + ";" +
                membershipDescription + ";" + member.getMembershipStatus() + ";" + member.getPaymentStatus();
    }

    /**
     * Parses a string from the file into a Member object.
     *
     * @param line A string representing a Member.
     * @return A Member object (either JuniorMember or SeniorMember).
     */
    private Member parseMember(String line) {
        String[] parts = line.split(";");

        if (parts.length < 12) { // Check for all fields, including new ones
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
        PaymentStatus paymentStatus = PaymentStatus.valueOf(parts[11].toUpperCase());

        // Parse the membership type
        String[] membershipParts = membershipDescription.split(" ");
        if (membershipParts.length != 3) {
            System.err.println("Invalid membership description format: " + membershipDescription);
            return null;
        }

        MembershipCategory category = MembershipCategory.valueOf(membershipParts[1].toUpperCase());
        MembershipLevel level = MembershipLevel.valueOf(membershipParts[0].toUpperCase());
        MembershipType membershipType = new MembershipType(category, level);

        // Create the correct subclass based on membership level
        if (level == MembershipLevel.JUNIOR) {
            return new JuniorMember(String.valueOf(id), name, email, city, street, region, zipcode,
                    membershipType, membershipStatus, paymentStatus, age, phoneNumber);
        } else {
            return new SeniorMember(String.valueOf(id), name, email, city, street, region, zipcode,
                    membershipType, membershipStatus, paymentStatus, age, phoneNumber);
        }
    }

    /**
     * Helper method to parse integers and handle empty or invalid input.
     * Returns a default value if the input is empty or invalid.
     *
     * @param value The string value to parse.
     * @return The parsed integer, or the default value if parsing fails.
     */
    private int parseIntOrDefault(String value) {
        if (value == null || value.trim().isEmpty()) {
            return -1;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing integer value: " + value);
            return -1;
        }
    }
}
