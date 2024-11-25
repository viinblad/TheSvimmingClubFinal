package swimclub.controllers;

import swimclub.models.*;
import swimclub.services.MemberService;
import swimclub.utilities.Validator;
import swimclub.repositories.MemberRepository;

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

            // Create a new member (e.g., JuniorMember)
            Member newMember = new JuniorMember(memberIdString, name, email, type, age, phoneNumber);

            // Save the validated member using the MemberService
            memberService.registerMember(newMember);

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
    public void updateMember(int memberId, String newName, String newEmail, int newAge, int newPhoneNumber) {
        try {
            // Validate updated member data
            Validator.validateMemberData(newName, newAge, newEmail, newEmail, newPhoneNumber);

            // Find the existing member by ID
            Member memberToUpdate = memberRepository.findById(memberId);

            if (memberToUpdate != null) {
                // Update member details
                memberToUpdate.setName(newName);
                memberToUpdate.setEmail(newEmail);
                memberToUpdate.setAge(newAge);
                memberToUpdate.setPhoneNumber(newPhoneNumber);

                // Update the member using the MemberService
                memberService.updateMember(memberToUpdate);

                System.out.println("Member updated successfully.");
            } else {
                System.out.println("Member not found with ID: " + memberId);
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public boolean deleteMember(int memberId) {
        memberService.deleteMember(memberId);
        Member member = memberRepository.findById(memberId);

        if (member == null){ // If member do not exist return false
            return false;
        }
        memberRepository.delete(member); // If member exist return true
        return true;
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
