package swimclub.controllers;

import swimclub.models.Member;
import swimclub.models.Team;
import swimclub.services.TeamService;

import java.util.List;

/**
 * Controller class for managing teams.
 */
public class TeamController {
    private final TeamService teamService;

    /**
     * Constructor to initialize the controller.
     *
     * @param teamService The service for managing teams.
     */
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Creates a new team.
     *
     * @param teamName The name of the team.
     * @return The created Team object.
     */
    public Team createTeam(String teamName) {
        return teamService.createTeam(teamName);
    }

    /**
     * Adds a member to a team.
     *
     * @param teamName The name of the team.
     * @param member   The member to add.
     */
    public void addMemberToTeam(String teamName, Member member) {
        teamService.addMemberToTeam(teamName, member);
    }

    /**
     * Retrieves all teams.
     *
     * @return A list of all teams.
     */
    public List<Team> getAllTeams() {
        return teamService.getAllTeams();
    }
}
