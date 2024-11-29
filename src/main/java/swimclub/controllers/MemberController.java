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
    public Member registerMember(String name, String email, String city, String street, String region, String zipcodeStr,
                                 String membershipType, MembershipStatus membershipStatus, String activityType,
                                 PaymentStatus paymentStatus, String ageStr, String phoneNumberStr) {
        Member returnMember = null;
        try {
            // Validate member data
            Validator.validateMemberData(name, Integer.parseInt(ageStr), membershipType, email, city, street, region, Integer.parseInt(zipcodeStr), Integer.parseInt(phoneNumberStr),
                    membershipStatus, activityType, paymentStatus);

            // Parse the membershipType into a MembershipType object
            MembershipType type = MembershipType.fromString(membershipType);

            // Parse the activityType into an ActivityTypeData object
            ActivityTypeData activity = ActivityTypeData.fromString(activityType);

            // Parse the age safely
            int age = parseAge(ageStr);

            // Parse the age safely
            int phoneNumber = parsePhoneNumber(phoneNumberStr);

            // Parse the zipcode safely
            int zipcode = parseZipCode(zipcodeStr);

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
    public void updateMember(int memberId, String newName, String newEmail, String newAgeStr, String newCity,
                             String newStreet, String newRegion, String newZipcodeStr, String newMembershipType,
                             MembershipStatus newMembershipStatus, String newActivityType, PaymentStatus newPaymentStatus,
                             String newPhoneNumberStr) {
        try {
            // Validate updated member data
            Validator.validateMemberData(newName, Integer.parseInt(newAgeStr), newMembershipType, newEmail, newCity, newStreet, newRegion,
                    Integer.parseInt(newZipcodeStr), Integer.parseInt(newPhoneNumberStr), newMembershipStatus, newActivityType, newPaymentStatus);

            // Find the existing member by ID
            Member memberToUpdate = memberRepository.findById(memberId);
            if (memberToUpdate == null) {
                System.out.println("Member not found with ID: " + memberId);
                return;
            }
            // Parse the age safely
            int newAge = parseAge(newAgeStr);

            // Parse the phonenumber safely
            int newPhoneNumber = parsePhoneNumber(newPhoneNumberStr);

            // Parse the zipcode safely
            int newZipcode = parseZipCode(newZipcodeStr);

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

    public int parseAge(String ageStr) {
        try {
            // Parse the age safely, throw exception if not valid
            int age = Integer.parseInt(ageStr);
            if (age <= 0) {
                throw new IllegalArgumentException("Age must be a positive number.");
            }
            return age;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid age input. Please enter a valid number.");
        }
    }
    public int parsePhoneNumber(String phonenumberStr){
        try {
            // Parse the phonenumber safely,  throw exeption if not valid
            return Integer.parseInt(phonenumberStr);
            } catch (NumberFormatException e){
            throw new IllegalArgumentException("Invalid phonenumber input. Please enter a valid phone number (8 digits):");
        }
    }
    public int parseZipCode(String zipCodeStr){
        try{
            // Parse the phonenumber safely, trhwo exception if not valid
            return Integer.parseInt(zipCodeStr);
        } catch (Exception e){
            throw new IllegalArgumentException("Invalid zipcode input. Please enter a valid zipcode number (4 digits):");
        }
    }
}
