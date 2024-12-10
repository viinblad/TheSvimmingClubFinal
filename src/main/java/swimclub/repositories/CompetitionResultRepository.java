package swimclub.repositories;

import swimclub.models.CompetitionResults;
import swimclub.models.Member;
import swimclub.utilities.FileHandler;
import swimclub.utilities.Validator;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing competition results.
 * This class handles adding, retrieving, and saving competition results to/from a file.
 */
public class CompetitionResultRepository {
    private final List<CompetitionResults> results;
    private final FileHandler fileHandler;
    private final String competitionResultsFilePath;

    /**
     * Constructor to initialize the repository with a file handler and a file path for storing competition results.
     *
     * @param fileHandler               The file handler used to save and load competition results.
     * @param competitionResultsFilePath The file path where the competition results are stored.
     */
    public CompetitionResultRepository(FileHandler fileHandler, String competitionResultsFilePath) {
        this.results = new ArrayList<>();
        this.fileHandler = fileHandler;
        this.competitionResultsFilePath = competitionResultsFilePath;
    }

    /**
     * Adds a new competition result to the repository and saves it to a file.
     * The result is validated before it is added.
     *
     * @param result The competition result to add.
     */
    public void addResult(CompetitionResults result) {
        Validator.validateMemberNotNull(result.getMember());
        Validator.validateEventName(result.getEvent());
        Validator.validatePlacement(result.getPlacement());
        Validator.validateTime(result.getTime());
        Validator.validateDate(result.getDate());
        Validator.validateActivityType(result.getActivityType());

        results.add(result);
        fileHandler.saveCompetitionResults(results, competitionResultsFilePath);
    }

    /**
     * Retrieves all competition results for a specific member.
     *
     * @param member The member whose competition results are to be retrieved.
     * @return A list of competition results for the specified member.
     */
    public List<CompetitionResults> getResultsByMember(Member member) {
        List<CompetitionResults> memberResults = new ArrayList<>();
        for (CompetitionResults result : results) {
            if (result.getMember().equals(member)) {
                memberResults.add(result);
            }
        }
        return memberResults;
    }

    /**
     * Adds a list of competition results to the repository and saves them to the file.
     * Each result is validated before it is added.
     *
     * @param resultsToAdd The list of competition results to add.
     * @throws IllegalArgumentException If the provided list is null or empty.
     */
    public void addAllResults(List<CompetitionResults> resultsToAdd) {
        if (resultsToAdd == null || resultsToAdd.isEmpty()) {
            throw new IllegalArgumentException("The list of results to add cannot be null or empty.");
        }

        for (CompetitionResults result : resultsToAdd) {
            Validator.validateCompetitionResult(result); // Validate each result
            results.add(result); // Add the result to the in-memory list
        }

        // Save the updated list to the file
        fileHandler.saveCompetitionResults(results, competitionResultsFilePath);
    }

    /**
     * Loads the competition results from the file and adds them to the in-memory list.
     *
     * @param memberRepository The member repository used to resolve members when loading results.
     */
    public void loadResults(MemberRepository memberRepository) {
        results.clear();
        results.addAll(fileHandler.loadCompetitionResults(competitionResultsFilePath, memberRepository));
    }

    /**
     * Retrieves all competition results stored in the repository.
     *
     * @return A list of all competition results.
     */
    public List<CompetitionResults> getAllResults() {
        return new ArrayList<>(results);
    }
}
