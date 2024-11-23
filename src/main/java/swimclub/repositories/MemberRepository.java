package swimclub.repositories;

import swimclub.models.*;
import swimclub.utilities.FileHandler;

import java.util.List;
import java.util.Optional;

public class MemberRepository {
    private final List<Member> members;
    private final FileHandler fileHandler;

    /**
     * Constructor for MemberRepository, initializes file handler and loads members.
     *
     * @param fileHandler The FileHandler to be used for loading and saving member data.
     */
    public MemberRepository(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.members = fileHandler.loadMembers(); // Load members from file at startup
    }

    /**
     * Save a new member to the list and the file.
     *
     * @param member The member to save.
     */
    public void save(Member member) {
        // Check if member already exists by memberId
        if (findById(member.getMemberId()) != null) {
            throw new RuntimeException("Member with ID " + member.getMemberId() + " already exists.");
        }

        members.add(member); // Add the member to the list
        fileHandler.saveMembers(members); // Save changes to file
    }

    /**
     * Find a member by their ID.
     *
     * @param id The ID of the member.
     * @return The found member.
     */
    public Member findById(int id) {
        // Using stream to search for the member by ID
        Optional<Member> member = members.stream()
                .filter(m -> m.getMemberId() == id)
                .findFirst();
        return member.orElse(null); // Return null if not found
    }

    /**
     * Update an existing member's data and save the updated list.
     *
     * @param updatedMember The updated member data.
     */
    public void update(Member updatedMember) {
        Member existingMember = findById(updatedMember.getMemberId());

        if (existingMember == null) {
            throw new RuntimeException("Member not found for ID " + updatedMember.getMemberId());
        }

        // Update the member details
        existingMember.setName(updatedMember.getName());
        existingMember.setAge(updatedMember.getAge());
        existingMember.setMembershipType(updatedMember.getMembershipType());
        existingMember.setEmail(updatedMember.getEmail());
        existingMember.setPhoneNumber(updatedMember.getPhoneNumber());

        fileHandler.saveMembers(members); // Save updated list to file
    }

    /**
     * Retrieve all members.
     *
     * @return List of all members.
     */
    public List<Member> findAll() {
        return members;
    }
}
