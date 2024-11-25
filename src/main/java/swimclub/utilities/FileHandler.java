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
                // Use formatMember to ensure consistent formatting
                writer.write(formatMember(member));
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
     * @param members
     */
    public boolean deleteMember(Member memberToDelete) {
        boolean memberDeleted = false;
        List<Member> members = loadMembers(); // Load all members from file

        List<Member> updatedMembers = new ArrayList<>();
        int id = memberToDelete.getMemberId(); // Get det id for the member to delete

        //Sorting through all the members
        for (Member member : members) {
            if (member.getMemberId() == id) { // Check if the current member matches the ID
                memberDeleted = true; // Mark the member as deleted
            } else {
                updatedMembers.add(member); // add the remaining members to the updated list
            }
        }
        if (memberDeleted){
            saveMembers(updatedMembers); //Save the updated list without the deleted member
            return true; // return true indicating the deletion was succesful
        } else {
            return false; // if no member was deleted, return false
        }

    }

    /**
     * Converts a Member object into a string format for saving to the file.
     *
     * @param member The Member object to format.
     * @return A string representation of the Member object.
     */
    private String formatMember(Member member) {
        // Format the membership description
        String membershipDescription = member.getMembershipType().getLevel() + " " +
                member.getMembershipType().getCategory() + " Swimmer";

        return member.getMemberId() + ";" + member.getName() + ";" + member.getEmail() + ";" +
                member.getAge() + ";" + member.getPhoneNumber() + ";" + membershipDescription;
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
            return null;
        }

        // Parse fields
        int id = parseIntOrDefault(parts[0]);  // Member ID
        String name = parts[1];
        String email = parts[2];
        int age = parseIntOrDefault(parts[3]);  // Age
        int phoneNumber = parseIntOrDefault(parts[4]);  // Phone number
        String membershipDescription = parts[5];

        // Parse the membership type
        String[] membershipParts = membershipDescription.split(" ");
        if (membershipParts.length != 3) {
            System.err.println("Invalid membership description format: " + membershipDescription);
            return null;
        }

        MembershipCategory category = MembershipCategory.valueOf(membershipParts[1].toUpperCase()); // Competitive/Exercise
        MembershipLevel level = MembershipLevel.valueOf(membershipParts[0].toUpperCase()); // Junior/Senior

        MembershipType membershipType = new MembershipType(category, level);

        // Create the correct subclass based on level
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
