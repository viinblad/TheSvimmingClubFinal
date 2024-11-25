package swimclub.ui;

import swimclub.controllers.MemberController;
import swimclub.models.Member;

import java.util.List;
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
        } while (option != 5); // Exit when the user selects option 4
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
        System.out.println("6. Exit");
        System.out.print("Please choose an option (1-6): ");
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
                searchMembers();
                break;
            case 3:
                updateMember();
                break;
            case 4:
                memberController.viewAllMembers();
                break;
            case 5:
                deleteMember();
                break;
            case 6:
                System.out.println("Exiting the program. Goodbye!");
                break;
            default:
                System.out.println("Invalid option. Please choose a number between 1 and 6.");
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
        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter phone number (8 digits): ");
        int phoneNumber = Integer.parseInt(scanner.nextLine());

        // Calls the controller to register the new member
        memberController.registerMember(name, email, city, street, region, zipcode, membershipType, age, phoneNumber);
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
        System.out.print("Enter new phone number (8 digits): ");
        int phoneNumber = Integer.parseInt(scanner.nextLine());

        // Call the controller's updateMember method with all new attributes
        memberController.updateMember(memberId, name, email, age, city, street, region, zipcode, membershipType, phoneNumber);
    }

    private void deleteMember() {
        System.out.println("Enter Members ID:");
        int memberId = Integer.parseInt(scanner.nextLine());
        boolean success = memberController.deleteMember(memberId);
        try {
            if (success) {
                System.out.println("Member succesfully deleted.");
            } else {
                System.out.println("Member not found, please check the ID and try again.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input please enter a valid numeric ID.");
        }
    }

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
}
