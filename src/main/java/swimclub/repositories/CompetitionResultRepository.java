package swimclub.repositories;

import swimclub.Utilities.Validator;
import swimclub.models.CompetitionResults;

import java.util.ArrayList;
import java.util.List;

public class CompetitionResultRepository {
    private final List<CompetitionResults> results;


    public CompetitionResultRepository() {
        this.results = new ArrayList<>();
    }

    public void addResult(CompetitionResults result) {
        Validator.validateCompetitionResult(result);
        results.add(result);

    }
    public List<CompetitionResults> getAllResults() {
        return new ArrayList<>(results);
    }


    public List<CompetitionResults> getResultsByMemberId(int memberId) {
        List<CompetitionResults> memberResults = new ArrayList<>();
        for (CompetitionResults result : results) {
            if (result.getMember().getMemberId() == memberId) {
                memberResults.add(result);
            }
        }
        return memberResults;
    }
}



