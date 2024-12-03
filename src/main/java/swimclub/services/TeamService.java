package swimclub.services;

import swimclub.models.Member;
import swimclub.models.Team;
import swimclub.models.TeamType;
import swimclub.repositories.TeamRepository;

import java.util.List;

/**
 * Service class for managing teams.
 */
public class TeamService {
    private final TeamRepository teamRepository;

    /**
     * Constructor to initialize the service.
     *
     * @param teamRepository The repository for storing teams.
     */
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * Creates a new team.
     *
     * @param teamName The name of the team.
     * @return The created Team object.
     */
    public Team createTeam(String teamName, String teamTypeString) {
        TeamType teamType = TeamType.fromString(teamTypeString);
        if (teamRepository.findTeamByName(teamName) != null) {
            throw new IllegalArgumentException("Team with this name already exists.");
        }
        Team newTeam = new Team(teamName, teamType);
        teamRepository.addTeam(newTeam);
        return newTeam;
    }

    /**
     * Adds a member to a team.
     *
     * @param teamName The name of the team.
     * @param member   The member to add.
     */
    public void addMemberToTeam(String teamName, Member member) {
        Team team = teamRepository.findTeamByName(teamName);
        if (team == null) {
            throw new IllegalArgumentException("Team not found.");
        }
        team.addMember(member);
        member.setTeam(team); // Set the member's team
    }

    /**
     * Removes a member from a team.
     *
     * @param teamName The name of the team.
     * @param member   The member to remove.
     */
    public void removeMemberFromTeam(String teamName, Member member) {
        Team team = teamRepository.findTeamByName(teamName);
        if (team == null) {
            throw new IllegalArgumentException("Team not found.");
        }
        team.removeMember(member);
        member.setTeam(null); // Remove the team reference from the member
    }

    /**
     * Retrieves all teams.
     *
     * @return A list of all teams.
     */
    public List<Team> getAllTeams() {
        return teamRepository.getAllTeams();
    }
}
