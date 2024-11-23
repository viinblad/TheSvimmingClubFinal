package swimclub.utilities;

import swimclub.models.Member;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private final String filePath;

    public FileHandler(String filePath) {
        this.filePath = filePath;
    }

    // Save all members to file
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

    // Load members from file
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

    // Format a Member object into a string for saving
    private String formatMember(Member member) {
        return member.getMemberId() + ";" + member.getName() + ";" + member.getAge() + ";" + member.getMembershipType();
    }

    // Parse a string from file back into a Member object
    private Member parseMember(String line) {
        String[] parts = line.split(";");
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        int age = Integer.parseInt(parts[2]);
        String membershipType = parts[3];
        return new Member(id, name, age, membershipType) {
            @Override
            public String getMembershipDescription() {
                return "";
            }
        };
    }
}
