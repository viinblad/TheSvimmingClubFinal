package swimclub.controllers;

import swimclub.models.ActivityType;
import swimclub.models.CompetitionResults;
import swimclub.models.Member;
import swimclub.models.MembershipLevel;
import swimclub.services.CompetitionResultService;
import swimclub.utilities.Validator;

import java.util.List;

public class CompetitionResultController {
    private final CompetitionResultService competitionService;

    public CompetitionResultController(CompetitionResultService competitionService) {
        this.competitionService = competitionService;
    }

    public void addCompetitionResult(Member member, String event, int placement, double time, String date, MembershipLevel level, ActivityType activityType) {
        Validator.validateMemberNotNull(member);
        Validator.validateEventName(event);
        Validator.validatePlacement(placement);
        Validator.validateTime(time);
        Validator.validateDate(date);
        Validator.validateActivityType(activityType);
        competitionService.addResult(member, event, activityType, placement, time, date, level);
    }

    public List<CompetitionResults> getResultsByMember(Member member) {
        return competitionService.getResultsByMember(member);
    }

    public List<CompetitionResults> getAllResults() {
        return competitionService.getAllResults();
    }
}
