package swimclub.services;

import swimclub.models.Member;
import swimclub.models.MembershipLevel;
import swimclub.repositories.MemberRepository;
import swimclub.utilities.Validator;

import java.util.List;

/**
 * Service class for managing swim club members.
 * This class provides functionality to register, update, delete, search, and manage member-related actions.
 */
public class MemberService {
    private final MemberRepository repository;  // Repository to manage member data

    /**
     * Constructor to initialize the member service with the provided member repository.
     *
     * @param repository The repository to interact with member data.
     */
    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    /**
     * Registers a new member after validation.
     * This method assigns the next available member ID automatically, validates the data,
     * determines the membership level based on age, and then saves the member to the repository.
     *
     * @param member The member to register.
     * @throws IllegalArgumentException If the member data is invalid during validation.
     */
    public void registerMember(Member member) {
        // Assign the next available member ID automatically
        int nextMemberId = repository.getNextMemberId();
        member.setMemberId(nextMemberId);  // Set the next available member ID

        // Debug: Print out the age to track registration process
        System.out.println("Registering member with Age: " + member.getAge());

        // Validate member data before saving using the Validator class
        try {
            // Validate all member data including MembershipStatus and PaymentStatus
            Validator.validateMemberData(
                    member.getName(),
                    member.getAge(),
                    member.getMembershipType().toString(),
                    member.getEmail(),
                    member.getCity(),
                    member.getStreet(),
                    member.getRegion(),
                    member.getZipcode(),
                    member.getPhoneNumber(),
                    member.getMembershipStatus(),
                    member.getActivityType().toString(),
                    member.getPaymentStatus()
            );
        } catch (IllegalArgumentException e) {
            // Handle validation error
            System.out.println("Error registering member: " + e.getMessage());
            return;  // Don't proceed if validation fails
        }

        // Automatically set membership level based on age
        setMembershipLevelBasedOnAge(member);
        System.out.println("Membership level set to: " + member.getMembershipType().getLevel());

        // Save validated member to the repository
        repository.save(member);  // Save the validated member to the repository
    }

    /**
     * Updates an existing member's details.
     * This method validates the updated member data, applies age-based membership level logic,
     * and then updates the member in the repository.
     *
     * @param updatedMember The updated member data.
     * @throws IllegalArgumentException If the updated member data is invalid during validation.
     */
    public void updateMember(Member updatedMember) {
        // Validate updated member data before updating using the Validator class
        try {
            // Validate all updated member data
            Validator.validateMemberData(
                    updatedMember.getName(),
                    updatedMember.getAge(),
                    updatedMember.getMembershipType().toString(),
                    updatedMember.getEmail(),
                    updatedMember.getCity(),
                    updatedMember.getStreet(),
                    updatedMember.getRegion(),
                    updatedMember.getZipcode(),
                    updatedMember.getPhoneNumber(),
                    updatedMember.getMembershipStatus(),
                    updatedMember.getActivityType().toString(),
                    updatedMember.getPaymentStatus()
            );
        } catch (IllegalArgumentException e) {
            // Handle validation error
            System.out.println("Error updating member: " + e.getMessage());
            return;  // Don't proceed if validation fails
        }

        // Automatically set membership level based on age
        setMembershipLevelBasedOnAge(updatedMember);

        // Update the member in the repository
        repository.update(updatedMember);
    }

    /**
     * Deletes a member by their ID.
     *
     * @param memberId The ID of the member to delete.
     * @throws RuntimeException If the member cannot be found by the ID.
     */
    public void deleteMember(int memberId) {
        // Find the member by ID
        Member member = repository.findById(memberId);

        // If member is not found, display a message and return
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        // Delete the member from the repository
        repository.delete(member);
        System.out.println("Member deleted successfully.");
    }

    /**
     * Determines the membership level based on age.
     * If age > 18, set the level to Senior. If age <= 18, set the level to Junior.
     *
     * @param member The member whose membership level will be determined based on age.
     */
    private void setMembershipLevelBasedOnAge(Member member) {
        System.out.println("Setting membership level based on age: " + member.getAge());

        // If member's age is over 18, assign Senior membership, else assign Junior membership
        if (member.getAge() > 18) {
            member.getMembershipType().setLevel(MembershipLevel.SENIOR);
        } else {
            member.getMembershipType().setLevel(MembershipLevel.JUNIOR);
        }
    }

    /**
     * Searches for members by ID, name, or phone number.
     *
     * @param query The search query.
     * @return A list of members matching the query.
     */
    public List<Member> searchMembers(String query) {
        // Delegate to repository to search members by the provided query
        return repository.search(query);
    }

    /**
     * Views all payments made by a specific member.
     *
     * @param memberId The ID of the member whose payments are to be retrieved.
     */
    public void viewPaymentsForMember(int memberId) {
        // Find the member by ID
        Member member = repository.findById(memberId);
        if (member == null) {
            System.out.println("Member not found with ID: " + memberId);
            return;
        }

        // Assuming you have a PaymentRepository to fetch payments (not implemented here)
        // List<Payment> payments = PaymentRepository.findPaymentsByMemberId(memberId); // Fetch payments from the repository

        System.out.println("Displaying payments for member ID: " + memberId);
        // Assuming that the PaymentRepository is implemented elsewhere:
        // payments.forEach(payment -> System.out.println(payment));
    }
}