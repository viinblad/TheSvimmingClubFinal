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
        // Define file paths for various data files
        String memberFilePath = "src/main/resources/members.dat";
        String paymentFilePath = "src/main/resources/payments.dat";
        String reminderFilePath = "src/main/resources/reminders.dat";
        String paymentRatesFilePath = "src/main/resources/paymentRates.dat";
        String teamsFilePath = "src/main/resources/teams.dat"; // Path for teams data
        String competitionResultsFilePath = "src/main/resources/competitionResults.dat";
        String staffFilePath = "src/main/resources/staff.dat";
        String trainingResultsFilePath = "src/main/resources/trainingResults.dat";

        // Initialize FileHandler for managing file operations across various data sets
        FileHandler fileHandler = new FileHandler(
                memberFilePath,
                paymentFilePath,
                reminderFilePath,
                paymentRatesFilePath,
                teamsFilePath,
                competitionResultsFilePath,
                staffFilePath,
                trainingResultsFilePath
        );

        // Initialize repositories for member data, payments, competition results, staff, and training results
        MemberRepository memberRepository = new MemberRepository(fileHandler);
        PaymentRepository paymentRepository = new PaymentRepository(reminderFilePath); // Pass the reminder file path
        CompetitionResultRepository competitionResultRepository = new CompetitionResultRepository(fileHandler, competitionResultsFilePath);
        StaffRepository staffRepository = new StaffRepository(fileHandler);
        TrainingResultsRepository trainingResultsRepository = new TrainingResultsRepository(fileHandler, trainingResultsFilePath);

        // Load the data from files into repositories
        memberRepository.reloadMembers(); // Load member data from file
        paymentRepository.loadPayments(paymentFilePath, memberRepository); // Load payment data
        competitionResultRepository.loadResults(memberRepository); // Load competition results
        trainingResultsRepository.loadResults(memberRepository); // Load training results

        // Initialize services for handling member, payment, competition result, and training result logic
        MemberService memberService = new MemberService(memberRepository);
        PaymentService paymentService = new PaymentService(paymentRepository, fileHandler);
        CompetitionResultService competitionResultService = new CompetitionResultService(competitionResultRepository);
        TrainingResultsService trainingResultsService = new TrainingResultsService(trainingResultsRepository);

        // Initialize TeamRepository and load teams from file
        TeamRepository teamRepository = new TeamRepository(fileHandler);
        teamRepository.loadTeams(memberRepository, staffRepository);  // Pass MemberRepository to loadTeams

        // Initialize TeamService with the TeamRepository
        TeamService teamService = new TeamService(teamRepository);

        // Initialize StaffService for managing staff members
        StaffService staffService = new StaffService(staffRepository);

        // Instantiate controllers that handle user interactions and logic delegation
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

        // Instantiate the UserInterface and pass all controllers to it
        UserInterface userInterface = new UserInterface(
                memberController,
                paymentController,
                teamController,
                competitionResultController,
                staffController,
                trainingResultsController
        );

        // Start the User Interface to handle interactions with the user
        userInterface.start();

        // After user interaction, save any changes back to the respective files
        fileHandler.saveMembers(memberRepository.findAll()); // Save updated members
        fileHandler.savePayments(paymentRepository.findAll(), paymentFilePath); // Save updated payments
        // Reminder data is managed by PaymentRepository, so no need to handle it here
        fileHandler.saveTeams(teamController.getAllTeams()); // Save teams to the file
        fileHandler.saveCompetitionResults(competitionResultRepository.getAllResults(), competitionResultsFilePath); // Save competition results to the file
    }
}