package swimclub.services;

import org.junit.jupiter.api.*;
import swimclub.models.*;
import swimclub.repositories.*;
import swimclub.utilities.FileHandler;
import swimclub.utilities.Validator;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {
    private static final String TEST_MEMBER_FILE = "src/test/resources/testMembers.txt";
    private static final String TEST_PAYMENT_FILE = "src/test/resources/testPayments.txt";
    private static final String TEST_REMINDER_FILE = "src/test/resources/testReminders.txt";
    private static final String TEST_PAYMENTRATES_FILE = "src/main/resources/paymentRates.dat";
    private static final String TEST_TEAMS_FILE = "src/main/resources/teams.dat";
    private static final String TEST_TRAININGRESULTS_FILE = "src/main/resources/trainingResults.dat";
    private static final String TEST_COMPETITIONRESULTS_FILE = "src/main/resources/competitionResults.dat";

    private static final String TEST_STAFF_FILE = "src/main/resources/staff.dat";

    private FileHandler fileHandler;
    private MemberRepository memberRepository;
    private PaymentRepository paymentRepository;
    private PaymentService paymentService;

    @BeforeEach
    public void setUp() {
        createTestFile(TEST_MEMBER_FILE);
        createTestFile(TEST_PAYMENT_FILE);
        createTestFile(TEST_REMINDER_FILE);

        fileHandler = new FileHandler(
                TEST_MEMBER_FILE,
                TEST_PAYMENT_FILE,
                TEST_REMINDER_FILE,
                TEST_PAYMENTRATES_FILE,
                TEST_TEAMS_FILE,
                TEST_COMPETITIONRESULTS_FILE,
                TEST_STAFF_FILE,
                TEST_TRAININGRESULTS_FILE
        );
        memberRepository = new MemberRepository(fileHandler);
        paymentRepository = new PaymentRepository(TEST_REMINDER_FILE);
        paymentService = new PaymentService(paymentRepository, fileHandler);

        memberRepository.reloadMembers(); // Ensure the repository is in a known state
        paymentRepository.loadPayments(TEST_PAYMENT_FILE, memberRepository);
    }

    private void createTestFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs(); // Ensure parent directories exist
                file.createNewFile();
            } catch (IOException e) {
                fail("Failed to create test file: " + fileName);
            }
        }
    }

    @AfterEach
    void tearDown() {
        new File(TEST_MEMBER_FILE).delete();
        new File(TEST_PAYMENT_FILE).delete();
        new File(TEST_REMINDER_FILE).delete();
    }

    private Member createTestMember(String id, String name, ActivityType activityType, PaymentStatus paymentStatus) {
        return new SeniorMember(
                id, name, name.toLowerCase() + "@example.com", "City", "Street", "Region", 12345, new MembershipType(MembershipCategory.COMPETITIVE, MembershipLevel.SENIOR),
                MembershipStatus.ACTIVE, activityType, paymentStatus, 30, 12345678, "SD"
        );
    }

    @Test
    void testRegisterPayment() {
        // Arrange
        Member member = createTestMember("1", "Alice", ActivityType.BACKCRAWL, PaymentStatus.PENDING);
        memberRepository.save(member);
        double paymentAmount = 1600;

        // Act
        paymentService.registerPayment(member.getMemberId(), paymentAmount, memberRepository, fileHandler, TEST_PAYMENT_FILE);

        // Assert
        List<Payment> payments = paymentRepository.findPaymentsByMemberId(member.getMemberId());
        assertEquals(1, payments.size(), "One payment should be registered.");
        assertEquals(paymentAmount, payments.get(0).getAmountPerYear(), "Payment amount should match.");
    }

    @Test
    void testTotalPaymentsForMember() {
        // Arrange
        Member member = createTestMember("2", "Bob", ActivityType.CRAWL, PaymentStatus.PENDING);
        memberRepository.save(member);

        paymentService.registerPayment(member.getMemberId(), 1600, memberRepository, fileHandler, TEST_PAYMENT_FILE);
        paymentService.registerPayment(member.getMemberId(), 500, memberRepository, fileHandler, TEST_PAYMENT_FILE);

        // Act
        double totalPayments = paymentService.getTotalPaymentsForMember(member.getMemberId());

        // Assert
        assertEquals(2100, totalPayments, "Total payments should equal the sum of all payments made by the member.");
    }

    @Test
    void testInvalidPaymentThrowsException() {
        // Arrange
        double invalidAmount = -1000;

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> Validator.validatePayment(invalidAmount, PaymentStatus.PENDING),
                "Negative payment amount should throw an exception."
        );
        assertEquals("Invalid payment amount: Amount must be greater than 0.", exception.getMessage());
    }

    @Test
    void testSaveAndLoadPayments() {
        // Arrange
        Member member = createTestMember("3", "Charlie", ActivityType.BUTTERFLY, PaymentStatus.PENDING);
        memberRepository.save(member);

        paymentService.registerPayment(member.getMemberId(), 1600, memberRepository, fileHandler, TEST_PAYMENT_FILE);

        // Act
        List<Payment> loadedPayments = fileHandler.loadPayments(TEST_PAYMENT_FILE, memberRepository);

        // Assert
        assertEquals(1, loadedPayments.size(), "Payments should be loaded from the file.");
        assertEquals(1600, loadedPayments.get(0).getAmountPerYear(), "Loaded payment amount should match the saved amount.");
    }

    @Test
    void testPaymentSummary() {
        // Arrange
        Member member1 = createTestMember("4", "Daisy", ActivityType.BACKCRAWL, PaymentStatus.COMPLETE);
        Member member2 = createTestMember("5", "Evan", ActivityType.CRAWL, PaymentStatus.PENDING);
        memberRepository.save(member1);
        memberRepository.save(member2);

        // Act
        String summary = paymentService.getPaymentSummary(memberRepository.findAll());

        // Assert
        assertTrue(summary.contains("Total Members Paid: 1"), "Summary should indicate 1 member has paid.");
        assertTrue(summary.contains("Total Members Pending: 1"), "Summary should indicate 1 member is pending.");
    }
}
