package swimclub.services;

import swimclub.models.Member;
import swimclub.models.MembershipLevel;
import swimclub.repositories.MemberRepository;
import swimclub.utilities.Validator;

public class MemberService {
    private final MemberRepository repository;

    // Constructor to initialize the repository
    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    /**
     * Registers a new member after validation.
     * This method assigns the next available member ID automatically, validates the data,
     * determines the membership level based on age, and then saves the member to the repository.
     *
     * @param member The member to register.
     */
    public void registerMember(Member member) {
        // Assign the next available member ID automatically
        int nextMemberId = repository.getNextMemberId();
        member.setMemberId(nextMemberId); // Set the next available member ID

        // Debug: Check age
        System.out.println("Registering member with Age: " + member.getAge());

        // Validate member data before saving using the Validator class
        try {
            Validator.validateMemberData(member.getName(), member.getAge(),
                    member.getMembershipType().toString(), member.getEmail(), member.getPhoneNumber());
        } catch (IllegalArgumentException e) {
            System.out.println("Error registering member: " + e.getMessage());
            return; // Don't proceed if validation fails
        }

        // Automatically set membership level based on age
        setMembershipLevelBasedOnAge(member);
        System.out.println("Membership level set to: " + member.getMembershipType().getLevel());

        // Save validated member to the repository
        repository.save(member); // Save validated member
    }

    /**
     * Update an existing member's details.
     * This method validates the updated member data, applies age-based membership level logic,
     * and then updates the member in the repository.
     *
     * @param updatedMember The updated member data.
     */
    public void updateMember(Member updatedMember) {
        // Validate updated member data before updating using the Validator class
        try {
            Validator.validateMemberData(updatedMember.getName(), updatedMember.getAge(),
                    updatedMember.getMembershipType().toString(), updatedMember.getEmail(), updatedMember.getPhoneNumber());
        } catch (IllegalArgumentException e) {
            // Handle the validation exception and log the error
            System.out.println("Error updating member: " + e.getMessage());
            return; // Don't proceed if validation fails
        }

        // Automatically set membership level based on age
        setMembershipLevelBasedOnAge(updatedMember);

        // Update the member in the repository
        repository.update(updatedMember); // Update the member in the repository
    }

    /**
     * Determines the membership level based on age.
     * If age > 18, set the level to Senior. If age <= 18, set the level to Junior.
     *
     * @param member The member whose membership level will be determined based on age.
     */
    private void setMembershipLevelBasedOnAge(Member member) {
        // Debug log to check if logic is being triggered
        System.out.println("Setting membership level based on age: " + member.getAge());

        if (member.getAge() > 18) {
            member.getMembershipType().setLevel(MembershipLevel.SENIOR);
        } else {
            member.getMembershipType().setLevel(MembershipLevel.JUNIOR);
        }
    }
}

