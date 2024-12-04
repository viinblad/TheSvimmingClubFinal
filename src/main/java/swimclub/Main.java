package swimclub;

import swimclub.controllers.MemberController;
import swimclub.controllers.PaymentController;
import swimclub.controllers.StaffController;
import swimclub.controllers.TeamController;
import swimclub.repositories.MemberRepository;
import swimclub.repositories.PaymentRepository;
import swimclub.repositories.StaffRepository;
import swimclub.repositories.TeamRepository;  // Import TeamRepository
import swimclub.services.MemberService;
import swimclub.services.PaymentService;
import swimclub.services.StaffService;
import swimclub.services.TeamService;  // Import TeamService
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
        // File paths for member data, payment data, reminder data, and teams data
        String memberFilePath = "src/main/resources/members.dat";
        String paymentFilePath = "src/main/resources/payments.dat";
        String reminderFilePath = "src/main/resources/reminders.dat";
        String paymentRatesFilePath = "src/main/resources/paymentRates.dat";
        String teamsFilePath = "src/main/resources/teams.dat"; // Path for teams data
        String staffFilePath = "src/main/resources/staff.dat";

        // Initialize the FileHandler for members, payments, reminders, and teams
        FileHandler fileHandler = new FileHandler(memberFilePath, paymentFilePath, reminderFilePath, paymentRatesFilePath, teamsFilePath, staffFilePath);

        // Initialize the repositories, passing the respective FileHandlers
        MemberRepository memberRepository = new MemberRepository(fileHandler);
        PaymentRepository paymentRepository = new PaymentRepository(reminderFilePath); // Pass the reminder file path
        StaffRepository staffRepository = new StaffRepository(fileHandler);
        // Load members, payments, and teams from the file
        memberRepository.reloadMembers(); // Load member data
        paymentRepository.loadPayments(paymentFilePath, memberRepository); // Load payment data

        // Initialize services for member and payment
        MemberService memberService = new MemberService(memberRepository);
        PaymentService paymentService = new PaymentService(paymentRepository, fileHandler);

        // Initialize TeamRepository
        TeamRepository teamRepository = new TeamRepository(fileHandler);
        teamRepository.loadTeams(memberRepository, staffRepository);  // Pass MemberRepository til loadTeams

        // Initialize TeamService with TeamRepository
        TeamService teamService = new TeamService(teamRepository);  // Pass TeamRepository to TeamService

        //intialize staffservice.
        StaffService staffService = new StaffService(staffRepository);

        // Instantiate the controllers
        MemberController memberController = new MemberController(memberService, memberRepository);
        TeamController teamController = new TeamController(teamService);  // Pass TeamService to TeamController
        StaffController staffController = new StaffController(staffService, staffRepository);
        PaymentController paymentController = new PaymentController(
                paymentService,
                memberRepository,
                fileHandler,
                paymentFilePath,
                paymentRatesFilePath
        );

        // Instantiate the UserInterface, passing all controllers (including teamController)
        UserInterface userInterface = new UserInterface(memberController, paymentController, teamController, staffController);

        // Start the User Interface to handle interactions
        userInterface.start();

        // After user interaction, save any changes to file
        fileHandler.saveMembers(memberRepository.findAll()); // Save updated members
        fileHandler.savePayments(paymentRepository.findAll(), paymentFilePath); // Save updated payments
        // Reminder data is managed by PaymentRepository, so no need to handle it here
        // Save the teams after user interaction
        fileHandler.saveTeams(teamController.getAllTeams()); // Save teams to the file
    }
}
