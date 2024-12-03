package swimclub;

import swimclub.controllers.MemberController;
import swimclub.controllers.PaymentController;
import swimclub.controllers.TeamController;
import swimclub.repositories.MemberRepository;
import swimclub.repositories.PaymentRepository;
import swimclub.services.MemberService;
import swimclub.services.PaymentService;
import swimclub.ui.UserInterface;
import swimclub.utilities.FileHandler;

public class Main {
    /**
     * The main method that initializes the application, loads data, and starts the user interface.
     * It also handles saving changes back to the files after the user interaction.
     *
     * @param args Command-line arguments (not used in this program).
     *             This parameter allows for passing arguments when running the program,
     *             but in this specific implementation, it is not used in the code.
     */
    public static void main(String[] args) {
        // File paths for member data, payment data, and reminder data
        String memberFilePath = "src/main/resources/members.dat";
        String paymentFilePath = "src/main/resources/payments.dat";
        String reminderFilePath = "src/main/resources/reminders.dat";
        String paymentRatesFilePath = "src/main/resources/paymentRates.dat";

        // Initialize the FileHandler for members, payments, and reminders
        FileHandler memberFileHandler = new FileHandler(memberFilePath, paymentFilePath, reminderFilePath, paymentRatesFilePath);

        // Initialize the repositories, passing the respective FileHandlers
        MemberRepository memberRepository = new MemberRepository(memberFileHandler);
        PaymentRepository paymentRepository = new PaymentRepository(reminderFilePath); // Pass the reminder file path

        // Load members and payments from the file
        memberRepository.reloadMembers(); // Load member data
        paymentRepository.loadPayments(paymentFilePath, memberRepository); // Load payment data

        // Initialize services for member and payment
        MemberService memberService = new MemberService(memberRepository);
        PaymentService paymentService = new PaymentService(paymentRepository, memberFileHandler);

        // Instantiate the controllers
        MemberController memberController = new MemberController(memberService, memberRepository);
        TeamController teamController = new TeamController();
        PaymentController paymentController = new PaymentController(
                paymentService,
                memberRepository,
                memberFileHandler,
                paymentFilePath,
                paymentRatesFilePath
        );

        // Instantiate the UserInterface, passing both controllers
        UserInterface userInterface = new UserInterface(memberController, paymentController);

        // Start the User Interface to handle interactions
        userInterface.start();


        // After user interaction, save any changes to file
        memberFileHandler.saveMembers(memberRepository.findAll());
        memberFileHandler.savePayments(paymentRepository.findAll(), paymentFilePath);
        // Reminders are saved directly by PaymentRepository via its methods, no need to use FileHandler here
    }
}
