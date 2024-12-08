package swimclub.services;

import swimclub.models.ActivityType;
import swimclub.models.CompetitionResults;
import swimclub.models.Member;
import swimclub.models.MembershipLevel;
import swimclub.repositories.CompetitionResultRepository;
import swimclub.utilities.Validator;

import java.util.List;

/**
 * Service class for managing competition results for swim club members.
 * Provides methods to add competition results and retrieve results for a specific member or all members.
 */
public class CompetitionResultService {
    private final CompetitionResultRepository resultRepository;  // Repository for storing and managing competition results

    /**
     * Constructor to initialize the service with the competition result repository.
     *
     * @param resultRepository The repository used to persist competition results.
     */
    public CompetitionResultService(CompetitionResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    /**
     * Adds a competition result for a specific member.
     * This method creates a new competition result object and adds it to the repository.
     *
     * @param member   The member who participated in the competition.
     * @param event    The name of the event in which the member participated.
     * @param placement The placement of the member in the event (e.g., 1st, 2nd, etc.).
     * @param time     The time taken by the member to complete the event.
     * @throws IllegalArgumentException If member or event is null or empty.
     */

    public void addResult(Member member, String event, ActivityType activityType, int placement, double time, String date, MembershipLevel level) {
        Validator.validateMemberNotNull(member);
        Validator.validateEventName(event);
        Validator.validatePlacement(placement);
        Validator.validateTime(time);
        Validator.validateDate(date);
        Validator.validateActivityType(activityType);


        if (event == null || event.isEmpty()) {
            throw new IllegalArgumentException("Event must not be empty.");
        }
        if (activityType == null) {
            throw new IllegalArgumentException("Activity type must not be null.");
        }
        if (placement <= 0) {
            throw new IllegalArgumentException("Placement must be greater than 0.");
        }
        if (time <= 0) {
            throw new IllegalArgumentException("Time must be positive.");
        }
        // Add the competition result to the repository
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Date must not be empty.");
        }



        // Create a new CompetitionResults object with the provided data
        CompetitionResults result = new CompetitionResults(member, level, event, placement, time, date, activityType);
        resultRepository.addResult(result);




    }

    /**
     * Retrieves all competition results for a specific member.
     * This method fetches results from the repository based on the member's ID.
     *
     * @param member The member whose competition results are to be retrieved.
     * @return A list of competition results for the specified member.
     * @throws IllegalArgumentException If member is null.
     */
    public List<CompetitionResults> getResultsByMember(Member member) {
        // Validate the member parameter
        if (member == null) {
            throw new IllegalArgumentException("Member must not be null.");
        }

        // Retrieve and return the results for the specified member
        return resultRepository.getResultsByMember(member);
    }

    /**
     * Retrieves all competition results in the repository.
     * This method returns a list of all competition results, regardless of the member.
     *
     * @return A list of all competition results.
     */
    public List<CompetitionResults> getAllResults() {
        // Retrieve and return all competition results
        return resultRepository.getAllResults();
    }
}