package swimclub.repositories;

import swimclub.models.Team;
import swimclub.utilities.FileHandler;
import swimclub.models.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository class for managing teams in the swim club.
 * This class provides methods for adding, finding, removing,
 * and persisting teams using a FileHandler.
 */
public class TeamRepository {
    private final List<Team> teams; // List to store all teams
    private final FileHandler fileHandler; // FileHandler for team persistence

    /**
     * Constructor to initialize the TeamRepository.
     * It initializes the list of teams and assigns the file handler for persistence.
     *
     * @param fileHandler The file handler used for loading and saving teams.
     */
    public TeamRepository(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.teams = new ArrayList<>();  // Initialize the teams list
    }

    /**
     * Adds a new team to the repository.
     * This method does not check for duplicates, so ensure unique teams before adding.
     *
     * @param team The Team object to add to the repository.
     */
    public void addTeam(Team team) {
        teams.add(team);  // Add the team to the list
    }

    /**
     * Finds a team by its name.
     * This method performs a case-insensitive search to find the team.
     *
     * @param teamName The name of the team to search for.
     * @return The Team object if found, otherwise null.
     */
    public Team findTeamByName(String teamName) {
        return teams.stream()
                .filter(team -> team.getTeamName().equalsIgnoreCase(teamName))
                .findFirst()
                .orElse(null);  // Return null if no team is found with the given name
    }

    /**
     * Retrieves all teams in the repository.
     * This returns a copy of the list to prevent external modifications.
     *
     * @return A list of all teams in the repository.
     */
    public List<Team> getAllTeams() {
        return new ArrayList<>(teams);  // Return a copy of the teams list
    }

    /**
     * Removes a team from the repository based on its name.
     * If the team is found, it will be removed; otherwise, nothing happens.
     *
     * @param teamName The name of the team to remove.
     * @return true if the team was removed, false if no team was found with the given name.
     */
    public boolean removeTeam(String teamName) {
        return teams.removeIf(team -> team.getTeamName().equalsIgnoreCase(teamName));
        // Return true if any team was removed; false otherwise
    }

    // ---------------------------
    // Methods for file handling
    // ---------------------------

    /**
     * Loads teams from a file and associates them with members and staff.
     * This method uses the FileHandler to read the teams from the file and populate the repository.
     *
     * @param memberRepository The MemberRepository to link members to the teams.
     * @param staffRepository The StaffRepository to link coaches to the teams.
     */
    public void loadTeams(MemberRepository memberRepository, StaffRepository staffRepository) {
        List<Member> allMembers = memberRepository.findAll();  // Get all members from the member repository
        List<Team> loadedTeams = fileHandler.loadTeams(allMembers, staffRepository);  // Load teams using fileHandler

        if (loadedTeams != null) {
            this.teams.clear();  // Clear the current list of teams
            this.teams.addAll(loadedTeams);  // Add the loaded teams to the repository
        }
    }

    /**
     * Saves the current list of teams to a file.
     * This method uses the FileHandler to persist the list of teams.
     */
    public void saveTeams() {
        fileHandler.saveTeams(teams);  // Save the teams list using the FileHandler
    }
}