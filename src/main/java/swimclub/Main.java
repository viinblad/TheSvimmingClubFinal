package swimclub;

import swimclub.controllers.MemberController;
import swimclub.repositories.MemberRepository;
import swimclub.services.MemberService;
import swimclub.utilities.FileHandler;
import swimclub.ui.UserInterface;

public class Main {
    public static void main(String[] args) {
        // Initialize the FileHandler, MemberRepository, and MemberService
        String filePath = "src/main/resources/members.txt"; // Adjust the file path as needed
        FileHandler fileHandler = new FileHandler(filePath);
        MemberRepository memberRepository = new MemberRepository(fileHandler);
        MemberService memberService = new MemberService(memberRepository);

        // Instantiate the MemberController
        MemberController memberController = new MemberController(memberService, memberRepository);

        // Instantiate the UserInterface and pass in the MemberController
        UserInterface userInterface = new UserInterface(memberController);

        // Start the User Interface
        userInterface.start();
    }
}
