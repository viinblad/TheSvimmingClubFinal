package swimclub.services;

import swimclub.models.Coach;
import swimclub.models.Member;
import swimclub.repositories.StaffRepository;
import swimclub.utilities.Validator;

public class StaffService {
    private final StaffRepository staffRepository;

    public StaffService (StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }


    public void registerCoach(Coach coach) {
        // Assign the next available member ID automatically
        int nextCoachId = staffRepository.getNextCoachId();
        coach.setCoachId(nextCoachId); // Set the next available member ID


    }

    public void deleteCoach(int coachId) {
        Coach coach = staffRepository.findCoachById(coachId);

        if (coach == null) {
            // Handle member not found
            System.out.println("Coach not found.");
            return;
        }
        staffRepository.deleteCoach(coach);
        System.out.println("Coach deleted successfully.");
    }
}
