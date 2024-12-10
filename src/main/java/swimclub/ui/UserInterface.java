package swimclub.ui;

import swimclub.controllers.*;
import swimclub.models.*;
import swimclub.utilities.PasswordUtils;
import swimclub.utilities.Validator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.text.DecimalFormat;
/**
 * UserInterface handles the interaction between the user and the program.
 * It allows the user to register, update, and view members, as well as manage payments.
 */
public class UserInterface {
    private final MemberController memberController;  // Controller to handle member actions
    private final PaymentController paymentController;  // Controller to handle payment actions
    private final TeamController teamController;  // Controller to handle team actions
    private final StaffController staffController;
    private final CompetitionResultController competitionResultController;
    private final TrainingResultsController trainingResultsController;
    private final AdminController adminController;
    private final Scanner scanner; // Scanner to read user input


    /**
     * Constructor to initialize the UserInterface with the provided controllers.
     * This constructor sets up the necessary controllers for managing members, payments, teams, competition results, staff, training results, and admin functionalities.
     *
     * @param memberController             The controller that handles the logic for member actions, such as registering, searching, and updating members.
     * @param paymentController            The controller that manages payment-related actions, including registering and viewing payments.
     * @param teamController               The controller that handles team-related actions, such as creating and managing teams.
     * @param competitionResultController  The controller that manages competition results, including viewing and adding results.
     * @param staffController              The controller that handles staff-related actions, such as managing staff members and roles.
     * @param trainingResultsController    The controller that manages training results, including adding and viewing training outcomes for members.
     * @param adminController              The controller that handles admin functionalities, including user management (adding, updating, deleting users).
     */
    public UserInterface(MemberController memberController, PaymentController paymentController, TeamController teamController, CompetitionResultController competitionResultController, StaffController staffController, TrainingResultsController trainingResultsController, AdminController adminController) {

        this.memberController = memberController;
        this.paymentController = paymentController;
        this.teamController = teamController;
        this.competitionResultController = competitionResultController;
        this.staffController = staffController;
        this.trainingResultsController = trainingResultsController;
        this.adminController = adminController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the user interface, displaying the login prompt (adminMenu) and handling user input.
     */

    public void start() {
        boolean loggedIn = false;

        // Loop until login is successful
        while (!loggedIn) {
            try {
                adminMenu();  // The adminMenu method handles login and role-based menu redirection
                loggedIn = true;  // If login succeeds, exit the loop
            } catch (IllegalArgumentException e) {
                System.out.println("Login failed: " + e.getMessage());
                System.out.println("Please try again.\n");
            }
        }
    }

    public void adminMenu() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        try {
            User user = adminController.login(username, password);
            System.out.println("Welcome, " + user.getUsername() + " (" + user.getRole() + ")!");

            // Redirect to role-specific menus
            switch (user.getRole()) {
                case ADMIN -> showAdminMenu(); // Full access for Admin
                case CHAIRMAN -> showChairmanMenu(); // Access for Chairman
                case TREASURER -> showTreasurerMenu(); // Access for Treasurer
                case COACH -> showCoachMenu(); // Access for Coach
                default -> System.out.println("Role not authorized.");
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid username or password."); // Rethrow exception for the start method
        }
    }

    /**
     * Displays the Admin menu and handles the admin-related functions.
     * The Admin menu allows the creation of new users and access to different management areas.
     */
    private void showAdminMenu() {
        int option = -1;
        do {
            System.out.println("\n--- Admin Functions ---");
            System.out.println("1. Create New Admin User");
            System.out.println("2. Manage Users");
            System.out.println("3. Manage Members");
            System.out.println("4. Manage Teams");
            System.out.println("5. Manage Payments");
            System.out.println("6. Manage Competitions");
            System.out.println("7. Manage Training Results");
            System.out.println("8. Exit");
            System.out.print("Please choose an option (1-8): ");

            try {
                option = Integer.parseInt(scanner.nextLine());  // Read user input and parse it to an integer

                switch (option) {
                    case 8 -> {
                        exitProgram();  // Call exitProgram() to exit the program
                        System.out.println("Returning to Main Menu...");  // Notify the user before exiting
                    }
                    default -> handleAdminOptions(option);  // Handle selected option in the menu
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 8.");
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
            }
        } while (true);  // Keep running until option 8 is selected to exit
    }

    /**
     * Handles the options selected by the admin, such as creating new users, managing members, etc.
     *
     * @param option The selected option from the admin menu.
     */
    private void handleAdminOptions(int option) {
        try {
            switch (option) {
                case 1 -> createNewAdminUser();  // Admin can create new admin users
                case 2 -> manageUsers();  // Admin can manage existing users (list, remove, etc.)
                case 3 -> manageMembers();  // Manage members (available for Chairman)
                case 4 -> manageTeams();  // Manage teams (available for Chairman, Coach)
                case 5 -> handlePayments();  // Handle payments (available for Chairman, Treasurer)
                case 6 -> manageCompetitions();  // Manage competitions (available for Chairman)
                case 7 -> manageTrainingResults();  // Manage training results (available for Chairman, Coach)
                default -> System.out.println("Invalid option. Please choose a valid number.");
            }
        } catch (Exception e) {
            // Catch any exception that occurs while handling options
            System.err.println("An error occurred while processing your option: " + e.getMessage());
        }
    }




    /**
     * Allows the admin to create a new user with a specified role (Admin, Chairman, Treasurer, Coach).
     * The password is validated, hashed, and the user is added to the system.
     */
    private void createNewAdminUser() {
        try {
            System.out.print("Enter username for new user: ");
            String username = scanner.nextLine();

            // Validate that the username is not empty
            while (username.trim().isEmpty()) {
                System.out.println("Username cannot be empty.");
                System.out.print("Enter username for new user: ");
                username = scanner.nextLine();
            }

            System.out.print("Enter password for new user: ");
            String password = scanner.nextLine();

            // Validate password length and security
            while (password.length() < 6) {
                System.out.println("Password must be at least 6 characters long.");
                System.out.print("Enter password for new user: ");
                password = scanner.nextLine();
            }

            // Hash the password and generate a salt
            String salt = PasswordUtils.generateSalt();  // Generate salt for password hashing
            String hashedPassword = PasswordUtils.hashPassword(password, salt);  // Hash the password

            // Ask for the role of the new user
            System.out.println("Select role for the new user:");
            System.out.println("1. CHAIRMAN");
            System.out.println("2. TREASURER");
            System.out.println("3. COACH");
            System.out.println("4. ADMIN");
            System.out.print("Enter the number for the role: ");

            int roleOption = -1;
            boolean validRole = false;
            Role role = null;

            // Validate user input for role selection
            while (!validRole) {
                try {
                    roleOption = Integer.parseInt(scanner.nextLine());
                    switch (roleOption) {
                        case 1 -> { role = Role.CHAIRMAN; validRole = true; }
                        case 2 -> { role = Role.TREASURER; validRole = true; }
                        case 3 -> { role = Role.COACH; validRole = true; }
                        case 4 -> { role = Role.ADMIN; validRole = true; }
                        default -> System.out.println("Invalid role selected. Please choose a valid number between 1 and 4.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number between 1 and 4.");
                }
            }

            // Create the new user with the selected role
            User newUser = new User(username, hashedPassword, salt, role);

            // Add the new user to the repository
            adminController.register(username, password, role);  // Using AdminController to register the new user

            // Confirm the user was created
            System.out.println("User created successfully with role: " + role);
        } catch (Exception e) {
            System.err.println("An error occurred while creating the new user: " + e.getMessage());
        }
    }

    /**
     * Allows the admin to manage users. This includes viewing, deleting, and updating user details.
     */
    private void manageUsers() {
        System.out.println("\n--- Manage Users ---");
        System.out.println("1. List All Users");
        System.out.println("2. Delete User");
        System.out.println("3. Update User");
        System.out.println("4. Exit to Admin Menu");
        System.out.print("Please choose an option (1-4): ");

        int option;
        try {
            option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1 -> listAllUsers();  // List all users
                case 2 -> deleteUser();     // Delete a user
                case 3 -> updateUser();     // Update user details
                case 4 -> System.out.println("Returning to Admin Menu..."); // Exit to Admin Menu
                default -> System.out.println("Invalid option. Please choose a valid number between 1 and 4.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 4.");
        }
    }

    /**
     * Displays a list of all users in the system.
     */
    /**
     * Displays a list of all users in the system.
     */
    private void listAllUsers() {
        System.out.println("\n--- All Users ---");
        // Call the listUsers method from AdminController to display the list of users
        adminController.listUsers(); // This will display all users
    }

    /**
     * Prompts the admin to delete a user by their username.
     */
    private void deleteUser() {
        System.out.print("Enter the username of the user to delete: ");
        String username = scanner.nextLine();

        // Try to delete the user using AdminController
        try {
            adminController.deleteUser(adminController.getLoggedInUsername(), adminController.getLoggedInPassword(), username);
            System.out.println("User '" + username + "' has been deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    /**
     * Allows the admin to update the details of an existing user.
     */
    private void updateUser() {
        System.out.print("Enter the username of the user to update: ");
        String username = scanner.nextLine();

        // Try to find the user to update using the AdminController
        User userToUpdate = adminController.getUserByUsername(username);
        if (userToUpdate != null) {
            // Prompt for new details (e.g., password, role, etc.)
            System.out.print("Enter new password for the user (leave blank to keep current): ");
            String newPassword = scanner.nextLine();

            if (!newPassword.isEmpty()) {
                // If a new password is provided, hash and update it
                String salt = PasswordUtils.generateSalt();
                String hashedPassword = PasswordUtils.hashPassword(newPassword, salt);
                userToUpdate.setPassword(hashedPassword);
                userToUpdate.setSalt(salt);
            }

            System.out.println("Choose new role for the user:");
            System.out.println("1. CHAIRMAN");
            System.out.println("2. TREASURER");
            System.out.println("3. COACH");
            System.out.println("4. ADMIN");
            System.out.print("Enter the number for the role: ");
            int roleOption = Integer.parseInt(scanner.nextLine());

            Role role;
            switch (roleOption) {
                case 1 -> role = Role.CHAIRMAN;
                case 2 -> role = Role.TREASURER;
                case 3 -> role = Role.COACH;
                case 4 -> role = Role.ADMIN;
                default -> {
                    System.out.println("Invalid role. Keeping current role.");
                    role = userToUpdate.getRole();  // Keep the current role if invalid input
                }
            }

            // Update user role
            userToUpdate.setRole(role);

            // Save the updated user details back to the repository
            try {
                adminController.updateUser(adminController.getLoggedInUsername(), adminController.getLoggedInPassword(), username, newPassword, role);
                System.out.println("User '" + username + "' has been updated successfully.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } else {
            System.out.println("User not found.");
        }
    }



    /**
     * Displays the menu for Chairman-specific functionality and redirects to handleOption() based on the selected option.
     */
    private void showChairmanMenu() {
        // Display the Chairman-specific menu options
        System.out.println("\n--- Chairman Functions ---");
        System.out.println("1. Register New Member");
        System.out.println("2. Search Members");
        System.out.println("3. Update Member");
        System.out.println("4. View All Members");
        System.out.println("5. Delete Member");
        System.out.println("6. Handle Payments");
        System.out.println("7. Manage Teams");
        System.out.println("8. Manage Competitions");
        System.out.println("9. Manage Training Results");
        System.out.println("10. Exit");
        System.out.print("Please choose an option (1-10): ");

        // Read user's option input and call handleOption() directly
        int option = Integer.parseInt(scanner.nextLine());
        handleOption(option);  // This will redirect based on the option selected by the chairman
    }

    /**
     * Displays the menu for Treasurer-specific functionality and redirects to handlePayments() directly.
     */
    private void showTreasurerMenu() {
        // Directly call  handlePayments when the user logs in as a treasurer
        handlePayments();
    }

    /**
     * Displays the menu for Coach-specific functionality and redirects to manageTeams() directly.
     */
    private void showCoachMenu() {
        // Directly call manageTeams when the user logs in as a coach
        manageTeams();
    }

    /**
     * Handles the user's selected menu option.
     * Depending on the option, it performs specific actions for members, payments, teams, competitions, and training results.
     *
     * @param option The selected option from the menu (1-10).
     */
    private void handleOption(int option) {
        try {
            switch (option) {
                case 1 -> registerMember();  // Register a new member
                case 2 -> searchMembers();  // Search members
                case 3 -> updateMember();  // Update member details
                case 4 -> memberController.viewAllMembers();  // View all members
                case 5 -> deleteMember();  // Delete a member
                case 6 -> handlePayments();  // Handle payments
                case 7 -> manageTeams();  // Manage teams
                case 8 -> manageCompetitions();  // Manage competitions
                case 9 -> manageTrainingResults();  // Manage training results
                case 10 -> exitProgram();  // Exit the program
                default -> System.out.println("Invalid option. Please choose a number between 1 and 10.");
            }
        } catch (Exception e) {
            // Catch and display any exceptions that occur during handling options
            System.err.println("An error occurred while processing your choice: " + e.getMessage());
        }
    }

    /**
     * Manages member-related operations such as registering, searching, updating,
     * viewing, and deleting members. Redirects to the appropriate function based on the user's choice.
     */
    private void manageMembers() {
        int option;
        do {
            System.out.println("\n--- Member Management ---");
            System.out.println("1. Register New Member");
            System.out.println("2. Search Members");
            System.out.println("3. Update Member");
            System.out.println("4. View All Members");
            System.out.println("5. Delete Member");
            System.out.println("6. Back to Main Menu");
            System.out.print("Please choose an option (1-6): ");

            try {
                option = Integer.parseInt(scanner.nextLine());
                switch (option) {
                    case 1 -> registerMember();  // Register a new member
                    case 2 -> searchMembers();  // Search members
                    case 3 -> updateMember();  // Update member details
                    case 4 -> memberController.viewAllMembers();  // View all members
                    case 5 -> deleteMember();  // Delete a member
                    case 6 -> System.out.println("Returning to Main Menu..."); // Exit submenu
                    default -> System.out.println("Invalid option. Please choose a number between 1 and 6.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 6.");
                option = -1; // Ensure loop continues on invalid input
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                option = -1; // Ensure loop continues if an exception is caught
            }
        } while (option != 6); // Exit loop when the user selects option 6
    }

    /**
     * Exits the program.
     */
    private void exitProgram() {
        System.out.println("Exiting the program. Goodbye!");  // Print a goodbye message
        System.exit(0);  // Exit the program
    }

    /**
     * Registers a new member by collecting their details and passing them to the controller.
     */
    private void registerMember() {
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        String age = correctAgeInput(scanner, "Enter member age:");
        String email = correctEmailInput(scanner, "Enter email:");
        System.out.print("Enter city of member:");
        String city = scanner.nextLine();
        System.out.print("Enter street of member:");
        String street = scanner.nextLine();
        System.out.print("Enter region of member:");
        String region = scanner.nextLine();
        String zipcode = correctZipCodeInput(scanner, "Enter Zip code (4 digits):");
        System.out.print("Enter membership type (Junior/Senior, Competitive/Exercise): ");
        String membershipType = scanner.nextLine();
        System.out.print("Enter activity type (Breaststroke, Crawl, Backcrawl or Butterfly):");
        String activityType = scanner.nextLine();
        String phoneNumber = correctPHInput(scanner, "Enter phone number (8 digits):");

        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;  // Default membership status
        PaymentStatus paymentStatus = PaymentStatus.PENDING;  // Default payment status

        // Call the controller to register the new member
        Member newMember = memberController.registerMember(name, email, city, street, region, Integer.parseInt(zipcode), membershipType, membershipStatus, activityType, paymentStatus, age, Integer.parseInt(phoneNumber));

        if (newMember != null) {
            // Calculate the membership fee
            double fee = paymentController.calculateMembershipFeeForMember(newMember.getMemberId());

            // Prompt user to pay now or later
            System.out.println("\nMembership fee for " + newMember.getName() + " is " + fee + " DKK");
            System.out.print("Do you want to pay now? (yes/no): ");
            String choice = scanner.nextLine().toLowerCase();

            if (choice.equals("yes")) {
                // Register the payment immediately
                paymentController.registerPayment(newMember.getMemberId(), fee);
            } else {
                // Provide reminder information and set a reminder
                System.out.println("A reminder will be sent to: " + email);
                paymentController.setPaymentReminder(newMember.getMemberId(), "Payment reminder for " + newMember.getName());
            }
        }
    }

    /**
     * Updates an existing member's information based on the provided member ID and new details.
     */
    private void updateMember() {
        System.out.print("Enter member ID to update: ");
        int memberId = Integer.parseInt(scanner.nextLine());
        // Gather all the required updated attributes
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        String email = correctEmailInput(scanner, "Enter new Email:");
        String age = correctAgeInput(scanner, "Enter new age:");
        System.out.print("Enter new city: ");
        String city = scanner.nextLine();
        System.out.print("Enter new street: ");
        String street = scanner.nextLine();
        System.out.print("Enter new region: ");
        String region = scanner.nextLine();
        String zipcode = correctZipCodeInput(scanner, "Enter new zipcode");
        System.out.print("Enter new membership type (Junior/Senior, Competitive/Exercise): ");
        String membershipType = scanner.nextLine();
        System.out.println("Enter new activity type (Crawl, Backcrawl, Breathstroke or Butterfly):");
        String activitytype = scanner.nextLine();
        String phoneNumber = correctPHInput(scanner, "Enter new phone number (8 digits): ");

        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;  // Default to ACTIVE
        PaymentStatus paymentStatus = PaymentStatus.PENDING;  // Default to PENDING

        // Call the controller's updateMember method with all new attributes
        memberController.updateMember(memberId, name, email, age, city, street, region, Integer.parseInt(zipcode), membershipType, membershipStatus, activitytype, paymentStatus, Integer.parseInt(phoneNumber));
    }

    /**
     * Manages team-related operations such as creating, viewing, updating,
     * and deleting teams. Redirects to the appropriate function based on the user's choice.
     */
    private void manageTeams() {
        int teamOption;
        do {
            System.out.println("\n--- Team Management ---");
            System.out.println("1. Create Team");
            System.out.println("2. Add Member to Team");
            System.out.println("3. Remove Member from Team");
            System.out.println("4. Assign Coach to team");
            System.out.println("5. Remove Coach from team");
            System.out.println("6. Register new Coach to the Swimming Club");
            System.out.println("7. View Teams");
            System.out.println("8. Delete Team");
            System.out.println("9. Back to Main Menu");
            System.out.print("Please choose an option (1-9): ");

            try {
                teamOption = Integer.parseInt(scanner.nextLine());
                switch (teamOption) {
                    case 1 -> createTeam();  // Create a new team
                    case 2 -> addMemberToTeam();  // Add a member to a team
                    case 3 -> removeMemberFromTeam();  // Remove a member from a team
                    case 4 -> assignTeamCoach();  // Assign a coach to a team
                    case 5 -> removeTeamCoach();  // Remove a coach from a team
                    case 6 -> registerCoach();  // Register a new coach to the swimming club
                    case 7 -> viewTeams();  // View all teams
                    case 8 -> deleteTeam();  // Delete a team
                    case 9 -> manageTrainingResults();  // Manage training results
                    case 10 -> manageCompetitions();  // Manage competitions results
                    case 11 -> System.out.println("Returning to Main Menu...");  // Exit to main menu
                    default -> System.out.println("Invalid option. Please choose a valid number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 9.");
                teamOption = -1; // Ensure loop continues on invalid input
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                teamOption = -1; // Ensure loop continues if an exception is caught
            }
        } while (teamOption != 11); // Exit loop when option 9 is selected
    }


    /**
     * Registers a new coach and optionally assigns them to a team.
     */

    private void registerCoach() {
        // Prompt for coach's name
        System.out.print("Enter coach name: ");
        String name = scanner.nextLine();

        // Validate and input coach's details
        String ageString = correctAgeInput(scanner, "Enter coach age:"); // Validate age input
        int age = Integer.parseInt(ageString); // Convert age to int

        String email = correctEmailInput(scanner, "Enter coach email:"); // Validate email input
        System.out.print("Enter city of coach: ");
        String city = scanner.nextLine();
        System.out.print("Enter street of coach: ");
        String street = scanner.nextLine();
        System.out.print("Enter region of coach: ");
        String region = scanner.nextLine();
        String zipcodeString = correctZipCodeInput(scanner, "Enter Zip code (4 digits):"); // Validate zip code input
        int zipcode = Integer.parseInt(zipcodeString); // Convert zip code to int

        String phoneNumberString = correctPHInput(scanner, "Enter phone number (8 digits):"); // Validate phone number input
        int phoneNumber = Integer.parseInt(phoneNumberString); // Convert phone number to int

        // Set the default role for a coach
        Role coachRole = Role.COACH;

        // Call the controller to register the new coach
        Coach newCoach = staffController.registerCoach(null, name, email, city, street, region, zipcode, age, phoneNumber, coachRole);

        if (newCoach != null) {
            // Ask user if they want to assign the coach to a team
            System.out.print("Do you want to assign the coach to a team now? (yes/no): ");
            String choice = scanner.nextLine().toLowerCase();

            if (choice.equals("yes")) {
                // Print all teams and check if there are any teams available
                boolean foundTeams = teamController.printAllTeams();
                if (!foundTeams) {
                    // If no teams are found, display a message and exit the method
                    System.out.println("No teams found on the list.");
                    return; // Exit if no teams are available
                }

                // Prompt for the team name to assign the coach to
                System.out.print("Enter the name of the team the new coach should be assigned to: ");
                String teamName = scanner.nextLine().trim();

                // Check if the entered team name exists in the list of teams
                Team team = teamController.findTeamByName(teamName);
                if (team == null) {
                    // If the team doesn't exist, notify the user and exit
                    System.out.println("Team '" + teamName + "' not found.");
                    return;  // Exit if the team is not found
                }

                // Attempt to assign the coach to the team
                try {
                    teamController.assignTeamCoach(teamName, newCoach);// Assigning the new coach to the team
                    staffController.setCoachTeamName(teamName, newCoach);
                    System.out.println("Coach '" + newCoach.getName() + "' assigned to team '" + teamName + "'.");
                } catch (IllegalArgumentException e) {
                    System.out.println("Error assigning team coach: " + e.getMessage());
                }
            } else if (choice.equals("no")) {
                // If user chooses not to assign the coach to any team
                System.out.println("Coach '" + newCoach.getName() + "' has been registered but is not assigned to any team.");
            } else {
                // Handle invalid input for 'yes' or 'no'
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        } else {
            // If the coach registration fails
            System.out.println("Failed to register the coach.");
        }
    }


    /**
     * Removes the coach from the specified team.
     * <p>
     * This method prints all the available teams, prompts the user to input a team name,
     * and removes the coach from that team. If the team or coach is not found, it will print
     * an error message.
     */
    private void removeTeamCoach() {
        // Print all teams and check if there are any teams available in the system
        boolean foundTeams = teamController.printAllTeams();
        if (!foundTeams) {
            // If no teams are found, display a message and exit the method
            System.out.println("No teams found on the list.");
            return; // Exit if no teams are available
        }

        // Prompt the user for the team name to remove the coach from
        System.out.print("Enter the name of the team of which the coach has to be removed: ");
        String teamName = scanner.nextLine().trim();  // Read the team name input from the user

        // Attempt to find the coach of the team and remove them
        try {
            // Find the team by name
            Team team = teamController.findTeamByName(teamName);
            if (team == null) {
                throw new IllegalArgumentException("Team not found.");
            }

            // Check if the team has a coach
            Coach teamCoach = team.getTeamCoach();
            if (teamCoach == null) {
                throw new IllegalArgumentException("No coach assigned to the team '" + teamName + "'.");
            }

            // Print the confirmation message before removing the coach
            System.out.println("Coach '" + teamCoach.getName() + "' removed from team '" + teamName + "'.");

            // Remove the coach from the team
            teamController.removeTeamCoach(teamName);

            // Set the team name of the coach to null as they are no longer assigned to the team
            staffController.findCoachById(teamCoach.getCoachId()).setTeamName(null);

        } catch (IllegalArgumentException e) {
            // Handle any error in the process, such as no team found or no coach assigned
            System.out.println("Error removing team coach: " + e.getMessage());
        }
    }

    /**
     * Creates a new team by collecting the team name and type from the user.
     */
    private void createTeam() {
        System.out.print("Enter Team Name: ");
        String teamName = scanner.nextLine().trim();

        System.out.print("Enter Team Type (Junior Competitive / Senior Competitive): ");
        String teamTypeStr = scanner.nextLine().trim().toUpperCase();

        // Validate team type input
        if (!teamTypeStr.equals("JUNIOR COMPETITIVE") && !teamTypeStr.equals("SENIOR COMPETITIVE")) {
            System.out.println("Invalid team type. Please enter 'Junior Competitive' or 'Senior Competitive'.");
            return;
        }

        try {
            teamController.createTeam(teamName, teamTypeStr, null);
            System.out.println("Team '" + teamName + "' created successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error creating team: " + e.getMessage());
        }
    }

    /**
     * Adds a member to an existing team.
     */
    private void addMemberToTeam() {
        // Display the list of teams and allow the user to pick a team
        viewTeams();
        System.out.print("Enter Team Name: ");
        String teamName = scanner.nextLine().trim();  // User input for team name

        // Display the list of all members
        memberController.viewAllMembers();
        System.out.print("Enter Member ID to Add: ");

        // Initialize memberId and handle potential NumberFormatException
        int memberId;
        try {
            memberId = Integer.parseInt(scanner.nextLine());  // Parse the entered member ID
        } catch (NumberFormatException e) {
            System.out.println("Invalid Member ID. Please enter a valid number.");
            return;
        }

        // Retrieve the member by ID from the memberController
        Member member = memberController.findMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found.");
            return;  // If member is not found, exit the method
        }

        // Debugging output: Check the current team name
        System.out.println("Current team name of member " + member.getName() + ": " + member.getTeamName());

        // Check if the member is already part of a team
        // If the member's team is "no team" or null, they are unassigned
        String memberTeamName = member.getTeamName();
        if (memberTeamName == null || memberTeamName.trim().equalsIgnoreCase("no team")) {
            // Proceed with adding the member to the team
            Team team = teamController.findTeamByName(teamName);
            if (team == null) {
                System.out.println("Team '" + teamName + "' not found.");
                return;  // If team doesn't exist, exit the method
            }

            try {
                // Add member to the team
                teamController.addMemberToTeam(teamName, member); // Team logic
                memberController.addTeamToMember(member, teamName); // Member logic

                // Debugging output: Check if the team was updated for the member
                System.out.println("Member '" + member.getName() + "' has been assigned to team '" + teamName + "'.");
            } catch (IllegalArgumentException e) {
                System.out.println("Error adding member to team: " + e.getMessage());
            }
        } else {
            // If the member is already part of a team, show the error message
            System.out.println("Error: Member is already part of a team. Current team: " + member.getTeamName());
        }
    }

    /**
     * Removes a member from an existing team.
     */
    private void removeMemberFromTeam() {
        System.out.print("Enter Team Name: ");
        String teamName = scanner.nextLine().trim();

        System.out.print("Enter Member ID to Remove: ");
        int memberId;
        try {
            memberId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid Member ID. Please enter a valid number.");
            return;
        }

        Member member = memberController.findMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found.");
            return;
        }

        try {
            teamController.removeMemberFromTeam(teamName, member);
            memberController.removeTeamFromMember(member);
            System.out.println("Member '" + member.getName() + "' removed from team '" + teamName + "'.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error removing member from team: " + e.getMessage());
        }
    }

    /**
     * Assigns a team coach to a specific team.
     */
    private void assignTeamCoach() {
        // First, print all teams and check if there are any teams available in the system
        boolean foundTeams = teamController.printAllTeams();
        if (!foundTeams) {
            // If no teams are found, display a message and exit the method
            System.out.println("No teams found on the list.");
            return; // Exit if no teams are available
        }

        // Prompt the user for the team name
        System.out.print("Enter the name of the team the new coach should be assigned to: ");
        String teamName = scanner.nextLine().trim();  // Read the team name input from the user

        // Check if the entered team name exists in the list of teams
        Team team = teamController.findTeamByName(teamName);
        if (team == null) {
            // If the team doesn't exist, notify the user and exit
            System.out.println("Team '" + teamName + "' not found.");
            return;  // Exit if the team is not found
        }

        // Check if the team already has a coach
        if (team.getTeamCoach() != null) {
            System.out.println("This team already has a coach: '" + team.getTeamCoach().getName() + "'. Cannot assign a new coach.");
            return; // Exit if the team already has a coach
        }

        // Now that we know the team exists, check if there are any coaches available
        boolean foundCoaches = staffController.getCoachList();
        if (!foundCoaches) {
            // If no coaches are found, display a message and exit the method
            System.out.println("No coaches found on the coach list.");
            return;  // Exit if no coaches are available
        }

        // Prompt for the coach ID
        System.out.print("Enter the Coach ID of the coach you want to assign to the team: ");
        int coachId;
        try {
            coachId = Integer.parseInt(scanner.nextLine().trim());  // Ensure leading/trailing spaces are removed
        } catch (NumberFormatException e) {
            System.out.println("Invalid coach ID. Please enter a valid number.");
            return; // Exit on invalid input
        }

        // Find the coach by ID
        Coach coach = staffController.findCoachById(coachId);
        if (coach == null) {
            // If the coach isn't found, notify the user and exit
            System.out.println("Coach not found.");
            return; // Exit if the coach isn't found
        }

        // Attempt to assign the coach to the team
        try {
            teamController.assignTeamCoach(teamName, coach); // assigning coach to team.
            staffController.setCoachTeamName(teamName, coach);
                    // assigning team name to coach.
            staffController.saveCoachList(); // Save updated coach data
            System.out.println("Coach '" + coach.getName() + "' assigned as coach of team '" + teamName + "'.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error assigning team coach: " + e.getMessage());
        }
    }

    /**
     * Displays all teams and their details.
     */
    private void viewTeams() {
        List<Team> teams = teamController.getAllTeams();
        if (teams.isEmpty()) {
            System.out.println("No teams available.");
        } else {
            System.out.println("--- All Teams ---");
            for (Team team : teams) {
                System.out.println(team);
            }
        }
    }

    /**
     * Deletes a team by its name.
     */
    private void deleteTeam() {
        System.out.print("Enter Team Name to Delete: ");
        String teamName = scanner.nextLine().trim();

        try {
            teamController.deleteTeam(teamName);
            System.out.println("Team '" + teamName + "' deleted successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error deleting team: " + e.getMessage());
        }
    }


    /**
     * Deletes a member by their ID.
     */
    private void deleteMember() {
        System.out.println("Enter Member's ID:");
        int memberId = Integer.parseInt(scanner.nextLine());
        boolean success = memberController.deleteMember(memberId);
        if (success) {
            System.out.println("Member successfully deleted.");
        } else {
            System.out.println("Member not found, please check the ID and try again.");
        }
    }

    /**
     * Searches for members by ID, name, or phone number.
     */
    private void searchMembers() {
        System.out.print("Enter search query (ID, name, or phone number): ");
        String query = scanner.nextLine();
        List<Member> results = memberController.searchMembers(query);

        if (results.isEmpty()) {
            System.out.println("No members found matching the query.");
        } else {
            System.out.println("\n--- Search Results ---");
            results.forEach(member ->
                    System.out.println("ID: " + member.getMemberId() +
                            ", Name: " + member.getName() +
                            ", Membership: " + member.getMembershipDescription() +
                            ", Phone: " + member.getPhoneNumber() +
                            ", Email: " + member.getEmail()));
        }
    }

    /**
     * Handles payment-related operations such as registering payments, viewing payment history,
     * filtering members by payment status, managing payment reminders, and updating payment rates.
     */
    private void handlePayments() {
        int paymentOption;
        do {
            System.out.println("\n--- Payment Management ---");
            System.out.println("1. Register Payment");
            System.out.println("2. View Payments for Member");
            System.out.println("3. Filter Members by Payment Status");
            System.out.println("4. View Payment Summary");
            System.out.println("5. Payment Reminder Manager");
            System.out.println("6. Update Payment Rates");
            System.out.println("7. log out");

            System.out.print("Please choose an option (1-8): ");

            try {
                paymentOption = Integer.parseInt(scanner.nextLine());
                // Using the modern "->" lambda-like syntax in switch
                switch (paymentOption) {
                    case 1 -> registerPayment();  // Register a new payment
                    case 2 -> viewPaymentsForMember();  // View payment history for the member
                    case 3 -> filterMembersByPaymentStatus();  // Filter members by payment status
                    case 4 -> paymentController.viewPaymentSummary();  // Show payment summary
                    case 5 -> managePaymentReminders();  // Manage payment reminders
                    case 6 -> managePaymentRates();  // Update payment rates
                    case 7 -> {
                        System.out.println("Returning to Main Menu...");  // Exit to main menu
                        return;  // Exit the method and return to the main menu
                    }
                    default -> System.out.println("Invalid option. Please choose a valid number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                paymentOption = -1;  // Ensure loop continues on invalid input
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                paymentOption = -1;  // Ensure loop continues if an exception is caught
            }
        } while (paymentOption != 7);  // Exit loop when option 7 is selected
    }

    /**
     * gives an overview of the current payment rates to the user, and gives the user the possibility of updating the payment rates
     * which will lead to update paymentrates() method.
     */
    private void managePaymentRates() {
        DecimalFormat df = new DecimalFormat("#.##"); // Format to show up to 2 decimal places

        System.out.println("The current payment rates:");
        System.out.println("Yearly junior-membership price: " + df.format(paymentController.getPaymentRates()[0]) + " DKK");
        System.out.println("Yearly senior-membership price: " + df.format(paymentController.getPaymentRates()[1]) + " DKK");

        System.out.println("Do you want to update the payment rates? Type \"Yes\" or \"No\"");
        String choice = scanner.nextLine();
        boolean validInput = false;

        while (!validInput) {
            switch (choice.toLowerCase()) {
                case "yes":
                    updatePaymentRates();
                    validInput = true;
                    break;
                case "no":
                    validInput = true;
                    break;
                default:
                    System.out.println("Invalid input.");
            }
        }
    }

    /**
     * updates the payment rates with userinput.
     */
    private void updatePaymentRates() {
        DecimalFormat df = new DecimalFormat("#.##"); // Format to show up to 2 decimal places

        boolean validInput = false;

        while (!validInput) {
            // prints out junior price.
            System.out.println("(Old yearly junior-membership price: " + df.format(paymentController.getPaymentRates()[0]) + " DKK) " +
                    "Please type in the updated price in DKK: ");
            try {
                double newJuniorPrice = scanner.nextDouble(); // Get the new junior price
                scanner.nextLine(); // Clear the buffer

                // prints out senior price.
                System.out.println("(Old yearly senior-membership price: " + df.format(paymentController.getPaymentRates()[1]) + " DKK) " +
                        "Please type in the updated price in DKK: ");
                double newSeniorPrice = scanner.nextDouble(); // Get the new senior price
                scanner.nextLine(); // Clear the buffer

                // updates the payment rates.
                paymentController.setPaymentRates(newJuniorPrice, newSeniorPrice);

                // print outs the new updated payment rates.
                System.out.println("You have now updated the prices. The updated payment rates:");
                System.out.println("Yearly junior-membership price: " + df.format(paymentController.getPaymentRates()[0]) + " DKK");
                System.out.println("Yearly senior-membership price: " + df.format(paymentController.getPaymentRates()[1]) + " DKK");

                validInput = true; // Exit loop since input is valid

            } catch (InputMismatchException e) {
                // Handle invalid input (values that are not a double)
                System.out.println("Invalid input. Please enter a valid number for the price.");
                scanner.nextLine(); // Clear the buffer to consume the invalid input
            }
        }
    }

    /**
     * Registers a new payment for a member by entering member ID and payment amount.
     */
    private void registerPayment() {
        System.out.print("Enter member ID to register payment: ");
        int memberId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter payment amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        // Call the PaymentController to register the payment
        paymentController.registerPayment(memberId, amount);
    }

    /**
     * Displays all payments made by a specific member.
     */
    private void viewPaymentsForMember() {
        System.out.print("Enter member ID to view payments: ");
        int memberId = Integer.parseInt(scanner.nextLine());

        // Call the PaymentController to view payments for the member
        paymentController.viewPaymentsForMember(memberId);
    }

    /**
     * Filters members based on their payment status and displays the filtered list.
     */
    private void filterMembersByPaymentStatus() {
        System.out.println("Enter payment status (COMPLETE or PENDING): ");
        String statusInput = scanner.nextLine().toUpperCase();
        PaymentStatus paymentStatus = PaymentStatus.valueOf(statusInput);

        List<Member> filteredMembers = paymentController.getMembersByPaymentStatus(paymentStatus);
        System.out.println("Members with payment status " + paymentStatus + ":");
        filteredMembers.forEach(member -> System.out.println("ID: " + member.getMemberId() + ", Name: " + member.getName()));
    }

    /**
     * Manages payment reminders (add, view, delete, clear reminders).
     */
    private void managePaymentReminders() {
        System.out.println("\n--- Payment Reminder Management ---");
        System.out.println("1. Add Payment Reminder");
        System.out.println("2. View All Reminders");
        System.out.println("3. Remove Specific Reminder");
        System.out.println("4. Clear All Reminders");
        System.out.println("5. Exit to Payment Management");

        System.out.print("Please choose an option (1-5): ");
        int reminderOption = Integer.parseInt(scanner.nextLine());

        switch (reminderOption) {
            case 1 -> addPaymentReminder();
            case 2 -> viewAllReminders();
            case 3 -> removePaymentReminder();
            case 4 -> clearAllReminders();
            case 5 -> {
                return;  // Exit to Payment Management
            }
            default -> System.out.println("Invalid option. Please choose a valid number.");
        }
    }

    /**
     * Adds a payment reminder for a specific member.
     */
    private void addPaymentReminder() {
        System.out.print("Enter Member ID to set a reminder: ");
        int memberId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Reminder Message: ");
        String reminderMessage = scanner.nextLine();

        paymentController.setPaymentReminder(memberId, reminderMessage);
        System.out.println("Reminder added successfully for Member ID: " + memberId);
    }

    /**
     * Displays all payment reminders.
     */
    private void viewAllReminders() {
        System.out.println("\n--- All Payment Reminders ---");
        paymentController.viewAllReminders();
    }

    /**
     * Removes a specific reminder for a member.
     */
    private void removePaymentReminder() {
        System.out.print("Enter Member ID for the reminder to remove: ");
        int memberId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter Reminder Message to remove: ");
        String reminderMessage = scanner.nextLine();

        paymentController.removePaymentReminder(memberId, reminderMessage);
        System.out.println("Reminder removed successfully for Member ID: " + memberId);
    }

    /**
     * Clears all payment reminders.
     */
    private void clearAllReminders() {
        System.out.print("Are you sure you want to clear all reminders? (yes/no): ");
        String confirmation = scanner.nextLine().toLowerCase();

        if (confirmation.equals("yes")) {
            paymentController.clearAllReminders();
            System.out.println("All reminders have been cleared.");
        } else {
            System.out.println("Operation cancelled.");
        }
    }

    /**
     * Validates and retrieves a positive numeric age input from the user.
     *
     * @param scanner The Scanner object for reading user input.
     * @param prompt  The prompt message displayed to the user.
     * @return A valid age input as a string.
     */
    private static String correctAgeInput(Scanner scanner, String prompt) {
        String age;
        while (true) {
            try {
                // Display the prompt message to the user
                System.out.print(prompt);
                age = scanner.nextLine().trim();

                // Validate that the input only contains digits
                if (!age.matches("\\d+")) {
                    throw new NumberFormatException("Only numeric values are allowed.");
                }

                // Convert the input to an integer

                // Ensure the age is a positive number
                if (Integer.parseInt(age) > 0) {
                    return age; // Return the valid age
                } else {
                    System.out.println("Invalid age. Age must be positive.");
                }
            } catch (NumberFormatException e) {
                // Handle invalid input and provide feedback to the user
                System.out.println("Invalid input. Please enter numbers");
            }
        }
    }

    /**
     * Validates and retrieves a valid 8-digit phone number input from the user.
     *
     * @param scanner The Scanner object for reading user input.
     * @param prompt  The prompt message displayed to the user.
     * @return A valid phone number as an integer.
     */
// Rule for right phonenumber input
    private static String correctPHInput(Scanner scanner, String prompt) {
        String phoneNumber;
        while (true) {
            try {
                System.out.print(prompt);
                phoneNumber = scanner.nextLine().trim();
                if (!phoneNumber.matches("\\d+")) {
                    throw new NumberFormatException("Only numeric values are allowed.");
                }

                if (Integer.parseInt(phoneNumber) >= 10000000 && Integer.parseInt(phoneNumber) <= 99999999) {
                    return phoneNumber;
                } else {
                    System.out.println("Phonenumber must be exactly 8 digits.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a 8 digits number.");
            }
        }
    }

    private static String correctZipCodeInput(Scanner scanner, String prompt) {
        String zipCode;
        while (true) {
            try {
                System.out.print(prompt);
                zipCode = scanner.nextLine().trim();
                if (!zipCode.matches("\\d+")) {
                    throw new NumberFormatException("Only numeric values are allowed");
                }
                if (Integer.parseInt(zipCode) >= 1000 && Integer.parseInt(zipCode) <= 9990) {
                    return zipCode;
                } else {
                    System.out.println("Zipcode has to be 4 digits.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a 4 digits number.");
            }
        }
    }

    private static String correctEmailInput(Scanner scanner, String prompt) {
        String email;
        while (true) {
            System.out.print(prompt);
            email = scanner.nextLine();

            if (email.contains("@") && email.contains(".")) {
                return email;
            } else {
                System.out.println("Invalid input. Email must contain '@' and '.' - Try again.");
            }
        }
    }


    private void addCompetitionResult() {
        System.out.println("\n--- Add Competition Result ---");

        int memberId;
        while (true) {
            try {
                System.out.print("Enter Member ID: ");
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    throw new IllegalArgumentException("Member ID cannot be empty.");
                }
                memberId = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid Member ID. Please enter a numeric value.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        Member member = memberController.findMemberById(memberId);

        if (member == null) {
            System.out.println("No member found with the given ID.");
            return;
        }

        String eventName;
        while (true) {
            System.out.println("Enter event name (cannot be empty): ");
            eventName = scanner.nextLine().trim();
            try {
                Validator.validateEventName(eventName);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        ActivityType activityType;
        while (true) {
            System.out.println("Enter activity type (Crawl, Butterfly, Breaststroke, Backcrawl): ");
            String activityTypeInput = scanner.nextLine().trim().toUpperCase();
            try {
                activityType = ActivityType.valueOf(activityTypeInput);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid activity type. Please enter one of the valid options.");
            }
        }

        int placement;
        while (true) {
            try {
                System.out.println("Enter placement (must be greater than 0): ");
                placement = Integer.parseInt(scanner.nextLine().trim());
                Validator.validatePlacement(placement);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid placement. Please enter a numeric value.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        double time;
        while (true) {
            try {
                System.out.println("Enter time (in seconds, must be greater than 0): ");
                time = Double.parseDouble(scanner.nextLine().trim());
                Validator.validateTime(time);
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid time. Please enter a numeric value.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("Enter date (DD-MM-YYYY): ");
        LocalDate now = LocalDate.now();
        LocalDate competitionDate;

        while (true) {
            String dateInput = scanner.nextLine().trim();

            try {
                // Parse the input using the DateTimeFormatter
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                competitionDate = LocalDate.parse(dateInput, dateTimeFormatter);

                // Check if the date is not in the future
                if (!competitionDate.isAfter(now)) {
                    break; // Exit the loop if the date is valid and not in the future
                } else {
                    System.out.println("The date cannot be in the future. Please enter a valid date in DD-MM-YYYY format.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format or nonexistent date. Please enter a valid date in DD-MM-YYYY format.");
            }
        }

        MembershipLevel level = member.getAge() < 18 ? MembershipLevel.JUNIOR : MembershipLevel.SENIOR;

        try {
            competitionResultController.addCompetitionResult(member, eventName, placement, time, competitionDate.toString(), level, activityType);
            System.out.println("Competition result added successfully.");
        } catch (Exception e) {
            System.out.println("Error adding competition result: " + e.getMessage());
        }
}



    private void viewAllCompetitionResults() {
        System.out.println("--- All Competition Results ---");

        List<CompetitionResults> results = competitionResultController.getAllResults();
        if (results.isEmpty()) {
            System.out.println("No competition results found.");
        } else {
            for (CompetitionResults result : results) {
                System.out.println(result);
            }
        }
    }

    private void viewMemberCompetitionResults() {
        System.out.println("\n--- View Results for a Member ---");

        System.out.print("Enter Member ID: ");
        int memberId = Integer.parseInt(scanner.nextLine());
        Member member = memberController.findMemberById(memberId);

        if (member == null) {
            System.out.println("No member found with the given ID.");
            return;
        }

        List<CompetitionResults> results = competitionResultController.getResultsByMember(member);
        if (results.isEmpty()) {
            System.out.println("No results found for the member.");
        } else {
            for (CompetitionResults result : results) {
                System.out.println(result);
            }
        }
    }




    private void manageCompetitions() {
        int competitionOption;
        do {
            System.out.println("\n--- Manage Competitions ---");
            System.out.println("1. Add competition result");
            System.out.println("2. View all competition results");
            System.out.println("3. View results for a member");
            System.out.println("4. Back to Main Menu");
            System.out.print("Please choose an option (1-4): ");

            try {
                competitionOption = Integer.parseInt(scanner.nextLine());
                switch (competitionOption) {
                    case 1 -> addCompetitionResult(); // Add competition result
                    case 2 -> viewAllCompetitionResults(); // View all competition results
                    case 3 -> viewMemberCompetitionResults(); // View results for a specific member
                    case 4 -> System.out.println("Returning to Main Menu..."); // Exit submenu
                    default -> System.out.println("Invalid option. Please choose a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 4.");
                competitionOption = -1; // Ensure loop continues on invalid input
            }
        } while (competitionOption != 4); // Exit loop when option 4 is selected
    }

    /**
     * Manages the menu and user interaction for training results.
     * Provides options to add, view, and list training results.
     */
    private void manageTrainingResults() {
        int trainingResultsOption;
        do {
            System.out.println("\n ---Training results---");
            System.out.println("1. Add training results");
            System.out.println("2. View training results for member");
            System.out.println("3. View all training results");
            System.out.println("4. View top 5 results for each discipline");
            System.out.println("5. Back to Main Menu");
            System.out.print("Please choose an option (1-5): ");

            try {
                trainingResultsOption = Integer.parseInt(scanner.nextLine());
                switch (trainingResultsOption){
                    case 1 -> addTrainingResults(); //Add trainingResults to member
                    case 2 -> viewMemberTrainingResults(); // View results for specific member
                    case 3 -> viewAllTrainingResults(); // View every training result
                    case 4 -> viewTop5Results();
                    case 5 -> System.out.println("Returning to Main Menu..."); // Exit submenu
                    default -> System.out.println("Invalid option. Please choose a number between 1 and 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 5.");
                trainingResultsOption = -1;
            }
        } while (trainingResultsOption != 5); // Exit loop when option 4 is selected

    }

    /**
     * Prompts the user to select a discipline and displays the top 5 training results for that discipline.
     * Handles invalid inputs gracefully.
     */
    private void viewTop5Results() {
        int inputOptions = -1;  // Start with an invalid choice
        while (inputOptions < 1 || inputOptions > 4) {  // Loop until valid input
            System.out.println("\n---Results menu---");
            System.out.println("1. Top five results in CRAWL");
            System.out.println("2. Top five results in BACKCRAWL");
            System.out.println("3. Top five results in BREASTSTROKE");
            System.out.println("4. Top five results in BUTTERFLY");
            System.out.print("Please choose an option (1-4): ");

            // Check if input is a valid number
            try {
                inputOptions = Integer.parseInt(scanner.nextLine());
                if (inputOptions < 1 || inputOptions > 4) {
                    System.out.println("Invalid choice. Please choose a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number between 1 and 4.");
            }
        }

        // Handle the selection and display the top 5 results for the chosen discipline
        switch (inputOptions) {
            case 1:
                System.out.println("\n---TOP PERFORMANCES IN CRAWL---");
                System.out.println("SENIOR:");
                trainingResultsController.top5Crawl();
                System.out.println("\nJUNIOR:");
                trainingResultsController.top5CrawlJunior();
                break;
            case 2:
                System.out.println("\n---TOP PERFORMANCES IN BACKCRAWL---");
                System.out.println("SENIOR:");
                trainingResultsController.top5BackCrawl();
                System.out.println("\nJUNIOR:");
                trainingResultsController.top5BackcrawlJunior();
                break;
            case 3:
                System.out.println("\n---TOP PERFORMANCES IN BREASTSTROKE---");
                System.out.println("SENIOR:");
                trainingResultsController.top5Breaststroke();
                System.out.println("\nJUNIOR:");
                trainingResultsController.top5BreaststrokeJunior();
                break;
            case 4:
                System.out.println("\n---TOP PERFORMANCES IN BUTTERFLY---");
                System.out.println("SENIOR:");
                trainingResultsController.top5Butterfly();
                System.out.println("\nJUNIOR:");
                trainingResultsController.top5ButterflyJunior();
                break;
            default:
                break;
        }

    }

    /**
     * Prompts the user to enter a member ID and displays the training results for that specific member.
     *
     * @throws NumberFormatException If the input for member ID is not a valid number.
     */
    private void viewMemberTrainingResults() {
        System.out.println("---View Members training results---");
        System.out.print("Enter memberID:");
        int memberid = Integer.parseInt(scanner.nextLine());
        Member member = memberController.findMemberById(memberid);

        if (member == null) {
            System.out.println("No member found with the given ID.");
            return;
        }
        List<TrainingResults> results = trainingResultsController.getResultsByMember(member);
        if (results.isEmpty()) {
            System.out.println("No results found for the member.");
        } else {
            for (TrainingResults result : results) {
                System.out.println(result);
            }
        }


    }

    /**
     * Allows the user to add training results for a specific member.
     *
     * @throws IllegalArgumentException If the input data is invalid or incomplete.
     */
    private void addTrainingResults() {
        System.out.println("\n---Add training results---");
        System.out.print("Enter memberId:");
        int memberId = Integer.parseInt(scanner.nextLine());
        Member member = memberController.findMemberById(memberId);

        MembershipLevel level = null;

        if (member == null) {
            System.out.println("No member found wtih given ID.");
            return;
        }

        System.out.print("Enter discipline (Breaststroke, Crawl, Backcrawl or Butterfly):");
        String activityType = scanner.nextLine().trim();

        System.out.print("Enter time:");
        double time = scanner.nextDouble();

        // Consume next line to prevent issues with scanner
        scanner.nextLine();
        System.out.println("Enter date (DD-MM-YYYY): ");
        LocalDate now = LocalDate.now();
        LocalDate trainingDate;

        while (true) {
            String dateInput = scanner.nextLine().trim();

            try {
                // Parse the input using the DateTimeFormatter
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                trainingDate = LocalDate.parse(dateInput, dateTimeFormatter);

                // Check if the date is not in the future
                if (!trainingDate.isAfter(now)) {
                    break; // Exit the loop if the date is valid and not in the future
                } else {
                    System.out.println("The date cannot be in the future. Please enter a valid date in DD-MM-YYYY format.");
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format or nonexistent date. Please enter a valid date in DD-MM-YYYY format.");
            }
        }


        try {
            trainingResultsController.addTrainingResults(member, activityType, time, trainingDate.toString(), level);
            System.out.println("Training results successfully added.");
        } catch (Exception e) {
            System.out.println("Error adding training result. " + e.getMessage());
        }
    }

    /**
     * Displays all training results in the system.
     */
    public void viewAllTrainingResults() {
        List<TrainingResults> results = trainingResultsController.getAllResults();

        if (results.isEmpty()) {
            System.out.println("No training results found.");
        } else {
            for (TrainingResults result : results) {
                System.out.println(result);
            }
        }
    }
}
