package swimclub.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swimclub.models.*;
import swimclub.repositories.MemberRepository;
import swimclub.services.MemberService;
import swimclub.utilities.FileHandler;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemberControllerTest {

    private MemberService memberService;
    private MemberRepository memberRepository;
    private MemberController controller;
    private FileHandler fileHandler;

    @BeforeEach
    void setUp() {
        // Sørg for at initialisere FileHandler med korrekte argumenter (fiktive stier for test)
        String memberFilePath = "test_members.dat";
        String paymentFilePath = "test_payments.dat";
        String reminderFilePath = "test_reminders.dat";
        fileHandler = new FileHandler(memberFilePath, paymentFilePath, reminderFilePath);

        // Initialiser MemberRepository med den korrekte FileHandler
        memberRepository = new MemberRepository(fileHandler);

        // Initialiser MemberService og MemberController med de nødvendige afhængigheder
        memberService = new MemberService(memberRepository);
        controller = new MemberController(memberService, memberRepository);
    }

    @Test
    void registerMember_ShouldCreateSeniorMember_WhenAgeIsAbove18() {
        // Arrange
        String name = "Carsten";
        String email = "CarstenWork@email.dk";
        String city = "Herning";
        String street = "HB Hansensvej 2";
        String region = "MidtJylland";
        int zipcode = 7400;
        String membershipType = "Senior competitive";
        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;
        String activityType = "Crawl";
        PaymentStatus paymentStatus = PaymentStatus.COMPLETE;
        String ageStr = "23";
        int phoneNumber = 60126754;

        // Act
        Member registeredMember = controller.registerMember(name, email, city, street, region, zipcode,
                membershipType, membershipStatus, activityType, paymentStatus, ageStr, phoneNumber);

        // Assert
        assertNotNull(registeredMember); // Ensure a member is returned
        assertEquals("Carsten", registeredMember.getName()); // Ensure the name is correctly set
        assertTrue(registeredMember instanceof SeniorMember); // Ensure it's a SeniorMember based on the age
    }
    @Test
    void registerMember_ShouldCreateJuniorMember_WhenAgeIsUnder18() {
        // Arrange
        String name = "Carsten";
        String email = "CarstenWork@email.dk";
        String city = "Herning";
        String street = "HB Hansensvej 2";
        String region = "MidtJylland";
        int zipcode = 7400;
        String membershipType = "Senior competitive";
        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;
        String activityType = "Crawl";
        PaymentStatus paymentStatus = PaymentStatus.COMPLETE;
        String ageStr = "17";
        int phoneNumber = 60126754;

        // Act
        Member registeredMember = controller.registerMember(name, email, city, street, region, zipcode,
                membershipType, membershipStatus, activityType, paymentStatus, ageStr, phoneNumber);

        // Assert
        assertNotNull(registeredMember); // Ensure a member is returned
        assertEquals("Carsten", registeredMember.getName()); // Ensure the name is correctly set
        assertTrue(registeredMember instanceof JuniorMember); // Ensure it's a SeniorMember based on the age
    }

    @Test
    void updateMember() {
        // Arrange - Prepare the data for the initial member "Carsten"
        String name = "Carsten";
        String email = "CarstenWork@email.dk";
        String city = "Herning";
        String street = "HB Hansensvej 2";
        String region = "MidtJylland";
        int zipcode = 7400;
        String membershipType = "Senior competitive";
        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;
        String activityType = "Crawl";
        PaymentStatus paymentStatus = PaymentStatus.COMPLETE;
        String ageStr = "23";
        int phoneNumber = 60126754;

        // Act - Register the member using the controller
        Member registeredMember = controller.registerMember(name, email, city, street, region, zipcode,
                membershipType, membershipStatus, activityType, paymentStatus, ageStr, phoneNumber);

        // Store the member's ID for later use in the update
        int memberId = registeredMember.getMemberId();

        // Updated Member - Prepare the new values for the member "Kasper"
        String newName = "Kasper";
        String newEmail = "KasperWork@email.dk";
        String newCity = "Aarhus";
        String newStreet = "Aarhus vej 2";
        String newRegion = "Midtjylland";
        int newZipcode = 8800;
        String newMembershipType = "Senior exercise";  // New membership type
        MembershipStatus newMembershipStatus = MembershipStatus.ACTIVE;
        String newActivityType = "Crawl";
        PaymentStatus newPaymentStatus = PaymentStatus.COMPLETE;
        String newAgeStr = "45";  // New age
        int newPhoneNumber = 60336754;

        // Act - Update the member with the new information
        controller.updateMember(memberId, newName, newEmail,
                newAgeStr, newCity, newStreet, newRegion, newZipcode,
                newMembershipType, newMembershipStatus, newActivityType, newPaymentStatus, newPhoneNumber);

        // Fetch the updated member from the repository
        Member updatedMember = memberRepository.findById(memberId);

        // Assert - Verify that the updates were applied correctly
        assertNotNull(updatedMember);  // Ensure that the updated member exists
        assertEquals("Kasper", updatedMember.getName());  // Check that the name was updated
        assertEquals("KasperWork@email.dk", updatedMember.getEmail());  // Check that the email was updated
        assertEquals("Aarhus", updatedMember.getCity());  // Check that the city was updated
        assertEquals("Aarhus vej 2", updatedMember.getStreet());  // Check that the street was updated
        assertEquals("Midtjylland", updatedMember.getRegion());  // Check that the region was updated
        assertEquals(45, updatedMember.getAge());  // Check that the age was updated
        assertEquals(60336754, updatedMember.getPhoneNumber());  // Check that the phone number was updated
        assertEquals(MembershipStatus.ACTIVE, updatedMember.getMembershipStatus());  // Check that the membership status is still ACTIVE
    }

    @Test
    void deleteMember() {
        //Arrange - member to be deleted
        String name = "Carsten";
        String email = "CarstenWork@email.dk";
        String city = "Herning";
        String street = "HB Hansensvej 2";
        String region = "MidtJylland";
        int zipcode = 7400;
        String membershipType = "Senior competitive";
        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;
        String activityType = "Crawl";
        PaymentStatus paymentStatus = PaymentStatus.COMPLETE;
        String ageStr = "23";
        int phoneNumber = 60126754;

        //Act - Deleting the member
        Member registeredMember = controller.registerMember(name, email, city,
                street, region, zipcode, membershipType, membershipStatus, activityType,
                paymentStatus, ageStr, phoneNumber);

        controller.deleteMember(registeredMember.getMemberId());

        //Assert
        Member deletedMember = memberRepository.findById(registeredMember.getMemberId());
        assertNull(deletedMember);
    }
    @Test
    void viewAllMembers() {
        // Arrange - Create two members: Carsten and Kasper
        String carstenName = "Carsten";
        String carstenEmail = "CarstenWork@email.dk";
        String carstenCity = "Herning";
        String carstenStreet = "HB Hansensvej 2";
        String carstenRegion = "MidtJylland";
        int carstenZipcode = 7400;
        String carstenMembershipType = "Senior competitive";
        MembershipStatus carstenMembershipStatus = MembershipStatus.ACTIVE;
        String carstenActivityType = "Crawl";
        PaymentStatus carstenPaymentStatus = PaymentStatus.COMPLETE;
        String carstenAgeStr = "23";
        int carstenPhoneNumber = 60126754;

        String kasperName = "Kasper";
        String kasperEmail = "KasperWork@email.dk";
        String kasperCity = "Aarhus";
        String kasperStreet = "Aarhus vej 2";
        String kasperRegion = "MidtJylland";
        int kasperZipcode = 8800;
        String kasperMembershipType = "Senior exercise";
        MembershipStatus kasperMembershipStatus = MembershipStatus.ACTIVE;
        String kasperActivityType = "Butterfly";
        PaymentStatus kasperPaymentStatus = PaymentStatus.COMPLETE;
        String kasperAgeStr = "30";
        int kasperPhoneNumber = 60336754;

        // Act - Register Carsten and Kasper
        Member carsten = controller.registerMember(carstenName, carstenEmail, carstenCity, carstenStreet, carstenRegion,
                carstenZipcode, carstenMembershipType, carstenMembershipStatus, carstenActivityType, carstenPaymentStatus,
                carstenAgeStr, carstenPhoneNumber);

        Member kasper = controller.registerMember(kasperName, kasperEmail, kasperCity, kasperStreet, kasperRegion,
                kasperZipcode, kasperMembershipType, kasperMembershipStatus, kasperActivityType, kasperPaymentStatus,
                kasperAgeStr, kasperPhoneNumber);

        controller.viewAllMembers();

        //Assert
        assertNotNull(memberRepository.findById(carsten.getMemberId()));
        assertNotNull(memberRepository.findById(kasper.getMemberId()));
    }

    @Test
    void searchMembers() {
        // Arrange - Member to search for
        String name = "Carsten";
        String email = "CarstenWork@email.dk";
        String city = "Herning";
        String street = "HB Hansensvej 2";
        String region = "MidtJylland";
        int zipcode = 7400;
        String membershipType = "Senior competitive";
        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;
        String activityType = "Crawl";
        PaymentStatus paymentStatus = PaymentStatus.COMPLETE;
        String ageStr = "23";
        int phoneNumber = 60126754;

        // Act - Register the new member
        Member registeredMember = controller.registerMember(name, email, city, street, region, zipcode,
                membershipType, membershipStatus, activityType, paymentStatus, ageStr, phoneNumber);

        // Act - Search for members by name
        List<Member> memberList = controller.searchMembers("Carsten");

        // Assert - Ensure the list is not empty
        assertNotNull(memberList, "The member list should not be null.");
        assertFalse(memberList.isEmpty(), "No members found.");

        // Assert - Ensure the registered member is in the list
        boolean memberFound = false;  // Initialize memberFound to false
        for (Member member : memberList) {
            if (member.getMemberId() == registeredMember.getMemberId()) {
                memberFound = true;  // Set to true when the member is found
                assertEquals("Carsten", member.getName());  // Optionally, check the name
                break;
            }
        }

        // Assert that the registered member was found
        assertTrue(memberFound, "Registered member 'Carsten' was not found in the search results.");
    }
}