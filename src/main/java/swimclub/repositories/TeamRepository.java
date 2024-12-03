package swimclub.repositories;

import swimclub.models.Team;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing teams.
 */
public class TeamRepository {
    private final List<Team> teams; // List to store all teams

    /**
     * Constructor to initialize the repository.
     */
    public TeamRepository() {
        this.teams = new ArrayList<>();
    }

    /**
     * Adds a new team to the repository.
     *
     * @param team The team to add.
     */
    public void addTeam(Team team) {
        teams.add(team);
    }

    /**
     * Finds a team by its name.
     *
     * @param teamName The name of the team.
     * @return The team object or null if not found.
     */
    public Team findTeamByName(String teamName) {
        return teams.stream()
                .filter(team -> team.getTeamName().equalsIgnoreCase(teamName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all teams.
     *
     * @return A list of all teams.
     */
    public List<Team> getAllTeams() {
        return new ArrayList<>(teams); // Return a copy to prevent modification
    }

    /**
     * Removes a team by its name.
     *
     * @param teamName The name of the team to remove.
     * @return true if the team was removed, false otherwise.
     */
    public boolean removeTeam(String teamName) {
        return teams.removeIf(team -> team.getTeamName().equalsIgnoreCase(teamName));
    }
}
