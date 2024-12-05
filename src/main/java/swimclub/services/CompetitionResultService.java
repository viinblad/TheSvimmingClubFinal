package swimclub.services;

import swimclub.models.CompetitionResults;
import swimclub.models.Member;
import swimclub.repositories.CompetitionResultRepository;

import java.time.LocalDate;
import java.util.List;

public class CompetitionResultService {
    private final CompetitionResultRepository resultRepository;

    public CompetitionResultService(CompetitionResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public void addResult(Member member, String event, int placement, double time) {
        if (member == null || event == null || event.isEmpty()) {
            throw new IllegalArgumentException("Member and event must not be null or empty.");
        }
        CompetitionResults result = new CompetitionResults(member, event, placement, time);
        resultRepository.addResult(result);


    }

    public List<CompetitionResults> getResultsByMember(Member member) {
        if (member == null) {
            throw new IllegalArgumentException("Member must not be null.");
        }
        return resultRepository.getResultsByMember(member);
    }

    public List<CompetitionResults> getAllResults() {
        return resultRepository.getAllResults();
    }
}
