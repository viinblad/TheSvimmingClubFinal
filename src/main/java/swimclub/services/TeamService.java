package swimclub.services;

import swimclub.models.Coach;
import swimclub.models.Member;
import swimclub.models.Team;
import swimclub.models.TeamType;
import swimclub.repositories.TeamRepository;

import java.util.List;

/**
 * Service class responsible for managing teams, including creating, modifying,
 * and deleting teams and managing team members and coaches.
 */
public class TeamService {
    // Repository for accessing and modifying team data
    private final TeamRepository teamRepository;

    /**
     * Constructor to initialize the service with the given TeamRepository.
     *
     * @param teamRepository The repository for storing and retrieving teams.
     */
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    /**
     * Creates a new team with the given name and team type, and assigns a coach.
     *
     * @param teamName        The name of the new team.
     * @param teamTypeString  The type of the team (e.g., Junior Competitive or Senior Competitive).
     * @param coach           The coach assigned to the new team.
     * @return The created Team object.
     * @throws IllegalArgumentException if a team with the same name already exists.
     */
    public Team createTeam(String teamName, String teamTypeString, Coach coach) {
        // Convert the team type string into a TeamType enum
        TeamType teamType = TeamType.fromString(teamTypeString);

        // Check if a team with the same name already exists
        if (teamRepository.findTeamByName(teamName) != null) {
            throw new IllegalArgumentException("Team with this name already exists.");
        }

        // Create a new team with the provided details
        Team newTeam = new Team(teamName, teamType, coach);

        // Add the new team to the repository
        teamRepository.addTeam(newTeam);

        // Save the team changes to the repository (e.g., file)
        saveTeam();

        return newTeam;
    }

    /**
     * Adds a member to an existing team.
     *
     * @param teamName The name of the team.
     * @param member   The member to add to the team.
     * @throws IllegalArgumentException if the team does not exist.
     */
    public void addMemberToTeam(String teamName, Member member) {
        // Find the team by name
        Team team = teamRepository.findTeamByName(teamName);

        // If the team is not found, throw an exception
        if (team == null) {
            throw new IllegalArgumentException("Team not found.");
        }

        // Check if the member is already in a team
        if (member.getTeamName() != null && !member.getTeamName().equalsIgnoreCase("No team")) {
            throw new IllegalArgumentException("Member is already part of a team.");
        }

        // Check if the member is already in the specified team
        if (team.getMembers().contains(member)) {
            throw new IllegalArgumentException("Member is already part of this team.");
        }

        // Add the member to the team and set the team for the member
        team.addMember(member);
        member.setTeamName(team.getTeamName()); // Update the member's team name

        // Save the changes to the repository (for both team and member)
        teamRepository.saveTeams();
    }
    /**
     * Removes a member from a team.
     *
     * @param teamName The name of the team.
     * @param member   The member to remove from the team.
     * @throws IllegalArgumentException if the team does not exist or the member is not part of the team.
     */
    public void removeMemberFromTeam(String teamName, Member member) {
        // Find the team by name
        Team team = teamRepository.findTeamByName(teamName);

        // If the team is found, attempt to remove the member
        if (team != null) {
            // If the member is not part of the team, throw an exception
            if (!team.getMembers().contains(member)) {
                throw new IllegalArgumentException("Member is not part of the team.");
            }
            // Remove the member from the team and set their team to null
            team.removeMember(member);
            member.setTeamName(null);

            // Save the changes to the repository
            teamRepository.saveTeams();
        } else {
            // If the team is not found, throw an exception
            throw new IllegalArgumentException("Team not found.");
        }
    }

    /**
     * Prints a list of all teams with their details.
     *
     * @return True if teams are found and printed, false if no teams exist.
     */
    public boolean printAllTeams() {
        List<Team> teams = teamRepository.getAllTeams();

        // If there are no teams, return false
        if (teams.isEmpty()) {
            return false;
        } else {
            // Print the details of each team
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
     * @throws IllegalArgumentException if the team does not exist.
     */
    public void deleteTeam(String teamName) {
        // Find the team by name
        Team team = teamRepository.findTeamByName(teamName);

        // If the team is not found, throw an exception
        if (team == null) {
            throw new IllegalArgumentException("Team not found.");
        }

        // Remove the team from the repository
        teamRepository.removeTeam(teamName);

        // Save the changes to the repository
        teamRepository.saveTeams();
    }

    /**
     * Assigns a coach to a team.
     *
     * @param teamName The name of the team.
     * @param coach    The coach to assign to the team.
     * @throws IllegalArgumentException if the team does not exist.
     */
    public void assignTeamCoach(String teamName, Coach coach) {
        // Find the team by name
        Team team = teamRepository.findTeamByName(teamName);

        // If the team is not found, throw an exception
        if (team == null) {
            throw new IllegalArgumentException("Team not found.");
        }

        // Assign the coach to the team
        team.setTeamCoach(coach);

        // Save the changes to the repository
        teamRepository.saveTeams();
    }

    /**
     * Removes the coach from a team.
     *
     * @param teamName The name of the team.
     * @throws IllegalArgumentException if the team does not exist.
     */
    public void removeTeamCoach(String teamName) {
        // Find the team by name
        Team team = teamRepository.findTeamByName(teamName);

        // If the team is not found, throw an exception
        if (team == null) {
            throw new IllegalArgumentException("Team not found.");
        }

        // Remove the coach from the team
        team.setTeamCoach(null);

        // Save the changes to the repository
        teamRepository.saveTeams();
    }

    /**
     * Retrieves a list of all teams.
     *
     * @return A list of all teams in the repository.
     */
    public List<Team> getAllTeams() {
        return teamRepository.getAllTeams();
    }

    /**
     * Finds a team by its name.
     *
     * @param name The name of the team to find.
     * @return The team object if found, or null if not found.
     */
    public Team findTeamByName(String name) {
        return teamRepository.findTeamByName(name);
    }

    /**
     * Saves the current team data to the repository (e.g., file).
     */
    public void saveTeam() {
        teamRepository.saveTeams();
    }
}