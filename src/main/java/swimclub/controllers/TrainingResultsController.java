package swimclub.controllers;

import swimclub.models.*;
import swimclub.services.TrainingResultsService;

import java.util.List;

/**
 * Controller class responsible for managing training results.
 */
public class TrainingResultsController {

    private final TrainingResultsService trainingService;

    // === CONSTRUCTOR ===
    /**
     * Constructor to initialize the TrainingResultsController with the necessary service.
     *
     * @param trainingService The service responsible for managing training results.
     */
    public TrainingResultsController(TrainingResultsService trainingService) {
        this.trainingService = trainingService;
    }

    // === ADD TRAINING RESULTS ===
    /**
     * Adds the training results for a member.
     *
     * @param member The member for whom the results are being added.
     * @param activityType The type of activity the member completed.
     * @param length The length of the activity (in meters).
     * @param time The time it took for the activity (in seconds).
     * @param date The date the activity took place.
     * @param level The membership level of the member (Junior or Senior).
     */
    public void addTrainingResults(Member member, String activityType, int length, double time, String date, MembershipLevel level) {
        ActivityTypeData activity = ActivityTypeData.fromString(activityType);

        // Assign membership level based on age if not provided
        if (member.getAge() < 18) {
            level = MembershipLevel.JUNIOR;  // Automatically set to JUNIOR if age < 18
        } else if (member.getAge() > 18) {
            level = MembershipLevel.SENIOR;  // Automatically set to SENIOR if age > 18
        }

        // Add the training result using the service
        trainingService.addResult(member, activity.toActivityType(), length, time, date, level);
    }

    // === GET RESULTS BY MEMBER ===
    /**
     * Retrieves the training results for a specific member.
     *
     * @param member The member whose results are being fetched.
     * @return A list of training results for the given member.
     */
    public List<TrainingResults> getResultsByMember(Member member) {
        return trainingService.getResultsByMember(member); // Delegate to the service to retrieve results
    }

    // === GET ALL TRAINING RESULTS ===
    /**
     * Retrieves all training results for all members.
     *
     * @return A list of all training results.
     */
    public List<TrainingResults> getAllResults() {
        return trainingService.getAllResults(); // Delegate to the service to retrieve all results
    }
}