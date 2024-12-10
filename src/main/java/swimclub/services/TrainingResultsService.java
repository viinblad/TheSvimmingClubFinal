package swimclub.services;

import swimclub.models.ActivityType;
import swimclub.models.Member;
import swimclub.models.MembershipLevel;
import swimclub.models.TrainingResults;
import swimclub.repositories.TrainingResultsRepository;

import java.util.List;

/**
 * Service class for managing training results.
 * This class provides methods for adding, retrieving, and validating training results.
 */
public class TrainingResultsService {
    private final TrainingResultsRepository resultsRepository;

    /**
     * Constructor to initialize the service with a training results repository.
     *
     * @param resultsRepository The repository used for storing and retrieving training results.
     */
    public TrainingResultsService(TrainingResultsRepository resultsRepository){
        this.resultsRepository = resultsRepository;
    }

    /**
     * Adds a new training result for a member.
     * This method validates the input and creates a `TrainingResults` object,
     * which is then added to the repository.
     *
     * @param member       The member who participated in the training session.
     * @param activityType The type of activity performed during the training (e.g., swimming, running).
     * @param time         The time spent on the activity during the training.
     * @param date         The date of the training session.
     * @param level        The membership level of the member.
     * @throws IllegalArgumentException If any of the input parameters are invalid (e.g., null or negative values).
     */
    public void addResult(Member member, ActivityType activityType, double time, String date, MembershipLevel level){
        if (activityType == null){
            throw new IllegalArgumentException("Fill in discipline.");
        }
        if (time == -1){
            throw new IllegalArgumentException("Time must be positive.");
        }
        if(date == null){
            throw new IllegalArgumentException("Fill in date.");
        }
        TrainingResults results = new TrainingResults(member, level, activityType, time, date);
        resultsRepository.addResults(results);
    }

    /**
     * Retrieves all training results for a specific member.
     *
     * @param member The member whose training results are to be retrieved.
     * @return A list of training results for the specified member.
     * @throws IllegalArgumentException If the provided member is null.
     */
    public List<TrainingResults> getResultsByMember(Member member){
        if (member == null){
            throw new IllegalArgumentException("Member must not be null.");
        }
        return resultsRepository.getResultsByMember(member);
    }

    /**
     * Retrieves all training results stored in the repository.
     *
     * @return A list of all training results.
     */
    public List<TrainingResults> getAllResults(){
        return resultsRepository.getAllResults();
    }
}
