package swimclub.services;

import swimclub.models.Coach;
import swimclub.models.Member;
import swimclub.repositories.StaffRepository;
import swimclub.utilities.Validator;

public class StaffService {
    // Repository for accessing and managing staff data
    private final StaffRepository staffRepository;

    /**
     * Constructor to initialize the StaffService with the provided StaffRepository.
     *
     * @param staffRepository The repository used to interact with staff data (coaches)
     */
    public StaffService(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    /**
     * Registers a new coach by assigning them the next available coach ID.
     *
     * @param coach The coach object to be registered.
     * This method automatically assigns the next available coach ID
     * and sets it in the coach object.
     */
    public void registerCoach(Coach coach) {
        // Get the next available coach ID from the repository
        int nextCoachId = staffRepository.getNextCoachId();
        coach.setCoachId(nextCoachId); // Set the next available coach ID
        // Note: The coach is not saved in the repository here (could add a save call if necessary)
    }

    /**
     * Deletes an existing coach by their ID.
     * If the coach is found in the repository, they are deleted.
     * If not, an error message is displayed.
     *
     * @param coachId The ID of the coach to be deleted.
     */
    public void deleteCoach(int coachId) {
        // Find the coach in the repository by their ID
        Coach coach = staffRepository.findCoachById(coachId);

        // Check if the coach exists, and handle the case where the coach is not found
        if (coach == null) {
            // Print a message indicating that the coach was not found
            System.out.println("Coach not found.");
            return;
        }

        // Delete the coach from the repository
        staffRepository.deleteCoach(coach);
        // Print a confirmation message after successful deletion
        System.out.println("Coach deleted successfully.");
    }
}
