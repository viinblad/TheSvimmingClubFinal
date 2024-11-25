package swimclub.ui;

import swimclub.controllers.MemberController;

import java.util.Scanner;

public class UserInterface {
    private final MemberController memberController;
    private final Scanner scanner;

    public UserInterface(MemberController memberController) {
        this.memberController = memberController;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        int option;
        do {
            printMenu();
            option = getUserInput();
            handleOption(option);
        } while (option != 5); // Exit when the user selects option 4
    }

    private void printMenu() {
        System.out.println("\n--- Swim Club Member Management ---");
        System.out.println("1. Register New Member");
        System.out.println("2. Update Member");
        System.out.println("3. View All Members");
        System.out.println("4. Delete Member");
        System.out.println("5. Exit");
        System.out.print("Please choose an option (1-4): ");
    }

    private int getUserInput() {
        int option = -1;
        try {
            option = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number between 1 and 4.");
        }
        return option;
    }

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
                deleteMember();
                break;
            case 5:
                System.out.println("Exiting the program. Goodbye!");
                break;
            default:
                System.out.println("Invalid option. Please choose a number between 1 and 4.");
        }
    }

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

        memberController.registerMember(name, email, membershipType, age, phoneNumber);
    }

    private void updateMember() {
        System.out.print("Enter member ID to update: ");
        int memberId = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new email: ");
        String email = scanner.nextLine();
        System.out.print("Enter new age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter new phone number (8 digits): ");
        int phoneNumber = Integer.parseInt(scanner.nextLine());

        memberController.updateMember(memberId, name, email, age, phoneNumber);
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
}
