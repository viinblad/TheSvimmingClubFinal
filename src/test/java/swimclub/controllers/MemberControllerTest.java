package swimclub.controllers;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import swimclub.models.*;
import swimclub.repositories.MemberRepository;
import swimclub.services.MemberService;
import swimclub.utilities.FileHandler;


import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemberControllerTest {
    private static final String TEST_MEMBER_FILE = "src/test/java/testResources/testMembers.dat";
    private static final String TEST_PAYMENT_FILE = "src/test/java/testResources/testPayments.dat";
    private static final String TEST_REMINDER_FILE = "src/test/java/testResources/testReminders.dat";
    private static final String TEST_PAYMENTRATES_FILE = "src/main/ressources/paymentRates.dat";
    private static final String TEST_TEAM_FILE = "src/main/ressources/teams.dat";

    private MemberService memberService;
    private MemberRepository memberRepository;
    private MemberController controller;
    private FileHandler fileHandler;

    @BeforeEach
    void setUp() {
        // Ensure that FileHandler is initialized with the correct arguments (mock paths for the test)
        createTestFile(TEST_MEMBER_FILE);
        createTestFile(TEST_PAYMENT_FILE);
        createTestFile(TEST_REMINDER_FILE);
        createTestFile(TEST_PAYMENTRATES_FILE);

        // Initialize the FileHandler with the file paths
        fileHandler = new FileHandler(TEST_MEMBER_FILE, TEST_PAYMENT_FILE, TEST_REMINDER_FILE, TEST_PAYMENTRATES_FILE,TEST_TEAM_FILE);

        // Initialize MemberRepository with the FileHandler instance
        memberRepository = new MemberRepository(fileHandler);

        // Initialize MemberService and MemberController with the necessary dependencies
        memberService = new MemberService(memberRepository);
        controller = new MemberController(memberService, memberRepository);
    }
    private void createTestFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @AfterEach
    void tearDown(){
        new File(TEST_MEMBER_FILE).delete();
        new File(TEST_PAYMENT_FILE).delete();
        new File(TEST_REMINDER_FILE).delete();

    }

    @Test
    void registerMember_ShouldCreateSeniorMember_WhenAgeIsAbove18() {
        // Arrange
        String name = "Carsten";
        String email = "CarstenWork@email.dk";
        String city = "Herning";
        String street = "HB Hansensvej 2";
        String region = "MidtJylland";
        String zipcode = "7400";
        String membershipType = "Senior competitive";
        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;
        String activityType = "Crawl";
        PaymentStatus paymentStatus = PaymentStatus.COMPLETE;
        String ageStr = "23";
        String phoneNumber = "60126754";

        // Act
        Member registeredMember = controller.registerMember(name, email, city, street, region, Integer.parseInt(zipcode),
                membershipType, membershipStatus, activityType, paymentStatus, ageStr, Integer.parseInt(phoneNumber));

        // Assert
        assertNotNull(registeredMember); // Ensure a member is returned
        assertEquals("Carsten", registeredMember.getName()); // Ensure the name is correctly set
        assertInstanceOf(SeniorMember.class, registeredMember); // Ensure it's a SeniorMember based on the age
    }
    @Test
    void registerMember_ShouldCreateJuniorMember_WhenAgeIsUnder18() {
        // Arrange
        String name = "Carsten";
        String email = "CarstenWork@email.dk";
        String city = "Herning";
        String street = "HB Hansensvej 2";
        String region = "MidtJylland";
        String zipcode = "7400";
        String membershipType = "Senior competitive";
        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;
        String activityType = "Crawl";
        PaymentStatus paymentStatus = PaymentStatus.COMPLETE;
        String ageStr = "17";
        String phoneNumber = "60126754";

        // Act
        Member registeredMember = controller.registerMember(name, email, city, street, region, Integer.parseInt(zipcode),
                membershipType, membershipStatus, activityType, paymentStatus, ageStr, Integer.parseInt(phoneNumber));

        // Assert
        assertNotNull(registeredMember); // Ensure a member is returned
        assertEquals("Carsten", registeredMember.getName()); // Ensure the name is correctly set
        assertInstanceOf(JuniorMember.class, registeredMember); // Ensure it's a SeniorMember based on the age
    }

    @Test
    void updateMember() {
        // Arrange - Prepare the data for the initial member "Carsten"
        String name = "Carsten";
        String email = "CarstenWork@email.dk";
        String city = "Herning";
        String street = "HB Hansensvej 2";
        String region = "MidtJylland";
        String zipcode = "7400";
        String membershipType = "Senior competitive";
        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;
        String activityType = "Crawl";
        PaymentStatus paymentStatus = PaymentStatus.COMPLETE;
        String ageStr = "23";
        String phoneNumber = "60126754";

        // Act - Register the member using the controller
        Member registeredMember = controller.registerMember(name, email, city, street, region, Integer.parseInt(zipcode),
                membershipType, membershipStatus, activityType, paymentStatus, ageStr, Integer.parseInt(phoneNumber));

        // Store the member's ID for later use in the update
        int memberId = registeredMember.getMemberId();

        // Updated Member - Prepare the new values for the member "Kasper"
        String newName = "Kasper";
        String newEmail = "KasperWork@email.dk";
        String newCity = "Aarhus";
        String newStreet = "Aarhus vej 2";
        String newRegion = "Midtjylland";
        String newZipcode = "8800";
        String newMembershipType = "Senior exercise";  // New membership type
        MembershipStatus newMembershipStatus = MembershipStatus.ACTIVE;
        String newActivityType = "Crawl";
        PaymentStatus newPaymentStatus = PaymentStatus.COMPLETE;
        String newAgeStr = "45";  // New age
        String newPhoneNumber = "60336754";

        // Act - Update the member with the new information
        controller.updateMember(memberId, newName, newEmail,
                newAgeStr, newCity, newStreet, newRegion, Integer.parseInt(newZipcode),
                newMembershipType, newMembershipStatus, newActivityType, newPaymentStatus, Integer.parseInt(newPhoneNumber));

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
        String zipcode = "7400";
        String membershipType = "Senior competitive";
        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;
        String activityType = "Crawl";
        PaymentStatus paymentStatus = PaymentStatus.COMPLETE;
        String ageStr = "23";
        String phoneNumber = "60126754";

        //Act - Deleting the member
        Member registeredMember = controller.registerMember(name, email, city,
                street, region, Integer.parseInt(zipcode), membershipType, membershipStatus, activityType,
                paymentStatus, ageStr, Integer.parseInt(phoneNumber));

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
        String carstenZipcode = "7400";
        String carstenMembershipType = "Senior competitive";
        MembershipStatus carstenMembershipStatus = MembershipStatus.ACTIVE;
        String carstenActivityType = "Crawl";
        PaymentStatus carstenPaymentStatus = PaymentStatus.COMPLETE;
        String carstenAgeStr = "23";
        String carstenPhoneNumber = "60126754";

        String kasperName = "Kasper";
        String kasperEmail = "KasperWork@email.dk";
        String kasperCity = "Aarhus";
        String kasperStreet = "Aarhus vej 2";
        String kasperRegion = "MidtJylland";
        String kasperZipcode = "8800";
        String kasperMembershipType = "Senior exercise";
        MembershipStatus kasperMembershipStatus = MembershipStatus.ACTIVE;
        String kasperActivityType = "Butterfly";
        PaymentStatus kasperPaymentStatus = PaymentStatus.COMPLETE;
        String kasperAgeStr = "30";
        String kasperPhoneNumber = "60336754";

        // Act - Register Carsten and Kasper
        Member carsten = controller.registerMember(carstenName, carstenEmail, carstenCity, carstenStreet, carstenRegion,
                Integer.parseInt(carstenZipcode), carstenMembershipType, carstenMembershipStatus, carstenActivityType, carstenPaymentStatus,
                carstenAgeStr, Integer.parseInt(carstenPhoneNumber));

        Member kasper = controller.registerMember(kasperName, kasperEmail, kasperCity, kasperStreet, kasperRegion,
                Integer.parseInt(kasperZipcode), kasperMembershipType, kasperMembershipStatus, kasperActivityType, kasperPaymentStatus,
                kasperAgeStr, Integer.parseInt(kasperPhoneNumber));

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
        String zipcode = "7400";
        String membershipType = "Senior competitive";
        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;
        String activityType = "Crawl";
        PaymentStatus paymentStatus = PaymentStatus.COMPLETE;
        String ageStr = "23";
        String phoneNumber = "60126754";

        // Act - Register the new member
        Member registeredMember = controller.registerMember(name, email, city, street, region, Integer.parseInt(zipcode),
                membershipType, membershipStatus, activityType, paymentStatus, ageStr, Integer.parseInt(phoneNumber));

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