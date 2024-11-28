package swimclub;

import swimclub.controllers.MemberController;
import swimclub.controllers.PaymentController;
import swimclub.repositories.MemberRepository;
import swimclub.repositories.PaymentRepository;
import swimclub.services.MemberService;
import swimclub.services.PaymentService;
import swimclub.ui.UserInterface;
import swimclub.utilities.FileHandler;

public class Main {
    public static void main(String[] args) {
        // File paths for member data and payment data
        String memberFilePath = "src/main/resources/members.txt";
        String paymentFilePath = "src/main/resources/payments.txt";

        // Initialize the FileHandler for members and payments
        FileHandler memberFileHandler = new FileHandler(memberFilePath);
        FileHandler paymentFileHandler = new FileHandler(paymentFilePath);

        // Initialize the repositories, passing the respective FileHandlers
        MemberRepository memberRepository = new MemberRepository(memberFileHandler);
        PaymentRepository paymentRepository = new PaymentRepository();

        // Load payments from the file and associate them with members
        paymentRepository.loadPayments(paymentFilePath, memberRepository);

        // Initialize services for member and payment
        MemberService memberService = new MemberService(memberRepository);
        PaymentService paymentService = new PaymentService(paymentRepository);

        // Instantiate the controllers
        MemberController memberController = new MemberController(memberService, memberRepository);
        PaymentController paymentController = new PaymentController(
                paymentService,
                memberRepository,
                paymentFileHandler,
                paymentFilePath
        );

        // Instantiate the UserInterface, passing both controllers
        UserInterface userInterface = new UserInterface(memberController, paymentController);

        // Start the User Interface to handle interactions
        userInterface.start();

        // After user interaction, save any changes to file
        memberFileHandler.saveMembers(memberRepository.findAll());
        paymentFileHandler.savePayments(paymentRepository.findAll(), paymentFilePath);
    }
}
