package swimclub.controllers;

import swimclub.models.*;
import swimclub.repositories.MemberRepository;
import swimclub.services.MemberService;
import swimclub.utilities.Validator;

import java.util.List;

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
     */
    public Member registerMember(String name, String email, String city, String street, String region, int zipcode,
                                 String membershipType, MembershipStatus membershipStatus, String activityType,
                                 PaymentStatus paymentStatus, int age, int phoneNumber) {
        Member returnMember = null;
        try {
            // Validate member data
            Validator.validateMemberData(name, age, membershipType, email, city, street, region, zipcode, phoneNumber,
                    membershipStatus, activityType, paymentStatus);

            // Parse the membershipType into a MembershipType object
            MembershipType type = MembershipType.fromString(membershipType);

            // Parse the activityType into an ActivityTypeData object
            ActivityTypeData activity = ActivityTypeData.fromString(activityType);

            // Generate the next available member ID
            int memberId = memberRepository.getNextMemberId();
            String memberIdString = String.valueOf(memberId); // Convert memberId to String

            // Dynamically create a JuniorMember or SeniorMember based on age
            Member newMember;
            if (age > 18) {
                newMember = new SeniorMember(memberIdString, name, email, city, street, region, zipcode, type,
                        membershipStatus, activity.toActivityType(), paymentStatus, age, phoneNumber);
            } else {
                newMember = new JuniorMember(memberIdString, name, email, city, street, region, zipcode, type,
                        membershipStatus, activity.toActivityType(), paymentStatus, age, phoneNumber);
            }

            // Save the validated member using the MemberService
            memberService.registerMember(newMember);
            returnMember = newMember;

            // Reload members to immediately reflect the changes
            memberRepository.reloadMembers();

            System.out.println("Member registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return returnMember;
    }

    /**
     * Updates an existing member after validating the input.
     */
    public void updateMember(int memberId, String newName, String newEmail, int newAge, String newCity,
                             String newStreet, String newRegion, int newZipcode, String newMembershipType,
                             MembershipStatus newMembershipStatus, String newActivityType, PaymentStatus newPaymentStatus,
                             int newPhoneNumber) {
        try {
            // Validate updated member data
            Validator.validateMemberData(newName, newAge, newMembershipType, newEmail, newCity, newStreet, newRegion,
                    newZipcode, newPhoneNumber, newMembershipStatus, newActivityType, newPaymentStatus);

            // Find the existing member by ID
            Member memberToUpdate = memberRepository.findById(memberId);
            if (memberToUpdate == null) {
                System.out.println("Member not found with ID: " + memberId);
                return;
            }

            // Parse updated types
            MembershipType membershipType = MembershipType.fromString(newMembershipType);
            ActivityTypeData activity = ActivityTypeData.fromString(newActivityType);

            // Update member details
            memberToUpdate.setName(newName);
            memberToUpdate.setEmail(newEmail);
            memberToUpdate.setAge(newAge);
            memberToUpdate.setCity(newCity);
            memberToUpdate.setStreet(newStreet);
            memberToUpdate.setRegion(newRegion);
            memberToUpdate.setZipcode(newZipcode);
            memberToUpdate.setPhoneNumber(newPhoneNumber);
            memberToUpdate.setMembershipType(membershipType);
            memberToUpdate.setMembershipStatus(newMembershipStatus);
            memberToUpdate.setActivityType(activity.toActivityType());
            memberToUpdate.setPaymentStatus(newPaymentStatus);

            // Save the updated member using the MemberService
            memberService.updateMember(memberToUpdate);

            // Reload members to immediately reflect the changes
            memberRepository.reloadMembers();

            System.out.println("Member updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Deletes a member by ID.
     */
    public boolean deleteMember(int memberId) {
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            return false;
        }

        // Delete the member
        memberService.deleteMember(memberId);
        memberRepository.delete(member);

        // Reload members to immediately reflect the changes
        memberRepository.reloadMembers();

        System.out.println("Member deleted successfully.");
        return true;
    }

    /**
     * View all members stored in the repository.
     */
    public void viewAllMembers() {
        List<Member> allMembers = memberRepository.findAll();
        if (allMembers.isEmpty()) {
            System.out.println("\n--- NO REGISTERED MEMBERS YET ---");
        } else {
            System.out.println("\n--- All Registered Members ---");
            allMembers.forEach(member ->
                    System.out.println("ID: " + member.getMemberId() + ", Name: " + member.getName() +
                            ", Membership: " + member.getMembershipDescription() +
                            ", Status: " + member.getMembershipStatus() +
                            ", Activity: " + member.getActivityType() +
                            ", Payment: " + member.getPaymentStatus()));
        }
    }

    /**
     * Searches for members by ID, name, or phone number.
     */
    public List<Member> searchMembers(String query) {
        return memberService.searchMembers(query);
    }
}
