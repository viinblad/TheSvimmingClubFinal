package swimclub.repositories;

import swimclub.models.CompetitionResults;
import swimclub.models.Member;
import swimclub.models.TrainingResults;
import swimclub.utilities.FileHandler;
import swimclub.utilities.Validator;

import java.util.ArrayList;
import java.util.List;

public class TrainingResultsRepository {
    private final List<TrainingResults> results;
    private final FileHandler fileHandler;
    private final String trainingResultsFilePath;

    public TrainingResultsRepository(FileHandler filehandler, String trainingResultsFilePath) {
        this.results = new ArrayList<>();
        this.fileHandler = filehandler;
        this.trainingResultsFilePath = trainingResultsFilePath;
    }

    public void addResults(TrainingResults result) {
        Validator.validateTrainingResult(result);
        results.add(result);
        fileHandler.saveTrainingResults(results, trainingResultsFilePath);
    }

    public List<TrainingResults> getResultsByMember(Member member) {
        List<TrainingResults> memberResults = new ArrayList<>();
        for (TrainingResults result : results) {
            if (result.getMember().equals(member)) {
                memberResults.add(result);
            }
        }
        return memberResults;
    }
    public void addAllResults(List<TrainingResults> resultsToAdd) {
        if (resultsToAdd == null || resultsToAdd.isEmpty()) {
            throw new IllegalArgumentException("The list of results to add cannot be null or empty.");
        }

        for (TrainingResults result : resultsToAdd) {
            Validator.validateTrainingResult(result); // Validate each result
            results.add(result); // Add the result to the in-memory list
        }

        // Save the updated list to the file
        fileHandler.saveTrainingResults(results, trainingResultsFilePath);
    }
    public void loadResults(MemberRepository memberRepository){
        results.clear();
        results.addAll(fileHandler.loadTrainingResults(trainingResultsFilePath,memberRepository));
    }

    public List<TrainingResults> getAllResults(){
        return new ArrayList<>(results);
    }

}
