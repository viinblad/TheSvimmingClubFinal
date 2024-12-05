package swimclub.services;

import swimclub.models.ActivityType;
import swimclub.models.CompetitionResults;
import swimclub.models.Member;
import swimclub.models.MembershipLevel;
import swimclub.repositories.CompetitionResultRepository;

import java.util.List;

public class CompetitionResultService {
    private final CompetitionResultRepository resultsRepository;

    public CompetitionResultService(CompetitionResultRepository resultsRepository) {
        this.resultsRepository = resultsRepository;
    }

    public void addResult(Member member, String event, ActivityType activityType, int placement, double time, String date, MembershipLevel level) {
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
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("Date must not be empty.");
        }

        CompetitionResults result = new CompetitionResults(member, level, event, placement, time, date, activityType);
        resultsRepository.addResult(result);

    }

    public List<CompetitionResults> getResultsByMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member must not be null.");
        }
        return resultsRepository.getResultsByMember(member);
    }

    public List<CompetitionResults> getAllResults() {
        return resultsRepository.getAllResults();
    }
}

