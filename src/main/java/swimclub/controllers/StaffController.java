package swimclub.controllers;

import swimclub.models.Role;
import swimclub.models.Coach;
import swimclub.repositories.StaffRepository;
import swimclub.services.StaffService;

import java.util.List;

public class StaffController {

    private final StaffRepository staffRepository;
    private final StaffService staffService;

    // Constructor to initialize the StaffController with StaffService and StaffRepository
    public StaffController(StaffService staffService, StaffRepository staffRepository) {
        this.staffService = staffService;
        this.staffRepository = staffRepository;
    }

    // Method to find a coach by their ID
    public Coach findCoachById(int coachId) {
        return staffRepository.findCoachById(coachId); // Use repository to find the coach by ID
    }

    /**
     * Registers a new coach and assigns them to a team.
     *
     * @param teamName   The name of the team the coach is assigned to.
     * @param name       The name of the coach.
     * @param email      The email of the coach.
     * @param city       The city where the coach resides.
     * @param street     The street address of the coach.
     * @param region     The region of the coach.
     * @param zipcode    The zipcode of the coach.
     * @param age        The age of the coach.
     * @param phoneNumber The phone number of the coach.
     * @param role       The role of the coach (typically COACH).
     * @return The newly registered coach.
     */
    public Coach registerCoach(String teamName, String name, String email, String city, String street, String region,
                               int zipcode, int age, int phoneNumber, Role role) {

        // Get the next coach ID before creating the new coach object
        int coachId = staffRepository.getNextCoachId();

        // Create a new Coach object with the necessary details
        Coach newCoach = new Coach(coachId, teamName, name, email, city, street, region, zipcode, age, phoneNumber, role);

        // Add the new coach to the staff repository
        staffRepository.addCoach(newCoach);

        // Return the newly created coach object
        return newCoach;
    }

    // Method to find a coach by their team name
    public Coach findCoachByTeamName(String teamName) {
        return staffRepository.findCoachByTeamName(teamName);
    }

    // Method to get a list of all coaches and display their details
    public boolean getCoachList() {
        List<Coach> coachList = staffRepository.getCoachList();

        if (coachList.isEmpty()) {
            return false;
        } else {
            System.out.println("List of all Coaches:");
            for (Coach coach : coachList) {
                System.out.println("Coach Name: " + coach.getName() +
                        ", Coach Id: " + coach.getCoachId() +
                        ", Team: " + coach.getTeamName() +
                        ", Role: " + coach.getRole() +
                        ", Age: " + coach.getAge() +
                        ", Phone Number: " + coach.getPhoneNumber());
            }
            return true;
        }
    }
}
