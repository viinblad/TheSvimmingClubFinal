package swimclub.controllers;

import swimclub.models.JuniorMember;
import swimclub.models.Member;
import swimclub.models.MembershipType;
import swimclub.models.SeniorMember;
import swimclub.repositories.MemberRepository;
import swimclub.services.MemberService;
import swimclub.utilities.Validator;

public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // Constructor to initialize the service and repository
    public MemberController(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    /**
     * Registers a new member after validating the input.
     *
     * @param name           Full name of the member.
     * @param email          Email address of the member.
     * @param membershipType Membership type (e.g., Junior Competitive).
     * @param age            Age of the member.
     * @param phoneNumber    Phone number of the member.
     */
    public void registerMember(String name, String email, String membershipType, int age, int phoneNumber) {
        try {
            // Validate member data
            Validator.validateMemberData(name, age, membershipType, email, phoneNumber);

            // Parse the membershipType into a MembershipType object
            MembershipType type = MembershipType.fromString(membershipType);

            // Generate the next available member ID
            int memberId = memberRepository.getNextMemberId();
            String memberIdString = String.valueOf(memberId); // Convert memberId to String

            // Dynamically create a JuniorMember or SeniorMember based on age
            Member newMember;
            if (age > 18) {
                newMember = new SeniorMember(memberIdString, name, email, type, age, phoneNumber);
            } else {
                newMember = new JuniorMember(memberIdString, name, email, type, age, phoneNumber);
            }

            // Save the validated member using the MemberService
            memberService.registerMember(newMember);

            // Reload members after registration to immediately reflect the changes
            memberRepository.reloadMembers();

            System.out.println("Member registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Update an existing member after validating the input.
     *
     * @param memberId       The ID of the member to update.
     * @param newName        The new name of the member.
     * @param newEmail       The new email of the member.
     * @param newAge         The new age of the member.
     * @param newPhoneNumber The new phone number of the member.
     */
    public void updateMember(int memberId, String newName, String newEmail, int newAge, String newMembershipType, int newPhoneNumber) {
        try {
            // Validate updated member data
            Validator.validateMemberData(newName, newAge, newMembershipType, newEmail, newPhoneNumber);

            // Find the existing member by ID
            Member memberToUpdate = memberRepository.findById(memberId);

            if (memberToUpdate != null) {
                // Update member details
                memberToUpdate.setName(newName);
                memberToUpdate.setEmail(newEmail);
                memberToUpdate.setAge(newAge);

                // Update membership type
                MembershipType membershipType = MembershipType.fromString(newMembershipType);
                memberToUpdate.setMembershipType(membershipType);

                memberToUpdate.setPhoneNumber(newPhoneNumber);

                // Update the member using the MemberService
                memberService.updateMember(memberToUpdate);

                // Reload members after update to immediately reflect the changes
                memberRepository.reloadMembers();

                System.out.println("Member updated successfully.");
            } else {
                System.out.println("Member not found with ID: " + memberId);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * View all members stored in the repository.
     */
    public void viewAllMembers() {
        System.out.println("\n--- All Registered Members ---");
        memberRepository.findAll().forEach(member ->
                System.out.println("ID: " + member.getMemberId() + ", Name: " + member.getName() + ", Membership: " + member.getMembershipDescription()));
    }
}
