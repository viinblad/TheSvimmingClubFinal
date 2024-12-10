package swimclub.controllers;

import swimclub.models.ActivityType;
import swimclub.models.CompetitionResults;
import swimclub.models.Member;
import swimclub.models.MembershipLevel;
import swimclub.services.CompetitionResultService;
import swimclub.utilities.Validator;

import java.util.List;

/**
 * Controller for handling competition results.
 * This class acts as an intermediary between the user requests and the service layer,
 * providing methods to add and retrieve competition results.
 */
public class CompetitionResultController {

    private final CompetitionResultService competitionService;

    /**
     * Creates a controller for competition results.
     *
     * @param competitionService Service for managing the storage and processing of competition results.
     */
    public CompetitionResultController(CompetitionResultService competitionService) {
        this.competitionService = competitionService;
    }

    /**
     * Adds a competition result for a given member.
     * This method validates the input and calls the service layer to add the result.
     *
     * @param member       The member participating in the competition.
     * @param event        The name of the event.
     * @param placement    The placement the member achieved in the competition.
     * @param time         The time the member spent in the competition.
     * @param date         The date of the competition.
     * @param level        The membership level of the member.
     * @param activityType The type of activity for the competition (e.g., swimming, running).
     */
    public void addCompetitionResult(Member member, String event, int placement, double time, String date, MembershipLevel level, ActivityType activityType) {
        Validator.validateMemberNotNull(member);
        Validator.validateEventName(event);
        Validator.validatePlacement(placement);
        Validator.validateTime(time);
        Validator.validateDate(date);
        Validator.validateActivityType(activityType);
        competitionService.addResult(member, event, activityType, placement, time, date, level);
    }

    /**
     * Retrieves all competition results for a given member.
     *
     * @param member The member whose results should be fetched.
     * @return A list of competition results for the member.
     */
    public List<CompetitionResults> getResultsByMember(Member member) {
        return competitionService.getResultsByMember(member);
    }

    /**
     * Retrieves all competition results.
     *
     * @return A list of all competition results.
     */
    public List<CompetitionResults> getAllResults() {
        return competitionService.getAllResults();
    }
}
