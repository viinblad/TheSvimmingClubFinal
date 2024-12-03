package swimclub.repositories;

import swimclub.models.Member;
import swimclub.models.MembershipLevel;
import swimclub.models.MembershipType;
import swimclub.Utilities.FileHandler;

import java.util.List;
import java.util.Optional;

public class MemberRepository {
    private List<Member> members;
    private final FileHandler fileHandler;

    /**
     * Constructor for MemberRepository, initializes the file handler and loads members from the file.
     *
     * @param fileHandler The file handler used to load and save members.
     */
    public MemberRepository(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.members = fileHandler.loadMembers(); // Load members from file at startup
    }

    /**
     * Get the next available member ID based on the highest existing ID.
     * If there are no members, it starts from 1.
     *
     * @return The next available member ID as an integer.
     */
    public int getNextMemberId() {
        // If there are no members, return 1 as the first member ID
        if (members.isEmpty()) {
            return 1; // If no members, the first ID is 1
        }

        // Find the highest member ID
        int maxId = members.stream()
                .mapToInt(Member::getMemberId)
                .max()
                .orElse(0); // Find the highest ID, default to 0 if no members

        return maxId + 1; // Return the next ID
    }

    /**
     * Save a new member to the repository and persist the change to the file.
     *
     * @param member The member to be saved.
     */
    public void save(Member member) {
        ensureCorrectMembershipLevel(member); // Ensure the member has the correct membership level
        members.add(member); // Add the member to the list
        fileHandler.saveMembers(members); // Save the updated list to the file
        reloadMembers(); // Reload to keep the in-memory list updated
    }

    /**
     * Delete a member from the repository and persist the change to the file.
     *
     * @param member The member to delete.
     * @return True if the member was deleted, false otherwise.
     */
    public boolean delete(Member member) {

        fileHandler.deleteMember(member); // Delete the member using the file handler
        return true; // Assume successful deletion
    }

    /**
     * Ensure the membership level (Junior or Senior) is correct based on the member's age.
     *
     * @param member The member whose membership level is to be validated and corrected.
     */
    public void ensureCorrectMembershipLevel(Member member) {
        MembershipType membershipType = member.getMembershipType();
        MembershipLevel correctLevel = (member.getAge() > 18) ? MembershipLevel.SENIOR : MembershipLevel.JUNIOR;


        membershipType.setLevel(correctLevel);  // Set the correct level
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
        return member.orElse(null); // Return null if the member is not found
    }

    /**
     * Update an existing member's information in the repository and persist the change.
     *
     * @param updatedMember The member object with updated details.
     * @throws RuntimeException If the member with the given ID is not found.
     */
    public void update(Member updatedMember) {
        Member existingMember = findById(updatedMember.getMemberId());

        if (existingMember == null) {
            throw new RuntimeException("Member not found for ID " + updatedMember.getMemberId());
        }


        ensureCorrectMembershipLevel(updatedMember); // Ensure the correct membership level is set

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

    /**
     * Search for members by their ID, name, or phone number.
     * The search is case-insensitive for name and exact for ID and phone number.
     *
     * @param query The search query (ID, name, or phone number).
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
