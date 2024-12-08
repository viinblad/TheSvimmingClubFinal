package swimclub.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Team in the swim club.
 * A Team contains a list of Members assigned to it.
 */
public class Team {
    private String teamName; // Name of the team
    private TeamType teamType; // Type of the team (Junior Competitive, Senior Competitive)
    private Coach coach;
    private List<Member> members; // List of members in the team

    /**
     * Constructor for creating a team.
     *
     * @param teamName The name of the team.
     * @param teamType The type of the team.
     */
    public Team(String teamName, TeamType teamType, Coach coach) {
        this.teamName = teamName;
        this.teamType = teamType;
        this.members = new ArrayList<>();
        this.coach = coach;
    }

    /**
     * Adds a member to the team.
     *
     * @param member The member to add.
     */
    public void addMember(Member member) {
        if (!members.contains(member)) {
            members.add(member);
        } else {
            throw new IllegalArgumentException("Member is already part of the team.");
        }
    }

    /**
     * Removes a member from the team.
     *
     * @param member The member to remove.
     */
    public void removeMember(Member member) {
        if (members.contains(member)) {
            members.remove(member);
        } else {
            throw new IllegalArgumentException("Member is not part of the team.");
        }
    }

    /**
     * Gets the list of members in the team.
     *
     * @return List of members.
     */
    public List<Member> getMembers() {
        return new ArrayList<>(members);
    }

    /**
     * Sets the team leader.
     *
     * @param coach The member to assign as the team leader.
     * @throws IllegalArgumentException if the leader is not a member of the team.
     */
    public void setTeamCoach(Coach coach) {
            this.coach = coach;
    }

    /**
     * Gets the team leader.
     *
     * @return The team leader, or null if not assigned.
     */
    public Coach getTeamCoach() {
        return coach;
    }

    /**
     * Gets the team name.
     *
     * @return The team name.
     */
    public String getTeamName() {
        return teamName;
    }

    /**
     * Sets the team name.
     *
     * @param teamName The new name of the team.
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    /**
     * Gets the team type.
     *
     * @return The type of the team.
     */
    public TeamType getTeamType() {
        return teamType;
    }

    /**
     * Sets the team type.
     *
     * @param teamType The new type of the team.
     */
    public void setTeamType(TeamType teamType) {
        this.teamType = teamType;
    }

    @Override
    public String toString() {
        return "Team: " + teamName +
                " (" + teamType.getDisplayName() + ")" +
                ", Coach: " + (coach != null ? coach.getName() : "None") +
                ", Members: " + members.size();
    }
}
