package swimclub;

import swimclub.utilities.FileHandler;
import swimclub.controllers.*;
import swimclub.repositories.*;
import swimclub.services.*;
import swimclub.ui.UserInterface;

public class Main {
    public static void main(String[] args) {
        // File paths for various data files
        String memberFilePath = "src/main/resources/members.dat";
        String paymentFilePath = "src/main/resources/payments.dat";
        String reminderFilePath = "src/main/resources/reminders.dat";
        String paymentRatesFilePath = "src/main/resources/paymentRates.dat";
        String teamsFilePath = "src/main/resources/teams.dat";
        String competitionResultsFilePath = "src/main/resources/competitionResults.dat";
        String staffFilePath = "src/main/resources/staff.dat";
        String trainingResultsFilePath = "src/main/resources/trainingResults.dat";
        String authFilePath = "src/main/resources/users.dat";

        // Initialize FileHandler for managing file operations
        FileHandler fileHandler = new FileHandler(
                memberFilePath, paymentFilePath, reminderFilePath, paymentRatesFilePath,
                teamsFilePath, competitionResultsFilePath, staffFilePath, trainingResultsFilePath
        );

        // Initialize the repositories
        MemberRepository memberRepository = new MemberRepository(fileHandler);
        PaymentRepository paymentRepository = new PaymentRepository(reminderFilePath);
        CompetitionResultRepository competitionResultRepository = new CompetitionResultRepository(fileHandler, competitionResultsFilePath);
        StaffRepository staffRepository = new StaffRepository(fileHandler);
        TrainingResultsRepository trainingResultsRepository = new TrainingResultsRepository(fileHandler, trainingResultsFilePath, memberRepository);
        UserRepository userRepository = new UserRepository(authFilePath);  // Use UserRepository for user management
        AuthRepository authRepository = new AuthRepository(authFilePath);

        // Load data from the repositories
        memberRepository.reloadMembers();
        paymentRepository.loadPayments(paymentFilePath, memberRepository);
        competitionResultRepository.loadResults(memberRepository);
        trainingResultsRepository.loadResults(memberRepository);

        // Initialize services
        MemberService memberService = new MemberService(memberRepository);
        PaymentService paymentService = new PaymentService(paymentRepository, fileHandler);
        CompetitionResultService competitionResultService = new CompetitionResultService(competitionResultRepository);
        TrainingResultsService trainingResultsService = new TrainingResultsService(trainingResultsRepository);

        // Initialize the necessary repositories for teams and staff
        TeamRepository teamRepository = new TeamRepository(fileHandler);
        teamRepository.loadTeams(memberRepository, staffRepository);
        TeamService teamService = new TeamService(teamRepository);

        // Initialize services for staff and authentication
        StaffService staffService = new StaffService(staffRepository);
        AuthService authService = new AuthService(authRepository);

        // Instantiate the controllers
        MemberController memberController = new MemberController(memberService, memberRepository);
        TeamController teamController = new TeamController(teamService);
        StaffController staffController = new StaffController(staffService, staffRepository);
        CompetitionResultController competitionResultController = new CompetitionResultController(competitionResultService);
        TrainingResultsController trainingResultsController = new TrainingResultsController(trainingResultsService, trainingResultsRepository);
        AdminController adminController = new AdminController(authService, userRepository);  // Pass AuthService and UserRepository to AdminController
        PaymentController paymentController = new PaymentController(paymentService, memberRepository, fileHandler, paymentFilePath, paymentRatesFilePath, adminController);

        // Initialize the UserInterface and pass all controllers to it
        UserInterface userInterface = new UserInterface(memberController,
                paymentController,
                teamController,
                competitionResultController,
                staffController,
                trainingResultsController,
                adminController);

        // Start the User Interface
        userInterface.start();

        // After user interaction, save the updated data
        fileHandler.saveMembers(memberRepository.findAll());
        fileHandler.savePayments(paymentRepository.findAll(), paymentFilePath);
        fileHandler.saveTeams(teamController.getAllTeams());
        fileHandler.saveCompetitionResults(competitionResultRepository.getAllResults(), competitionResultsFilePath);
    }
}
