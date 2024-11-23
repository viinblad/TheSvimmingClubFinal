package swimclub;

import swimclub.models.*;
import swimclub.repositories.MemberRepository;
import swimclub.services.MemberService;
import swimclub.utilities.FileHandler;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Initialize FileHandler and Repository
        String filePath = "src/main/resources/members.txt";
        FileHandler fileHandler = new FileHandler(filePath);  // Directly instantiate FileHandler
        MemberRepository repository = new MemberRepository(fileHandler);  // Pass FileHandler to repository
        MemberService memberService = new MemberService(repository);

        // Create new member and register
        MembershipType competitiveJunior = new MembershipType(MembershipCategory.COMPETITIVE, MembershipLevel.JUNIOR);
        Member juniorMember = new JuniorMember("1", "Alice", "alice@example.com", competitiveJunior, 15, 12345678);

        System.out.println("Registering new member...");
        memberService.registerMember(juniorMember); // Save new member

        // Retrieve and print all members (to confirm registration)
        System.out.println("\nAll Members After Registration:");
        List<Member> members = repository.findAll();
        for (Member member : members) {
            System.out.println("Member: " + member.getName() + " - " + member.getMembershipDescription());
        }

        // Update the member's information
        System.out.println("\nUpdating member information...");
        juniorMember.setName("Alice Updated");
        juniorMember.setEmail("alice_updated@example.com");
        juniorMember.setAge(16); // Update age
        juniorMember.setPhoneNumber(98765432); // Update phone number

        memberService.updateMember(juniorMember); // Update member details

        // Retrieve and print all members after the update
        System.out.println("\nAll Members After Update:");
        members = repository.findAll();
        for (Member member : members) {
            System.out.println("Member: " + member.getName() + " - " + member.getMembershipDescription());
        }

        // Try to find the member by ID
        System.out.println("\nFinding Member by ID (1):");
        Member foundMember = repository.findById(1);
        System.out.println("Found Member: " + foundMember.getName() + " - " + foundMember.getMembershipDescription());
    }
}
