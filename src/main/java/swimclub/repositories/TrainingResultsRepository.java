package swimclub.repositories;

import swimclub.models.ActivityType;
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
    private final MemberRepository memberRepository;

    public TrainingResultsRepository(FileHandler filehandler, String trainingResultsFilePath, MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        this.results = new ArrayList<>();
        this.fileHandler = filehandler;
        this.trainingResultsFilePath = trainingResultsFilePath;
    }
    public Member findById(int id){
        for (TrainingResults result : results){
            if (result.getMember().getMemberId() == id){
                return result.getMember();
            }
        }
        return null;
    }

    public TrainingResults findResultsByMemberAndActivity(int memberId, ActivityType activityType) {
        // Iterate through all training results in the list
        for (TrainingResults result : results) {
            // Check if both memberId and activityType match
            if (result.getMember().getMemberId() == memberId && result.getActivityType().equals(activityType)) {
                return result; // Return the first matching result
            }
        }
        // If no match is found, return null
        return null;
    }

    public void updateResults(TrainingResults updatedResults){
        TrainingResults existingResults = findResultsByMemberAndActivity(updatedResults.getMember().getMemberId(),updatedResults.getMember().getActivityType());

        if (existingResults == null) {
            throw new RuntimeException("Member not found for ID " + updatedResults.getMember().getMemberId());
        }
        existingResults.setTime(updatedResults.getTime());
        existingResults.setDate(updatedResults.getDate());

        fileHandler.saveTrainingResults(results);

    loadResults(memberRepository);

    }

    public void addResults(TrainingResults result) {
        Validator.validateTrainingResult(result);
        results.add(result);
        fileHandler.saveTrainingResults(results);
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
        fileHandler.saveTrainingResults(results);
    }
    public void loadResults(MemberRepository memberRepository){
        results.clear();
        results.addAll(fileHandler.loadTrainingResults(trainingResultsFilePath,memberRepository));
    }

    public List<TrainingResults> getAllResults(){
        return new ArrayList<>(results);
    }

}
