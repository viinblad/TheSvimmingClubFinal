package swimclub.controllers;

import swimclub.models.Member;
import swimclub.models.Team;
import swimclub.models.Coach;
import swimclub.services.TeamService;

import java.util.List;

/**
 * Controller class for managing teams.
 */
public class TeamController {
    private final TeamService teamService;

    // === CONSTRUCTOR ===
    /**
     * Constructor to initialize the TeamController with TeamService.
     *
     * @param teamService The service responsible for managing teams.
     */
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    // === CREATE A TEAM ===
    /**
     * Creates a new team with a given name, type, and assigned coach.
     *
     * @param teamName The name of the team.
     * @param teamTypeString The type of the team (e.g., Junior, Senior).
     * @param coach The coach assigned to the team.
     * @return The created Team object.
     */
    public Team createTeam(String teamName, String teamTypeString, Coach coach) {
        return teamService.createTeam(teamName, teamTypeString, coach); // Delegate team creation to service
    }

    // === ADD A MEMBER TO A TEAM ===
    /**
     * Adds a member to a specified team.
     *
     * @param teamName The name of the team.
     * @param member The member to add to the team.
     */
    public void addMemberToTeam(String teamName, Member member) {
        teamService.addMemberToTeam(teamName, member); // Delegate adding member to team
    }

    // === REMOVE A MEMBER FROM A TEAM ===
    /**
     * Removes a member from a specified team.
     *
     * @param teamName The name of the team.
     * @param member The member to remove from the team.
     */
    public void removeMemberFromTeam(String teamName, Member member) {
        teamService.removeMemberFromTeam(teamName, member); // Delegate member removal to service
    }

    // === DELETE A TEAM ===
    /**
     * Deletes a team by its name.
     *
     * @param teamName The name of the team to delete.
     */
    public void deleteTeam(String teamName) {
        teamService.deleteTeam(teamName); // Delegate team deletion to service
    }

    // === ASSIGN A TEAM COACH ===
    /**
     * Assigns a coach as the team leader of a team.
     *
     * @param teamName The name of the team.
     * @param coach The coach to assign as the team leader.
     */
    public void assignTeamCoach(String teamName, Coach coach) {
        teamService.assignTeamCoach(teamName, coach); // Delegate assigning coach to service
    }

    // === REMOVE TEAM COACH ===
    /**
     * Removes the assigned coach from a team.
     *
     * @param teamName The name of the team.
     */
    public void removeTeamCoach(String teamName) {
        teamService.removeTeamCoach(teamName); // Delegate removal of coach from team
    }

    // === FIND A TEAM BY NAME ===
    /**
     * Finds a team by its name.
     *
     * @param name The name of the team.
     * @return The team object if found, null otherwise.
     */
    public Team findTeamByName(String name) {
        return teamService.findTeamByName(name); // Delegate to service to find the team by name
    }

    // === RETRIEVE ALL TEAMS ===
    /**
     * Retrieves a list of all teams in the system.
     *
     * @return A list of all teams.
     */
    public List<Team> getAllTeams() {
        return teamService.getAllTeams(); // Delegate to service to get all teams
    }

    // === PRINT ALL TEAMS ===
    /**
     * Prints all teams in the system.
     *
     * @return A boolean indicating whether the teams were printed successfully.
     */
    public boolean printAllTeams() {
        return teamService.printAllTeams(); // Delegate to service to print all teams
    }
}