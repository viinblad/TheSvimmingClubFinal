package swimclub.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Team in the swim club.
 * A Team contains a list of Members assigned to it.
 */
public class Team {
    private String teamName;
    private Member teamLeader; // Optional: Define a team leader
    private List<Member> members; // List of members in the team

    /**
     * Constructor for Team.
     *
     * @param teamName The name of the team.
     */
    public Team(String teamName) {
        this.teamName = teamName;
        this.members = new ArrayList<>();
    }

    /**
     * Adds a member to the team.
     *
     * @param member The member to add.
     */
    public void addMember(Member member) {
        if (!members.contains(member)) {
            members.add(member);
        }
    }

    /**
     * Removes a member from the team.
     *
     * @param member The member to remove.
     */
    public void removeMember(Member member) {
        members.remove(member);
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
     * @param leader The member to assign as the team leader.
     */
    public void setTeamLeader(Member leader) {
        if (members.contains(leader)) {
            this.teamLeader = leader;
        } else {
            throw new IllegalArgumentException("Team leader must be a member of the team.");
        }
    }

    /**
     * Gets the team leader.
     *
     * @return The team leader, or null if not assigned.
     */
    public Member getTeamLeader() {
        return teamLeader;
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
     * @param teamName The name of the team.
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "Team: " + teamName + ", Leader: " + (teamLeader != null ? teamLeader.getName() : "None") +
                ", Members: " + members.size();
    }
}
