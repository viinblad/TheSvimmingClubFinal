package swimclub.ui;

import swimclub.controllers.MemberController;
import swimclub.controllers.PaymentController;
import swimclub.models.*;

import java.util.List;
import java.util.Scanner;

/**
 * UserInterface handles the interaction between the user and the program.
 * It allows the user to register, update, and view members, as well as manage payments.
 */
public class UserInterface {
    private final MemberController memberController;  // Controller to handle member actions
    private final PaymentController paymentController;  // Controller to handle payment actions
    private final Scanner scanner; // Scanner to read user input

    /**
     * Constructor to initialize the UserInterface with the member controller and payment controller.
     *
     * @param memberController  The controller that handles the logic for member actions.
     * @param paymentController The controller that handles the logic for payment actions.
     */
    public UserInterface(MemberController memberController, PaymentController paymentController) {
        this.memberController = memberController;
        this.paymentController = paymentController;  // Initialize PaymentController
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
        } while (option != 7); // Exit when the user selects option 7
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
        System.out.println("6. Payment Management");  // New menu option for payment handling
        System.out.println("7. Exit");
        System.out.print("Please choose an option (1-7): ");
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
            System.out.println("Invalid input. Please enter a number between 1 and 7.");
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
        String zipcode = correctZipCodeInput(scanner,"Enter Zip code (4 digits):");
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
        String email = correctEmailInput(scanner,"Enter new Email:");
        String age = correctAgeInput(scanner, "Enter new age:");
        System.out.print("Enter new city: ");
        String city = scanner.nextLine();
        System.out.print("Enter new street: ");
        String street = scanner.nextLine();
        System.out.print("Enter new region: ");
        String region = scanner.nextLine();
        String zipcode = correctZipCodeInput(scanner,"Enter new zipcode");
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
        System.out.println("6. Exit to Main Menu");

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
                return;  // Exit to main menu
            default:
                System.out.println("Invalid option. Please choose a valid number.");
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
    private static String correctEmailInput(Scanner scanner, String prompt){
        String email;
        while (true){
            System.out.print(prompt);
            email = scanner.nextLine();

            if (email.contains("@") && email.contains(".")){
                return email;
            } else {
                System.out.println("Invalid input. Email must contain '@' and '.' - Try again.");
            }
        }
    }
}
