package swimclub.ui;

import swimclub.controllers.MemberController;

import java.util.Scanner;

/**
 * UserInterface handles the interaction between the user and the program.
 * It allows the user to register, update, and view members.
 */
public class UserInterface {
    private final MemberController memberController; // Controller to handle member actions
    private final Scanner scanner; // Scanner to read user input

    /**
     * Constructor to initialize the UserInterface with the member controller.
     *
     * @param memberController The controller that handles the logic for member actions.
     */
    public UserInterface(MemberController memberController) {
        this.memberController = memberController;
        this.scanner = new Scanner(System.in);
    }

    /**
     * Starts the user interface, displaying the menu and handling user input.
     */
    public void start() {
        int option;
        do {
            printMenu();
            option = getUserInput();
            handleOption(option);
        } while (option != 4); // Exit when the user selects option 4
    }

    /**
     * Prints the main menu of the user interface.
     */
    private void printMenu() {
        System.out.println("\n--- Swim Club Member Management ---");
        System.out.println("1. Register New Member");
        System.out.println("2. Update Member");
        System.out.println("3. View All Members");
        System.out.println("4. Exit");
        System.out.print("Please choose an option (1-4): ");
    }

    /**
     * Reads user input to select an option from the menu.
     *
     * @return The selected option as an integer.
     */
    private int getUserInput() {
        int option = -1;
        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 4.");
        }
        return option;
    }

    /**
     * Handles the user's selected menu option.
     * Based on the option, it either registers, updates, or views members.
     *
     * @param option The selected option from the menu (1-4).
     */
    private void handleOption(int option) {
        switch (option) {
            case 1:
                registerMember();
                break;
            case 2:
                updateMember();
                break;
            case 3:
                memberController.viewAllMembers();
                break;
            case 4:
                System.out.println("Exiting the program. Goodbye!");
                break;
            default:
                System.out.println("Invalid option. Please choose a number between 1 and 4.");
        }
    }

    /**
     * Registers a new member by collecting their details and passing them to the controller.
     */
    private void registerMember() {
        System.out.print("Enter member name: ");
        String name = scanner.nextLine();
        System.out.print("Enter member email: ");
        String email = scanner.nextLine();
        System.out.print("Enter membership type (Junior/Senior, Competitive/Exercise): ");
        String membershipType = scanner.nextLine();
        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter phone number (8 digits): ");
        int phoneNumber = Integer.parseInt(scanner.nextLine());

        // Calls the controller to register the new member
        memberController.registerMember(name, email, membershipType, age, phoneNumber);
    }

    /**
     * Updates an existing member's information based on the provided member ID and new details.
     */
    private void updateMember() {
        System.out.print("Enter member ID to update: ");
        int memberId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new membership type (Junior/Senior, Competitive/Exercise): ");
        String membershipType = scanner.nextLine();  // New input for membership type
        System.out.print("Enter new phone number (8 digits): ");
        int phoneNumber = Integer.parseInt(scanner.nextLine());

        // Calls the controller to update the member with the new details
        memberController.updateMember(memberId, name, email, age, membershipType, phoneNumber);
    }
}
