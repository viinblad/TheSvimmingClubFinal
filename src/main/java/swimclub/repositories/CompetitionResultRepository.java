package swimclub.repositories;

import swimclub.Utilities.FileHandler;
import swimclub.Utilities.Validator;
import swimclub.models.CompetitionResults;
import swimclub.models.Member;

import java.util.ArrayList;
import java.util.List;

public class CompetitionResultRepository {
    private final List<CompetitionResults> results;
    private final FileHandler fileHandler;
    private final String competitionResultsFilePath;


    public CompetitionResultRepository(FileHandler fileHandler, String competitionResultsFilePath) {
        this.results = new ArrayList<>();
        this.fileHandler = fileHandler;
        this.competitionResultsFilePath = competitionResultsFilePath;
    }

    public void addResult(CompetitionResults result) {
        Validator.validateCompetitionResult(result);
        results.add(result);
        fileHandler.saveCompetitionResults(results, competitionResultsFilePath); // Save to file


    }

    public List<CompetitionResults> getAllResults() {
        return new ArrayList<>(results);
    }


    public List<CompetitionResults> getResultsByMember(Member member) {
        List<CompetitionResults> memberResults = new ArrayList<>();
        for (CompetitionResults result : results) {
            if (result.getMember().equals(member)) {
                memberResults.add(result);
            }
        }
        return memberResults;
    }

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


    public void loadResults(MemberRepository memberRepository) {
        results.clear(); // Clear existing in-memory data
        results.addAll(fileHandler.loadCompetitionResults(competitionResultsFilePath, memberRepository));
    }
}


