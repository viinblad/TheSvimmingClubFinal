package swimclub.repositories;

import swimclub.models.Coach;
import swimclub.utilities.FileHandler;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

public class StaffRepository {
    private final FileHandler fileHandler;
    private List<Coach> coachList;

    /**
     * Constructor for StaffRepository that initializes the list of coaches
     * using a FileHandler to load data.
     *
     * @param fileHandler The FileHandler object used to load and save coach data.
     */
    public StaffRepository(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
        this.coachList = fileHandler.loadCoaches(); // Load coaches from the file
    }

    /**
     * Finds a coach by their unique coach ID.
     *
     * @param coachId The ID of the coach to find.
     * @return The Coach object if found, otherwise null.
     */
    public Coach findCoachById(int coachId) {
        Optional<Coach> coach = coachList.stream()
                .filter(c -> c.getCoachId() == coachId)
                .findFirst();
        return coach.orElse(null); // Return null if the coach is not found
    }

    /**
     * Finds a coach by their associated team name.
     *
     * @param teamName The name of the team to search for a coach.
     * @return The Coach object if found, otherwise null.
     */
    public Coach findCoachByTeamName(String teamName) {
        for (Coach coach : coachList) {
            if (coach.getTeamName().equalsIgnoreCase(teamName)) {
                return coach;
            }
        }
        return null; // Return null if no coach is found with the given team name
    }

    /**
     * Returns the list of all coaches.
     *
     * @return List of all Coach objects in the repository.
     */
    public List<Coach> getCoachList() {
        return this.coachList; // Return the list of coaches
    }

    /**
     * Fetches all coaches in the repository.
     *
     * @return List of all coaches.
     */
    public List<Coach> findAll() {
        return coachList; // Return the list of all coaches
    }

    /**
     * Generates the next available coach ID by checking the highest ID in the current list.
     * If the list is empty, returns 1 as the first ID.
     *
     * @return The next available coach ID.
     */
    public int getNextCoachId() {
        // Check if coachList is null or empty
        if (coachList == null) {
            coachList = new ArrayList<>();  // Initialize the list if it's null
        }

        if (coachList.isEmpty()) {
            return 1; // If no coaches, return 1 as the next coach ID
        }

        // Find the maximum coach ID from the existing list
        int maxId = 0;
        for (Coach coach : coachList) {
            if (coach.getCoachId() > maxId) {
                maxId = coach.getCoachId(); // Update maxId if the current coach ID is greater
            }
        }

        // Return the next ID, which is one more than the max ID
        return maxId + 1;
    }

    /**
     * Deletes a coach from the list and updates the file.
     *
     * @param coach The coach object to be deleted.
     * @return true if the coach was successfully deleted, false otherwise.
     */
    public boolean deleteCoach(Coach coach) {
        boolean isRemoved = coachList.remove(coach);
        if (isRemoved) {
            fileHandler.deleteCoach(coach); // Delete the coach using the file handler
        }
        return isRemoved; // Return whether the coach was successfully removed
    }

    /**
     * Adds a new coach to the repository and updates the file.
     *
     * @param coach The coach object to add to the repository.
     */
    public void addCoach(Coach coach) {
        coachList.add(coach); // Add the coach to the list
        fileHandler.saveCoaches(coachList); // Save the updated list of coaches to the file
    }

    /**
     * Saves the current list of coaches to the file.
     */
    public void saveCoachList() {
        fileHandler.saveCoaches(coachList); // Save the list of coaches to the file
    }
}
