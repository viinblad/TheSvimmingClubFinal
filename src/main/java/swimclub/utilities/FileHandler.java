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
                // Ensure that we format the membership type correctly
                String membershipDescription = member.getMembershipType().getLevel() + " " +
                        member.getMembershipType().getCategory() + " Swimmer";

                // Write the member details in the correct format
                writer.write(member.getMemberId() + ";" + member.getName() + ";" + member.getEmail() + ";" +
                        member.getAge() + ";" + member.getPhoneNumber() + ";" + membershipDescription);
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
                Member member = parseMember(line);
                if (member != null) {

                    // System.out.println("Loaded member: " + member.getName() + " with level: " + member.getMembershipType().getLevel());
                    members.add(member);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading members: " + e.getMessage());
        }
        return members;
    }


    /**
     * Converts a Member object into a string format for saving to the file.
     *
     * @param member The Member object to format.
     * @return A string representation of the Member object.
     */
    private String formatMember(Member member) {
        return member.getMemberId() + ";" + member.getName() + ";" + member.getEmail() + ";" +
                member.getAge() + ";" + member.getPhoneNumber() + ";" +
                member.getMembershipDescription();
    }

    /**
     * Parses a string from the file into a Member object.
     *
     * @param line A string representing a Member.
     * @return A Member object (either JuniorMember or SeniorMember).
     */
    private Member parseMember(String line) {
        String[] parts = line.split(";");

        if (parts.length < 6) {
            System.err.println("Skipping invalid member data (not enough fields): " + line);
            return null; // Skip invalid member data (missing fields)
        }

        // Parse fields
        int id = parseIntOrDefault(parts[0], -1);  // Parse member ID
        String name = parts[1];
        String email = parts[2];
        int age = parseIntOrDefault(parts[3], -1);  // Parse age
        int phoneNumber = parseIntOrDefault(parts[4], -1);  // Parse phone number
        String membershipDescription = parts[5]; // This is the "level category Swimmer" part

        // Handle membership type parsing
        String[] membershipParts = membershipDescription.split(" ");
        if (membershipParts.length != 3) {
            System.err.println("Invalid membership type format: " + membershipDescription);
            return null;
        }

        MembershipCategory category = MembershipCategory.valueOf(membershipParts[1].toUpperCase()); // Competitive or Exercise

        // Dynamically determine membership level based on age (Junior or Senior)
        MembershipLevel level = (age > 18) ? MembershipLevel.SENIOR : MembershipLevel.JUNIOR;

        // Create the MembershipType using the dynamically determined level
        MembershipType membershipType = new MembershipType(category, level);

        // Instantiate the correct subclass based on membership level
        if (level == MembershipLevel.JUNIOR) {
            return new JuniorMember(String.valueOf(id), name, email, membershipType, age, phoneNumber);
        } else {
            return new SeniorMember(String.valueOf(id), name, email, membershipType, age, phoneNumber);
        }
    }

    /**
     * Helper method to parse integers and handle empty or invalid input.
     * Returns a default value if the input is empty or invalid.
     *
     * @param value        The string value to parse.
     * @param defaultValue The default value to return if parsing fails.
     * @return The parsed integer, or the default value if parsing fails.
     */
    private int parseIntOrDefault(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;  // Return default value if the value is empty or null
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing integer value: " + value);
            return defaultValue;  // Return default value if parsing fails
        }
    }

    /**
     * Helper method to parse the membership type and handle errors gracefully.
     *
     * @param membershipDescription The membership description (e.g., "Junior Competitive").
     * @return A valid MembershipType object, or null if invalid.
     */
    private MembershipType parseMembershipType(String membershipDescription) {
        try {
            String[] membershipParts = membershipDescription.split(" ");
            if (membershipParts.length != 2) {
                System.err.println("Invalid membership description format: " + membershipDescription);
                return null;  // Invalid format (must be "Level Category")
            }

            String level = membershipParts[0];  // "Junior" or "Senior"
            String category = membershipParts[1]; // "Competitive" or "Exercise"

            // Create MembershipType
            MembershipLevel membershipLevel = MembershipLevel.valueOf(level.toUpperCase());
            MembershipCategory membershipCategory = MembershipCategory.valueOf(category.toUpperCase());
            return new MembershipType(membershipCategory, membershipLevel);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            System.err.println("Error parsing membership type: " + membershipDescription);
            return null;  // Return null if the membership type is invalid
        }
    }
}
