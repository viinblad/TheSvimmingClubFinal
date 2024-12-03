package swimclub.services;

import swimclub.models.CompetitionResults;
import swimclub.repositories.CompetitionResultRepository;

import java.util.List;

public class CompetitionResultService {
    private final CompetitionResultRepository resultRepository;

    public CompetitionResultService(CompetitionResultRepository resultRepository) {
        this.resultRepository = resultRepository;
    }

    public void addResult(CompetitionResults result) {
        resultRepository.addResult(result);
    }

    public List<CompetitionResults> getResultsByMemberId(int memberId) {
        return resultRepository.getResultsByMemberId(memberId);
    }

    public List<CompetitionResults> getAllResults() {
        return resultRepository.getAllResults();
    }
}
