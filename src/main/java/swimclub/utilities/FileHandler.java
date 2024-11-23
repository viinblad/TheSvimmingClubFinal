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
                members.add(parseMember(line));
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
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        String email = parts[2];
        int age = Integer.parseInt(parts[3]);
        int phoneNumber = Integer.parseInt(parts[4]);
        String membershipType = parts[5];

        // Determine subclass based on membership type
        if (membershipType.toLowerCase().contains("junior")) {
            return new JuniorMember(id, name, email, age, phoneNumber, membershipType.split(" ")[1]);
        } else if (membershipType.toLowerCase().contains("senior")) {
            return new SeniorMember(id, name, email, age, phoneNumber, membershipType.split(" ")[1]);
        } else {
            throw new IllegalArgumentException("Unknown membership type: " + membershipType);
        }
    }
}