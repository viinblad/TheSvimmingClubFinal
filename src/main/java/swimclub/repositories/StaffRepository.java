package swimclub.repositories;

import swimclub.models.Coach;
import swimclub.models.Member;
import swimclub.models.Staff;
import swimclub.services.StaffService;
import swimclub.services.TeamService;
import swimclub.utilities.FileHandler;

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
        // If there are no members, return 1 as the first member ID
        if (coachList.isEmpty()) {
            return 1; // If no members, the first ID is 1
        }

        // Find the highest member ID
        int maxId = coachList.stream()
                .mapToInt(Coach::getCoachId)
                .max()
                .orElse(0); // Find the highest ID, default to 0 if no members

        return maxId + 1; // Return the next ID
    }

    public boolean deleteCoach(Coach coach) {

        fileHandler.deleteCoach(coach); // Delete the member using the file handler
        return true; // Assume successful deletion
    }

    public void addCoach (Coach coach) {
        coachList.add(coach);
    }
}
