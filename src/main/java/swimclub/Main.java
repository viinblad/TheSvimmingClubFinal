package swimclub;

import swimclub.utilities.FileHandler;
import swimclub.controllers.*;
import swimclub.repositories.*;
import swimclub.services.*;
import swimclub.ui.UserInterface;

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
        String competitionResultsFilePath = "src/main/resources/competitionResults.dat";
        String staffFilePath = "src/main/resources/staff.dat";
        String trainingResultsFilePath = "src/main/resources/trainingResults.dat";


        // Initialize the FileHandler for members, payments, reminders, and teams
        FileHandler fileHandler = new FileHandler(memberFilePath, paymentFilePath, reminderFilePath, paymentRatesFilePath, teamsFilePath, competitionResultsFilePath, staffFilePath,trainingResultsFilePath);

        // Initialize the repositories, passing the respective FileHandlers
        MemberRepository memberRepository = new MemberRepository(fileHandler);
        PaymentRepository paymentRepository = new PaymentRepository(reminderFilePath); // Pass the reminder file path
        CompetitionResultRepository competitionResultRepository = new CompetitionResultRepository(fileHandler, competitionResultsFilePath);

        StaffRepository staffRepository = new StaffRepository(fileHandler);
        TrainingResultsRepository trainingResultsRepository = new TrainingResultsRepository(fileHandler,trainingResultsFilePath);
        // Load members, payments, and teams from the file
        memberRepository.reloadMembers(); // Load member data
        paymentRepository.loadPayments(paymentFilePath, memberRepository); // Load payment data

        competitionResultRepository.loadResults(memberRepository); // Load competition results
        trainingResultsRepository.loadResults(memberRepository);




        // Initialize services for member, payment and competition results
        MemberService memberService = new MemberService(memberRepository);
        PaymentService paymentService = new PaymentService(paymentRepository, fileHandler);
        CompetitionResultService competitionResultService = new CompetitionResultService(competitionResultRepository);
        TrainingResultsService trainingResultsService = new TrainingResultsService(trainingResultsRepository);
        // Initialize TeamRepository
        TeamRepository teamRepository = new TeamRepository(fileHandler);
        teamRepository.loadTeams(memberRepository, staffRepository);  // Pass MemberRepository til loadTeams

        // Initialize TeamService with TeamRepository
        TeamService teamService = new TeamService(teamRepository);

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

        CompetitionResultController competitionResultController = new CompetitionResultController(competitionResultService);
        TrainingResultsController trainingResultsController = new TrainingResultsController(trainingResultsService);

        // Instantiate the UserInterface, passing all controllers (including teamController)
        UserInterface userInterface = new UserInterface(memberController, paymentController, teamController, competitionResultController, staffController,trainingResultsController);


        // Start the User Interface to handle interactions
        userInterface.start();

        // After user interaction, save any changes to file
        fileHandler.saveMembers(memberRepository.findAll()); // Save updated members
        fileHandler.savePayments(paymentRepository.findAll(), paymentFilePath); // Save updated payments
        // Reminder data is managed by PaymentRepository, so no need to handle it here
        // Save the teams after user interaction
        fileHandler.saveTeams(teamController.getAllTeams()); // Save teams to the file
        fileHandler.saveCompetitionResults(competitionResultRepository.getAllResults(), competitionResultsFilePath); // Save competition results to the file
    }
}
