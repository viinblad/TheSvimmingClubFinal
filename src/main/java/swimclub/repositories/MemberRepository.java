package swimclub.repositories;

import swimclub.models.Member;
import swimclub.models.MembershipLevel;
import swimclub.models.MembershipType;
import swimclub.utilities.FileHandler;

import java.util.List;
import java.util.Optional;

public class MemberRepository {
    private List<Member> members;
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

        // Reload members from the file to keep in-memory list updated
        reloadMembers();
    }

    /**
     * Delete a member from the list and th file.
     *
     * @param member delete the member
     */
    public boolean delete(Member member) {

        fileHandler.deleteMember(member);
        return true;
    }

    /**
     * Ensure the membership level is set correctly based on the member's age.
     * This ensures that a member is assigned to Senior or Junior based on age.
     *
     * @param member The member whose level needs to be set correctly.
     */
    public void ensureCorrectMembershipLevel(Member member) {
        MembershipType membershipType = member.getMembershipType();
        MembershipLevel correctLevel = (member.getAge() > 18) ? MembershipLevel.SENIOR : MembershipLevel.JUNIOR;

        // Update the level dynamically
        membershipType.setLevel(correctLevel);
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

        // Reload members from the file to keep in-memory list updated
        reloadMembers();
    }

    /**
     * Retrieve all members.
     *
     * @return List of all members.
     */
    public List<Member> findAll() {
        return members;
    }

    /**
     * Reload the list of members from the file to ensure that the in-memory list is up-to-date.
     */
    public void reloadMembers() {
        this.members = fileHandler.loadMembers(); // Reload members from the file
    }
    // Other methods...

    /**
     * Searches members by ID, name, or phone number.
     *
     * @param query The search query to match.
     * @return A list of members matching the query.
     */
    public List<Member> search(String query) {
        return members.stream()
                .filter(member -> {
                    // Match ID (converted to String for comparison)
                    String memberId = String.valueOf(member.getMemberId());
                    if (memberId.equalsIgnoreCase(query)) {
                        return true;
                    }

                    // Match name (case-insensitive)
                    if (member.getName().equalsIgnoreCase(query)) {
                        return true;
                    }

                    // Match phone number (converted to String for comparison)
                    String phoneNumber = String.valueOf(member.getPhoneNumber());
                    return phoneNumber.equalsIgnoreCase(query);
                })
                .toList(); // Collect matching members into a list
    }
}
