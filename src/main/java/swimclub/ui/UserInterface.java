package swimclub.ui;

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
    public UserInterface(MemberController memberController, PaymentController paymentController, TeamController teamController, CompetitionResultController competitionResultController, TrainingResultsController trainingResultsController) {
        this.memberController = memberController;
        this.paymentController = paymentController;
        this.teamController = teamController;
        this.competitionResultController = competitionResultController;
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
            case 8 :
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
            System.out.println("4. Assign Team Leader");
            System.out.println("5. View Teams");
            System.out.println("6. Delete Team");
            System.out.println("7. Exit to Main Menu");

            System.out.print("Please choose an option (1-7): ");
            try {
                teamOption = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                continue;
            }

            switch (teamOption) {
                case 1 -> createTeam();
                case 2 -> addMemberToTeam();
                case 3 -> removeMemberFromTeam();
                case 4 -> assignTeamLeader();
                case 5 -> viewTeams();
                case 6 -> deleteTeam();
                case 7 -> System.out.println("Returning to Main Menu...");
                default -> System.out.println("Invalid option. Please choose a valid number.");
            }
        } while (teamOption != 7);
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
            teamController.createTeam(teamName, teamTypeStr);
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
     * Assigns a team leader to a specific team.
     */
    private void assignTeamLeader() {
        System.out.print("Enter Team Name: ");
        String teamName = scanner.nextLine().trim();

        System.out.print("Enter Member ID to Assign as Leader: ");
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
            teamController.assignTeamLeader(teamName, member);
            System.out.println("Member '" + member.getName() + "' assigned as leader of team '" + teamName + "'.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error assigning team leader: " + e.getMessage());
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
        if(results.isEmpty()) {
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

    private void manageTrainingResults(){
        int trainingResultsOption;
        do {
            System.out.println("\n ---Training results---");
            System.out.println("1. Add training results");
            System.out.println("2. View training results for member");
            System.out.println("3. View all training results");
            System.out.println("4. Back to Main Menu");
            System.out.print("Please choose an option (1-3): ");

            try {
                trainingResultsOption = Integer.parseInt(scanner.nextLine());
                switch (trainingResultsOption){
                    case 1 -> addTrainingResults(); //Add trainingResults to member
                    case 2 -> viewMemberTrainingResults(); // View results for specific member
                    case 3 -> viewAllTrainingResults(); // View every training result
                    case 4 -> System.out.println("Returning to Main Menu..."); // Exit submenu
                    default -> System.out.println("Invalid option. Please choose a number between 1 and 4.");
                }
            } catch (NumberFormatException e){
                System.out.println("Invalid input. Please enter a number between 1 and 3.");
                trainingResultsOption = -1;
            }
        } while (trainingResultsOption != 3); // Exit loop when option 4 is selected

    }

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
        if (results.isEmpty()){
            System.out.println("No results found for the member.");
        } else {
            for (TrainingResults result : results){
                System.out.println(result);
            }
        }


    }

    private void addTrainingResults(){
        System.out.println("\n---Add training results---");
        System.out.print("Enter memberId:");
        int memberId = Integer.parseInt(scanner.nextLine());
        Member member = memberController.findMemberById(memberId);

        MembershipLevel level = null;

        if(member == null){
            System.out.println("No member found wtih given ID.");
            return;
        }

        System.out.print("Enter discipline (Breaststroke, Crawl, Backcrawl or Butterfly):");
        String activityType = scanner.nextLine();

        System.out.print("Enter the total length swum during this training session (In meters):");
        int length = scanner.nextInt();
        //Consume line
        scanner.nextLine();

        System.out.print("Enter time:");
        double time = scanner.nextDouble();

        //Consume next line
        scanner.nextLine();

        System.out.print("Enter date of training (dd-MM-yyyy):");
        String date = scanner.nextLine();

        try {
            trainingResultsController.addTrainingResults(member, activityType, length, time, date, level);
            System.out.println("Training results succesfully added.");
        } catch (Exception e) {
            System.out.println("Error adding training result." + e.getMessage());
        }
    }

    public void viewAllTrainingResults(){
        List<TrainingResults> results = trainingResultsController.getAllResults();

        if (results.isEmpty()){
            System.out.println("No training results found.");
        } else {
            for (TrainingResults result : results){
                System.out.println(result);
            }
        }
    }
}
