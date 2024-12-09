package swimclub.ui;

import swimclub.controllers.CompetitionResultController;
import swimclub.controllers.MemberController;
import swimclub.controllers.PaymentController;
import swimclub.controllers.StaffController;
import swimclub.controllers.TeamController;
import swimclub.controllers.*;
import swimclub.models.*;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
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
    private final Scanner scanner; // Scanner to read user input


    /**
     * Constructor to initialize the UserInterface with the member controller and payment controller.
     *
     * @param memberController  The controller that handles the logic for member actions.
     * @param paymentController The controller that handles the logic for payment actions.
     * @param teamController    The controller that handles the logic for team actions.
     */
    public UserInterface(MemberController memberController, PaymentController paymentController, TeamController teamController, CompetitionResultController competitionResultController, StaffController staffController, TrainingResultsController trainingResultsController) {

        this.memberController = memberController;
        this.paymentController = paymentController;
        this.teamController = teamController;
        this.competitionResultController = competitionResultController;
        this.staffController = staffController;
        this.trainingResultsController = trainingResultsController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the user interface, displaying the menu and handling user input.
     */
    public void start() {
        int option;
        do {
            printMenu();  // Display the main menu
            option = getUserInput();  // Get user's input option
            handleOption(option);  // Handle the option selected by the user
        } while (option != 9); // Exit when the user selects option 8
    }

    /**
     * Prints the main menu of the user interface.
     */
    private void printMenu() {
        System.out.println("\n--- Swim Club Member Management ---");
        System.out.println("1. Register New Member");
        System.out.println("2. Search Members");
        System.out.println("3. Update Member");
        System.out.println("4. View All Members");
        System.out.println("5. Delete Member");
        System.out.println("6. Payment Management");
        System.out.println("7. Team Management");
        System.out.println("8. Competition Management");
        System.out.println("9. Register training results");
        System.out.println("10. Exit");
        System.out.print("Please choose an option (1-10): ");
    }

    /**
     * Reads user input to select an option from the menu.
     *
     * @return The selected option as an integer.
     */
    private int getUserInput() {
        int option = -1;
        try {
            option = Integer.parseInt(scanner.nextLine());  // Parse the input as integer
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 10.");
        }
        return option;
    }

    /**
     * Handles the user's selected menu option.
     * Based on the option, it either registers, updates, views members or handles payments.
     *
     * @param option The selected option from the menu (1-7).
     */
    private void handleOption(int option) {

        switch (option) {
            case 1:
                registerMember();  // Register a new member
                break;
            case 2:
                searchMembers();  // Search members
                break;
            case 3:
                updateMember();  // Update member details
                break;
            case 4:
                memberController.viewAllMembers();  // View all members
                break;
            case 5:
                deleteMember();  // Delete a member
                break;
            case 6:
                handlePayments();  // Handle payments (new option)
                break;
            case 7:
                manageTeams();  // Handle payments (new option)
                break;
            case 8:
                manageCompetitions();
                break;
            case 9:
                manageTrainingResults();
                break;
            case 10:
                exitProgram(); // Exit the program
                break;
            default:
                System.out.println("Invalid option. Please choose a number between 1 and 7.");
        }
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
     * Manages team-related operations: creating, viewing, updating, and deleting teams.
     */
    private void manageTeams() {
        int teamOption = -1;
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
            System.out.println("9. Exit to Main Menu");

            System.out.print("Please choose an option (1-9): ");
            try {
                teamOption = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 9.");
                continue;
            }

            switch (teamOption) {
                case 1 -> createTeam();
                case 2 -> addMemberToTeam();
                case 3 -> removeMemberFromTeam();
                case 4 -> assignTeamCoach();
                case 5 -> removeTeamCoach();
                case 6 -> registerCoach();
                case 7 -> viewTeams();
                case 8 -> deleteTeam();
                case 9 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid option. Please choose a valid number.");
            }
        } while (teamOption != 9);
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

        // Call the controller to register the new coach
        Coach newCoach = staffController.registerCoach(null, name, email, city, street, region, zipcode, age, phoneNumber);

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
                    teamController.assignTeamCoach(teamName, newCoach); // Assigning the new coach to the team
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
        System.out.print("Enter Team Name: ");
        String teamName = scanner.nextLine().trim();

        System.out.print("Enter Member ID to Add: ");
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
            teamController.addMemberToTeam(teamName, member);
            System.out.println("Member '" + member.getName() + "' added to team '" + teamName + "'.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error adding member to team: " + e.getMessage());
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
            coach.setTeamName(teamName); // assigning team name to coach.
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
     * Handles payment-related operations: Registering payments and viewing payments.
     */
    private void handlePayments() {
        System.out.println("\n--- Payment Management ---");
        System.out.println("1. Register Payment");
        System.out.println("2. View Payments for Member");
        System.out.println("3. Filter Members by Payment Status");
        System.out.println("4. View Payment Summary");
        System.out.println("5. Payment reminder manager");
        System.out.println("6. Update Payment Rates");
        System.out.println("7. Exit to Main Menu");

        System.out.print("Please choose an option (1-5): ");
        int paymentOption = Integer.parseInt(scanner.nextLine());

        switch (paymentOption) {
            case 1:
                registerPayment();  // Register a new payment

                break;
            case 2:
                viewPaymentsForMember();  // View payment history for the member
                break;
            case 3:
                filterMembersByPaymentStatus();  // Filter members by payment status
                break;
            case 4:
                paymentController.viewPaymentSummary(); // Show payment summary
                break;
            case 5:
                managePaymentReminders();
                break;
            case 6:
                managePaymentRates();
            case 7:
                return;  // Exit to main menu
            default:
                System.out.println("Invalid option. Please choose a valid number.");
        }
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

        System.out.print("Enter Member ID: ");
        int memberId = Integer.parseInt(scanner.nextLine());
        Member member = memberController.findMemberById(memberId);

        if (member == null) {
            System.out.println("No member found with the given ID.");
            return;
        }

        System.out.println("Enter event name: ");
        String eventName = scanner.nextLine();

        System.out.println("Enter placement: ");
        int placement = Integer.parseInt(scanner.nextLine());

        System.out.println("Enter time (in seconds): ");
        double time = Double.parseDouble(scanner.nextLine());

        try {
            competitionResultController.addResult(member, eventName, placement, time);
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
            System.out.print("Please choose an option (1-3): ");

            try {
                trainingResultsOption = Integer.parseInt(scanner.nextLine());
                switch (trainingResultsOption) {
                    case 1 -> addTrainingResults(); // Add training results to a member's record
                    case 2 -> viewMemberTrainingResults(); // View specific member's results
                    case 3 -> viewAllTrainingResults(); // View all training results
                    case 4 -> viewTop5Results(); // View top 5 results per discipline
                    case 5 -> System.out.println("Returning to Main Menu..."); // Exit submenu
                    default -> System.out.println("Invalid option. Please choose a number between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                trainingResultsOption = -1;
            }
        } while (trainingResultsOption != 5); // Exit loop when option 5 is selected
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
            System.out.println("No member found with given ID.");
            return;
        }

        System.out.print("Enter discipline (Breaststroke, Crawl, Backcrawl or Butterfly):");
        String activityType = scanner.nextLine().trim();

        System.out.print("Enter time:");
        double time = scanner.nextDouble();

        // Consume next line to prevent issues with scanner
        scanner.nextLine();

        System.out.print("Enter date of training (dd-MM-yyyy):");
        String date = scanner.nextLine().trim();

        try {
            trainingResultsController.addTrainingResults(member, activityType, time, date, level);
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
