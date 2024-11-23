package swimclub.repositories;

import swimclub.models.Member;
import swimclub.utilities.FileHandler;

import java.util.List;
import java.util.Optional;

public class MemberRepository {
    private List<Member> members;
    private final FileHandler fileHandler;

    public MemberRepository() {
        // Specificer filstien
        String resourcePath = "src/main/resources/members.txt";
        this.fileHandler = new FileHandler(resourcePath);
        this.members = fileHandler.loadMembers(); // Indlæs medlemmer fra fil ved opstart
    }

    public void save(Member member) {
        members.add(member);
        fileHandler.saveMembers(members); // Gem ændringerne til filen
    }

    public Member findById(int id) {
        Optional<Member> member = members.stream()
                .filter(m -> m.getMemberId() == id)
                .findFirst();
        return member.orElseThrow(() -> new RuntimeException("Member not found"));
    }

    // Metode til at opdatere et medlems data
    public void update(Member updatedMember) {
        Member existingMember = findById(updatedMember.getMemberId()); // Find den eksisterende member
        existingMember.setName(updatedMember.getName());
        existingMember.setAge(updatedMember.getAge());
        existingMember.setMembershipType(updatedMember.getMembershipType());
        fileHandler.saveMembers(members); // Opdater filen
    }

    public List<Member> findAll() {
        return members;
    }
}
