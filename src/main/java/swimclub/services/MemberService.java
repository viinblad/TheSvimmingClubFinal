package swimclub.services;

import swimclub.models.Member;
import swimclub.repositories.MemberRepository;
import swimclub.utilities.Validator;

public class MemberService {
    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    /**
     * Registers a new member after validation.
     *
     * @param member The member to register.
     */
    public void registerMember(Member member) {
        // Validate member data before saving
        Validator.validateMemberData(member.getName(), member.getAge(),
                member.getMembershipType().toString(), member.getEmail(), member.getPhoneNumber());
        repository.save(member); // Save validated member
    }

    /**
     * Update an existing member's details.
     *
     * @param updatedMember The updated member data.
     */
    public void updateMember(Member updatedMember) {
        // Validate member data before updating
        Validator.validateMemberData(updatedMember.getName(), updatedMember.getAge(),
                updatedMember.getMembershipType().toString(), updatedMember.getEmail(), updatedMember.getPhoneNumber());
        repository.update(updatedMember); // Update the member in the repository
    }
}
