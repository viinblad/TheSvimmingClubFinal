package swimclub.services;

import swimclub.models.Coach;
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
     * @param teamTypeString The type of the team (e.g., Junior Competitive or Senior Competitive).
     * @return The created Team object.
     */
    public Team createTeam(String teamName, String teamTypeString, Coach coach) {
        TeamType teamType = TeamType.fromString(teamTypeString);
        if (teamRepository.findTeamByName(teamName) != null) {
            throw new IllegalArgumentException("Team with this name already exists.");
        }
        Team newTeam = new Team(teamName, teamType, coach);
        teamRepository.addTeam(newTeam);
        saveTeam();
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
        member.setTeam(team);
        teamRepository.saveTeams(); // Save changes to the file
    }

    /**
     * Removes a member from a team.
     *
     * @param teamName The name of the team.
     * @param member   The member to remove.
     */
    public void removeMemberFromTeam(String teamName, Member member) {
        Team team = teamRepository.findTeamByName(teamName);
        if (team != null) {
            if (!team.getMembers().contains(member)) {
                throw new IllegalArgumentException("Member is not part of the team.");
            }
            team.removeMember(member);
            member.setTeam(null);
            teamRepository.saveTeams(); // Save changes to the file
        } else {
            throw new IllegalArgumentException("Team not found.");
        }
    }

    public boolean printAllTeams() {
        List<Team> teams = teamRepository.getAllTeams();
        if (teams.isEmpty()) {
            return false;
        } else {
            System.out.println("List of all Teams:");
            for (Team team : teams) {
                System.out.println("Team Name: " + team.getTeamName() +
                        ", Team Type: " + team.getTeamType().getDisplayName() +
                        ", Coach: " + (team.getTeamCoach() != null ? team.getTeamCoach().getName() : "None") +
                        ", Members Count: " + team.getMembers().size());
            }
        }
        return true;
    }

    /**
     * Deletes a team by its name.
     *
     * @param teamName The name of the team to delete.
     */
    public void deleteTeam(String teamName) {
        Team team = teamRepository.findTeamByName(teamName);
        if (team == null) {
            throw new IllegalArgumentException("Team not found.");
        }
        teamRepository.removeTeam(teamName);
        teamRepository.saveTeams(); // Save changes to the file
    }

    /**
     * Assigns a member as the team leader.
     *
     * @param teamName The name of the team.
     * @param coach   The member to assign as team leader.
     */
    public void assignTeamCoach(String teamName, Coach coach) {
        Team team = teamRepository.findTeamByName(teamName);
        if (team == null) {
            throw new IllegalArgumentException("Team not found.");
        }
        team.setTeamCoach(coach); // Set the team coach
        teamRepository.saveTeams();
    }

    public void removeTeamCoach(String teamName) {
        Team team = teamRepository.findTeamByName(teamName);
        if (team == null) {
            throw new IllegalArgumentException("Team not found.");
        }
        team.setTeamCoach(null); // Set the team leader
        teamRepository.saveTeams();
    }

    /**
     * Retrieves all teams.
     *
     * @return A list of all teams.
     */
    public List<Team> getAllTeams() {
        return teamRepository.getAllTeams();
    }

    public Team findTeamByName(String name) {
        return teamRepository.findTeamByName(name);
    }

    public void saveTeam () {
        teamRepository.saveTeams();
    }
}
