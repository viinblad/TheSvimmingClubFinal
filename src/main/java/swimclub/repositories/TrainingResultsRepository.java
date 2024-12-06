package swimclub.repositories;

import swimclub.models.CompetitionResults;
import swimclub.models.Member;
import swimclub.models.TrainingResults;
import swimclub.utilities.FileHandler;
import swimclub.utilities.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing training results in the swim club.
 * This class provides methods to add, retrieve, validate, load, and persist training results.
 */
public class TrainingResultsRepository {
    private final List<TrainingResults> results; // List to store all training results
    private final FileHandler fileHandler; // FileHandler for saving and loading results
    private final String trainingResultsFilePath; // File path to store and load results

    /**
     * Constructor to initialize the repository with file handler and file path.
     * Initializes an empty list of results.
     *
     * @param fileHandler           The file handler used for saving and loading results.
     * @param trainingResultsFilePath The file path to load and save training results.
     */
    public TrainingResultsRepository(FileHandler fileHandler, String trainingResultsFilePath) {
        this.results = new ArrayList<>();  // Initialize the results list
        this.fileHandler = fileHandler;
        this.trainingResultsFilePath = trainingResultsFilePath;
    }

    /**
     * Adds a new training result to the repository.
     * The result is validated before being added to the list.
     * After adding, the results are saved to the file.
     *
     * @param result The training result to add.
     */
    public void addResults(TrainingResults result) {
        Validator.validateTrainingResult(result); // Validate the training result
        results.add(result); // Add the result to the in-memory list
        fileHandler.saveTrainingResults(results, trainingResultsFilePath); // Save the updated list to the file
    }

    /**
     * Retrieves all training results for a specific member.
     * This method returns a list of results for the specified member.
     *
     * @param member The member whose training results to retrieve.
     * @return A list of training results for the specified member.
     */
    public List<TrainingResults> getResultsByMember(Member member) {
        List<TrainingResults> memberResults = new ArrayList<>();
        for (TrainingResults result : results) {
            if (result.getMember().equals(member)) {  // Match results based on the member
                memberResults.add(result);
            }
        }
        return memberResults;  // Return the list of member's results
    }

    /**
     * Adds multiple training results to the repository at once.
     * Each result is validated before being added. After all results are added,
     * the updated list is saved to the file.
     *
     * @param resultsToAdd A list of training results to add to the repository.
     * @throws IllegalArgumentException if the list of results to add is null or empty.
     */
    public void addAllResults(List<TrainingResults> resultsToAdd) {
        if (resultsToAdd == null || resultsToAdd.isEmpty()) {
            throw new IllegalArgumentException("The list of results to add cannot be null or empty.");
        }

        // Validate and add each result to the in-memory list
        for (TrainingResults result : resultsToAdd) {
            Validator.validateTrainingResult(result); // Validate each result
            results.add(result);  // Add the result to the in-memory list
        }

        // Save the updated results list to the file
        fileHandler.saveTrainingResults(results, trainingResultsFilePath);
    }

    /**
     * Loads all training results from a file and associates them with members.
     * The results are loaded and then the in-memory list is cleared and populated with the loaded results.
     *
     * @param memberRepository The MemberRepository used to link results with members.
     */
    public void loadResults(MemberRepository memberRepository) {
        results.clear();  // Clear the existing results list
        // Load the results from the file and populate the results list
        results.addAll(fileHandler.loadTrainingResults(trainingResultsFilePath, memberRepository));
    }

    /**
     * Retrieves all training results in the repository.
     * This method returns a copy of the list to prevent external modifications.
     *
     * @return A list of all training results in the repository.
     */
    public List<TrainingResults> getAllResults() {
        return new ArrayList<>(results);  // Return a copy of the results list
    }
}