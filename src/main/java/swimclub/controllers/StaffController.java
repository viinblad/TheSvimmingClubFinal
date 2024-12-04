package swimclub.controllers;

import swimclub.models.MembershipStatus;
import swimclub.models.PaymentStatus;
import swimclub.repositories.StaffRepository;
import swimclub.models.Coach;
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

    public Coach registerCoach(String teamName, String name, String email, String city, String street, String region,
                               int zipcode, int age, int phoneNumber) {
        Coach newCoach = null;
        staffRepository.addCoach(newCoach);

        newCoach = new Coach(staffRepository.getNextCoachId(), teamName, name, email, city, street, region,
                zipcode, age, phoneNumber);


        return newCoach;
    }

    public Coach findCoachByTeamName(String teamName) {
        return staffRepository.findCoachByTeamName(teamName);
    }

    public boolean getCoachList() {
        List<Coach> coachList = staffRepository.getCoachList();

        if (coachList.isEmpty()) {
            return false;
        } else {
            System.out.println("List of all Coaches:");
            for (Coach coach : coachList) {
                System.out.println("Coach Name: " + coach.getName() +
                        ", Team: " + coach.getTeamName() +
                        ", Age: " + coach.getAge() +
                        ", Phone Number: " + coach.getPhoneNumber());
            }
            return true;
        }
    }
}