package swimclub.controllers;

import swimclub.models.*;
import swimclub.repositories.MemberRepository;
import swimclub.services.MemberService;
import swimclub.utilities.Validator;

import java.util.List;

public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;

    // === CONSTRUCTOR ===
    /**
     * Initializes the MemberController with the provided service and repository.
     *
     * @param memberService The service to handle member logic.
     * @param memberRepository The repository to interact with member data.
     */
    public MemberController(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    // === MEMBER REGISTRATION ===
    /**
     * Registers a new member after validating the input.
     * This method also assigns the next available member ID, parses the membership type and activity type,
     * and determines if the member is a Junior or Senior based on age.
     *
     * @param name             The name of the member.
     * @param email            The email address of the member.
     * @param city             The city where the member lives.
     * @param street           The street address of the member.
     * @param region           The region where the member lives.
     * @param zipcode          The zip code of the member's address.
     * @param membershipType   The type of membership (e.g., Junior or Senior, Competitive or Exercise).
     * @param membershipStatus The current membership status (e.g., ACTIVE).
     * @param activityType     The type of activity the member participates in (e.g., Breaststroke, Crawl).
     * @param paymentStatus    The current payment status (e.g., PENDING, COMPLETE).
     * @param ageStr           The age of the member as a string.
     * @param phoneNumber      The phone number of the member.
     * @return The newly registered member, or null if there was an error during registration.
     */
    public Member registerMember(String name, String email, String city, String street, String region, int zipcode,
                                 String membershipType, MembershipStatus membershipStatus, String activityType,
                                 PaymentStatus paymentStatus, String ageStr, int phoneNumber) {
        Member returnMember = null;
        try {
            // Validate member data
            Validator.validateMemberData(name, Integer.parseInt(ageStr), membershipType, email, city, street, region, zipcode, phoneNumber,
                    membershipStatus, activityType, paymentStatus);

            // Parse the membershipType into a MembershipType object
            MembershipType type = MembershipType.fromString(membershipType);

            // Parse the activityType into an ActivityTypeData object
            ActivityTypeData activity = ActivityTypeData.fromString(activityType);

            // Parse the age safely
            int age = parseAge(ageStr);

            // Generate the next available member ID
            int memberId = memberRepository.getNextMemberId();
            String memberIdString = String.valueOf(memberId); // Convert memberId to String

            // Dynamically create a JuniorMember or SeniorMember based on age
            Member newMember;
            if (age > 18) {
                newMember = new SeniorMember(memberIdString, name, email, city, street, region, zipcode, type,
                        membershipStatus, activity.toActivityType(), paymentStatus, age, phoneNumber, null);
            } else {
                newMember = new JuniorMember(memberIdString, name, email, city, street, region, zipcode, type,
                        membershipStatus, activity.toActivityType(), paymentStatus, age, phoneNumber, null);
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

    // === MEMBER RETRIEVAL ===
    /**
     * Finds a member by ID.
     *
     * @param memberId The ID of the member to find.
     * @return The member if found, or null if not found.
     */
    public Member findMemberById(int memberId) {
        return memberRepository.findById(memberId); // Use repository to find the member by ID
    }

    // === MEMBER UPDATE ===
    /**
     * Updates an existing member after validating the input.
     * This method also parses and validates the updated member's details.
     *
     * @param memberId         The ID of the member to update.
     * @param newName          The updated name of the member.
     * @param newEmail         The updated email address of the member.
     * @param newAgeStr        The updated age of the member as a string.
     * @param newCity          The updated city of the member.
     * @param newStreet        The updated street address of the member.
     * @param newRegion        The updated region where the member lives.
     * @param newZipcode       The updated zip code of the member's address.
     * @param newMembershipType The updated type of membership (e.g., Junior or Senior).
     * @param newMembershipStatus The updated membership status (e.g., ACTIVE).
     * @param newActivityType  The updated activity type the member participates in.
     * @param newPaymentStatus The updated payment status (e.g., PENDING, COMPLETE).
     * @param newPhoneNumber   The updated phone number of the member.
     */
    public void updateMember(int memberId, String newName, String newEmail, String newAgeStr, String newCity,
                             String newStreet, String newRegion, int newZipcode, String newMembershipType,
                             MembershipStatus newMembershipStatus, String newActivityType, PaymentStatus newPaymentStatus,
                             int newPhoneNumber) {
        try {
            // Validate updated member data
            Validator.validateMemberData(newName, Integer.parseInt(newAgeStr), newMembershipType, newEmail, newCity, newStreet, newRegion,
                    newZipcode, newPhoneNumber, newMembershipStatus, newActivityType, newPaymentStatus);

            // Find the existing member by ID
            Member memberToUpdate = memberRepository.findById(memberId);
            if (memberToUpdate == null) {
                System.out.println("Member not found with ID: " + memberId);
                return;
            }
            // Parse the age safely
            int newAge = parseAge(newAgeStr);

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

    // === MEMBER DELETION ===
    /**
     * Deletes a member by ID.
     * This method removes the member from both the repository and the service.
     *
     * @param memberId The ID of the member to delete.
     * @return true if the member was deleted successfully, false if the member was not found.
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

    // === VIEW MEMBERS ===
    /**
     * View all members stored in the repository.
     * This method prints the details of all registered members.
     */
    public void viewAllMembers() {
        // Retrieve all members from the memberRepository
        List<Member> allMembers = memberRepository.findAll();

        // Check if the list of members is empty
        if (allMembers.isEmpty()) {
            // Print a message if no members are registered
            System.out.println("\n--- NO REGISTERED MEMBERS YET ---");
        } else {
            // Print a header for the list of all members
            System.out.println("\n--- All Registered Members ---");
            System.out.println("---------------------------------------------------------------");

            // Iterate over all members and print their details
            for (Member member : allMembers) {
                // Retrieve team name
                String teamName = member.getTeamName();

                // If teamName is null or empty string, set it to "No team"
                if (teamName == null || teamName.trim().isEmpty()) {
                    teamName = "No team";  // If null or empty, set to "No team"
                }

                // Print the member's details
                System.out.println("ID: " + member.getMemberId());
                System.out.println("Name: " + member.getName());
                System.out.println("Membership: " + member.getMembershipDescription());
                System.out.println("Status: " + member.getMembershipStatus());
                System.out.println("Activity: " + member.getActivityType());
                System.out.println("Payment: " + member.getPaymentStatus());
                System.out.println("Team: " + teamName);  // This will print "No team" if there's no team
                System.out.println("---------------------------------------------------------------");
            }
        }
    }

    // === MEMBER SEARCH ===
    /**
     * Searches for members by ID, name, or phone number.
     * This method delegates the search functionality to the service layer.
     *
     * @param query The search query to match.
     * @return A list of members matching the query.
     */
    public List<Member> searchMembers(String query) {
        return memberService.searchMembers(query);
    }

    // === AGE PARSING ===
    /**
     * Safely parses the age from a string.
     *
     * @param ageStr The age as a string to be parsed.
     * @return The parsed age as an integer.
     * @throws IllegalArgumentException If the age is not a valid positive number.
     */
    private int parseAge(String ageStr) {
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

    /**
     * Associates a team with a member by setting the team name.
     *
     * @param member   The member to associate with a team.
     * @param teamName The name of the team to assign to the member.
     */
    public void addTeamToMember(Member member, String teamName) {

        // Set the team name for the given member
        member.setTeamName(teamName);

        // Save the updated member data to the repository
        memberRepository.saveMembers();
    }

    /**
     * Removes the association of a team from a member by setting the team name to null.
     *
     * @param member The member whose team association is to be removed.
     */
    public void removeTeamFromMember(Member member) {
        // Remove the team association by setting the team name to null
        member.setTeamName(null);

        // Save the updated member data to the repository
        memberRepository.saveMembers();
    }
 }