package swimclub.repositories;

import swimclub.models.Coach;
import swimclub.models.Member;
import swimclub.models.Staff;
import swimclub.services.StaffService;
import swimclub.services.TeamService;
import swimclub.utilities.FileHandler;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

public class StaffRepository {
    private final FileHandler fileHandler;
    private List<Coach> coachList;

    public StaffRepository(FileHandler filehandler) {
        this.coachList = filehandler.loadCoaches();
        this.fileHandler = filehandler;
    }

    public Coach findCoachById(int coachId) {
        Optional<Coach> coach = coachList.stream()
                .filter(c -> c.getCoachId() == coachId)
                .findFirst();
        return coach.orElse(null); // Return null if the member is not found
    }

    public Coach findCoachByTeamName (String teamName) {
        for (Coach coach : coachList) {
            if (coach.getTeamName().equalsIgnoreCase(teamName)) {
                return coach;
            }
        }
        return null;
    }
    public List<Coach> getCoachList () {
        return this.coachList;
    }

    public List<Coach> findAll() {
        return coachList;
    }

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


    public boolean deleteCoach(Coach coach) {
coachList.remove(coach);
        fileHandler.deleteCoach(coach); // Delete the member using the file handler
        return true; // Assume successful deletion
    }

    public void addCoach (Coach coach) {
        coachList.add(coach);
        fileHandler.saveCoaches(coachList);
    }
}
