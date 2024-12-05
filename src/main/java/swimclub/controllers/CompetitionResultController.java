package swimclub.controllers;

import swimclub.models.CompetitionResults;
import swimclub.models.Member;
import swimclub.services.CompetitionResultService;

import java.util.List;

public class CompetitionResultController {
    private final CompetitionResultService resultService;

    public CompetitionResultController(CompetitionResultService resultService) {
        this.resultService = resultService;
    }

    // Add a result using a Member object
    public void addResult(Member member, String event, int placement, double time) {
        resultService.addResult(member, event, placement, time);
    }

    // Get all competition results
    public List<CompetitionResults> getAllResults() {
        return resultService.getAllResults();
    }

    // Get results for a specific member
    public List<CompetitionResults> getResultsByMember(Member member) {
        return resultService.getResultsByMember(member);
    }
}
