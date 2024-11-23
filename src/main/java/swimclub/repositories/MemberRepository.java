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
     */
    public MemberRepository(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.members = fileHandler.loadMembers(); // Load members from file at startup
    }

    /**
     * Get the next available member ID based on the highest existing ID.
     * This method will return the next available ID (current highest ID + 1).
     *
     * @return The next available member ID.
     */
    public int getNextMemberId() {
        // If there are no members, return 1 as the first member ID
        if (members.isEmpty()) {
            return 1;
        }

        // Find the highest member ID
        int maxId = members.stream()
                .mapToInt(Member::getMemberId)
                .max()
                .orElse(0);

        return maxId + 1; // Return the next ID
    }

    /**
     * Save a new member to the list and the file.
     *
     * @param member The member to save.
     */
    public void save(Member member) {
        // Ensure the correct membership level is set
        ensureCorrectMembershipLevel(member);

        // Add the member to the list
        members.add(member);

        // Save the member to the file
        fileHandler.saveMembers(members);
    }

    /**
     * Ensure the membership level is set correctly based on the member's age.
     * This ensures that a member is assigned to Senior or Junior based on age.
     *
     * @param member The member whose level needs to be set correctly.
     */
    private void ensureCorrectMembershipLevel(Member member) {
        // Automatically set membership level based on age
        if (member.getAge() > 18) {
            member.getMembershipType().setLevel(MembershipLevel.SENIOR);
        } else {
            member.getMembershipType().setLevel(MembershipLevel.JUNIOR);
        }
    }

    /**
     * Find a member by their ID.
     *
     * @param id The ID of the member.
     * @return The found member.
     */
    public Member findById(int id) {
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

        // Ensure the correct membership level is set
        ensureCorrectMembershipLevel(updatedMember);

        // Update the member details
        existingMember.setName(updatedMember.getName());
        existingMember.setAge(updatedMember.getAge());
        existingMember.setMembershipType(updatedMember.getMembershipType());
        existingMember.setEmail(updatedMember.getEmail());
        existingMember.setPhoneNumber(updatedMember.getPhoneNumber());

        // Save updated list to the file
        fileHandler.saveMembers(members);
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
