package swimclub.ui;

import swimclub.controllers.MemberController;
import swimclub.controllers.PaymentController;
import swimclub.models.Member;
import swimclub.models.MembershipStatus;
import swimclub.models.PaymentStatus;

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
     * @param memberController The controller that handles the logic for member actions.
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
        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter member email: ");
        String email = scanner.nextLine();
        System.out.println("Enter city of member");
        String city = scanner.nextLine();
        System.out.println("Enter street of member");
        String street = scanner.nextLine();
        System.out.println("Enter region of member");
        String region = scanner.nextLine();
        System.out.print("Enter Zip code: ");
        int zipcode = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter membership type (Junior/Senior, Competitive/Exercise): ");
        String membershipType = scanner.nextLine();
        System.out.print("Enter activity type (Breaststroke, Crawl, Backcrawl or Butterfly):");
        String activityType = scanner.nextLine();
        System.out.print("Enter phone number (8 digits): ");
        int phoneNumber = Integer.parseInt(scanner.nextLine());

        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;  // Default membership status
        PaymentStatus paymentStatus = PaymentStatus.PENDING;  // Default payment status

        // Call the controller to register the new member
        Member newMember = memberController.registerMember(name, email, city, street, region, zipcode, membershipType, membershipStatus, activityType, paymentStatus, age, phoneNumber);

        if (newMember != null) {
            // Calculate the membership fee
            double fee = paymentController.calculateMembershipFeeForMember(newMember.getMemberId());

            // Prompt user to pay now or later
            System.out.println("\nMembership fee for " + newMember.getName() + " is $" + fee);
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
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new city: ");
        String city = scanner.nextLine();
        System.out.print("Enter new street: ");
        String street = scanner.nextLine();
        System.out.print("Enter new region: ");
        String region = scanner.nextLine();
        System.out.print("Enter new zip code: ");
        int zipcode = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new membership type (Junior/Senior, Competitive/Exercise): ");
        String membershipType = scanner.nextLine();
        System.out.println("Enter new activity type (Crawl, Backcrawl, Breathstroke or Butterfly):");
        String activitytype = scanner.nextLine();
        System.out.print("Enter new phone number (8 digits): ");
        int phoneNumber = Integer.parseInt(scanner.nextLine());

        MembershipStatus membershipStatus = MembershipStatus.ACTIVE;  // Default to ACTIVE
        PaymentStatus paymentStatus = PaymentStatus.PENDING;  // Default to PENDING

        // Call the controller's updateMember method with all new attributes
        memberController.updateMember(memberId, name, email, age, city, street, region, zipcode, membershipType, membershipStatus, activitytype, paymentStatus, phoneNumber);
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
        System.out.println("4. View Payment Summary");  // Added Payment Summary option
        System.out.println("5. Exit to Main Menu");

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

    private void viewPaymentSummary() {
        System.out.println("Payment Summary: ");
        List<Member> paidMembers = paymentController.getMembersPaidList();
        double totalPayments = paidMembers.stream()
                .mapToDouble(member -> paymentController.calculateMembershipFeeForMember(member.getMemberId()))
                .sum();
        System.out.println("Total Payments Made: " + totalPayments);
        System.out.println("Number of Members Paid: " + paidMembers.size());
    }

    private void filterMembersByPaymentStatus() {
        System.out.println("Enter payment status (COMPLETE or PENDING): ");
        String statusInput = scanner.nextLine().toUpperCase();
        PaymentStatus paymentStatus = PaymentStatus.valueOf(statusInput);

        List<Member> filteredMembers = paymentController.getMembersByPaymentStatus(paymentStatus);
        System.out.println("Members with payment status " + paymentStatus + ":");
        filteredMembers.forEach(member -> System.out.println("ID: " + member.getMemberId() + ", Name: " + member.getName()));
    }
}
