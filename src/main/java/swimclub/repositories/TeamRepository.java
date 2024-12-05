package swimclub.repositories;

import swimclub.models.Team;
import swimclub.utilities.FileHandler;
import swimclub.models.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing teams.
 */
public class TeamRepository {
    private final List<Team> teams; // List to store all teams
    private final FileHandler fileHandler;  // Added fileHandler for persistence

    /**
     * Constructor to initialize the repository.
     *
     * @param fileHandler The file handler for loading and saving teams.
     */
    public TeamRepository(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.teams = new ArrayList<>();
        loadTeams();  // Load teams from the file on initialization
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

    // ---------------------------
    // Methods for file handling
    // ---------------------------

    /**
     * Loads teams from the file using the FileHandler.
     */
    public void loadTeams() {
        List<Member> allMembers = new ArrayList<>(); // This should be your actual list of members
        // For this, you would typically call a method to load the members, like:
        // allMembers = memberRepository.findAll(); // Or whatever method you use to get all members

        List<Team> loadedTeams = fileHandler.loadTeams(allMembers);  // Pass members to load teams
        if (loadedTeams != null) {
            this.teams.clear();
            this.teams.addAll(loadedTeams);  // Add loaded teams to the repository
        }
    }

    /**
     * Saves all teams to the file using the FileHandler.
     */
    public void saveTeams() {
        fileHandler.saveTeams(teams);  // Save the list of teams using the FileHandler
    }
}
