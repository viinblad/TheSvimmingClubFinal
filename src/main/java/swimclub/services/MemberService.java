package swimclub.services;

import swimclub.models.Member;
import swimclub.repositories.MemberRepository;
import swimclub.utilities.Validator;

public class MemberService {
    private final MemberRepository repository;

    public MemberService(MemberRepository repository) {
        this.repository = repository;
    }

    public void registerMember(Member member) {
        // Validate member data before saving
        Validator.validateMemberData(member.getName(), member.getAge(), member.getMembershipType());
        repository.save(member);
    }
}
