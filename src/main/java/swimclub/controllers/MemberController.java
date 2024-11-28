package swimclub.controllers;

import swimclub.models.*;
import swimclub.repositories.MemberRepository;
import swimclub.services.MemberService;
import swimclub.utilities.Validator;
import swimclub.controllers.PaymentController;

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
     *
     * @param name            Full name of the member.
     * @param email           Email address of the member.
     * @param city            Living city of the member.
     * @param street          Living street of the member.
     * @param region          Living region of the member.
     * @param zipcode         Living zip code.
     * @param membershipType  Membership type (e.g., Junior Competitive).
     * @param membershipStatus Membership status (Active/Passive).
     * @param paymentStatus   Payment status (Pending/Complete/Failed).
     * @param age             Age of the member.
     * @param phoneNumber     Phone number of the member.
     */
    public Member registerMember(String name, String email, String city, String street, String region, int zipcode,
                               String membershipType, MembershipStatus membershipStatus,String activityType, PaymentStatus paymentStatus,
                               int age, int phoneNumber) {
        Member returnMember = null;
        try {
            // Validate member data
            Validator.validateMemberData(name, age, membershipType, email, city, street, region, zipcode, phoneNumber,
                    membershipStatus,activityType, paymentStatus);

            // Parse the membershipType into a MembershipType object
            MembershipType type = MembershipType.fromString(membershipType.toUpperCase());

            // Parse the activityType into an ActivityType object
            ActivityTypeData activity = ActivityTypeData.fromString(activityType);

            // Generate the next available member ID
            int memberId = memberRepository.getNextMemberId();
            String memberIdString = String.valueOf(memberId); // Convert memberId to String

            // Dynamically create a JuniorMember or SeniorMember based on age
            Member newMember;
            if (age > 18) {
                newMember = new SeniorMember(memberIdString, name, email, city, street, region, zipcode, type,
                        membershipStatus,activity.toActivityType(), paymentStatus, age, phoneNumber);
            } else {
                newMember = new JuniorMember(memberIdString, name, email, city, street, region, zipcode, type,
                        membershipStatus,activity.toActivityType(), paymentStatus, age, phoneNumber);
            }

            // Save the validated member using the MemberService
            memberService.registerMember(newMember);

            returnMember = newMember;
            //

            // Reload members after registration to immediately reflect the changes
            memberRepository.reloadMembers();

            System.out.println("Member registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return returnMember;
    }

    /**
     * Update an existing member after validating the input.
     *
     * @param memberId        The ID of the member to update.
     * @param newName         The new name of the member.
     * @param newEmail        The new email of the member.
     * @param newAge          The new age of the member.
     * @param newCity         Living city of the member.
     * @param newStreet       Living street of the member.
     * @param newRegion       Living region of the member.
     * @param newZipcode      Living zip code.
     * @param newMembershipType Membership type (e.g., Junior Competitive).
     * @param newMembershipStatus Membership status (Active/Passive).
     * @param newPaymentStatus Payment status (Pending/Complete/Failed).
     * @param newPhoneNumber  The new phone number of the member.
     */
    public void updateMember(int memberId, String newName, String newEmail, int newAge, String newCity,
                             String newStreet, String newRegion, int newZipcode, String newMembershipType,
                             MembershipStatus newMembershipStatus,String newActivityType, PaymentStatus newPaymentStatus,
                             int newPhoneNumber) {
        try {
            // Validate updated member data
            Validator.validateMemberData(newName, newAge, newMembershipType, newEmail, newCity, newStreet, newRegion,
                    newZipcode, newPhoneNumber, newMembershipStatus,newActivityType, newPaymentStatus);

            // Find the existing member by ID
            Member memberToUpdate = memberRepository.findById(memberId);

            if (memberToUpdate != null) {
                // Update member details
                memberToUpdate.setName(newName);
                memberToUpdate.setEmail(newEmail);
                memberToUpdate.setAge(newAge);
                memberToUpdate.setCity(newCity);
                memberToUpdate.setStreet(newStreet);
                memberToUpdate.setRegion(newRegion);
                memberToUpdate.setZipcode(newZipcode);
                memberToUpdate.setPhoneNumber(newPhoneNumber);

                // Update membership and payment details
                MembershipType membershipType = MembershipType.fromString(newMembershipType);
                memberToUpdate.setMembershipType(membershipType);
                memberToUpdate.setMembershipStatus(newMembershipStatus);
                memberToUpdate.setPaymentStatus(newPaymentStatus);

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
     * Deletes a member by ID.
     *
     * @param memberId The ID of the member to delete.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteMember(int memberId) {
        memberService.deleteMember(memberId);
        Member member = memberRepository.findById(memberId);

        if (member == null) { // If member does not exist, return false
            return false;
        }
        memberRepository.delete(member); // If member exists, delete and return true

        // Reload members after update to immediately reflect the changes
        memberRepository.reloadMembers();
        return true;

    }

    /**
     * View all members stored in the repository.
     */
    public void viewAllMembers() {
        if (memberRepository.findAll().isEmpty()) {
            System.out.println("\n---NO REGISTERED MEMBERS YET---");
        } else {
            System.out.println("\n--- All Registered Members ---");
            memberRepository.findAll().forEach(member ->
                    System.out.println("ID: " + member.getMemberId() + ", Name: " + member.getName() +
                            ", Membership: " + member.getMembershipDescription() +
                            ", Status: " + member.getMembershipStatus() +
                            ", Activity: " + member.getActivityType() +
                            ", Payment: " + member.getPaymentStatus()));
        }
    }

    /**
     * Searches for members by ID, name, or phone number.
     *
     * @param query The search query provided by the user.
     * @return A list of members matching the query.
     */
    public List<Member> searchMembers(String query) {
        return memberService.searchMembers(query);
    }
}
