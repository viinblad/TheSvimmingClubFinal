package swimclub.models;

/**
 * Represents the results of a competition for a member.
 * This class stores information about the member, their performance, and the competition details.
 */
public class CompetitionResults {
    private Member member;
    private String event;
    private int placement;
    private double time;
    private String date;
    private MembershipLevel level;
    private ActivityType activityType;

    /**
     * Constructor to initialize a competition result.
     *
     * @param member       The member who participated in the competition.
     * @param level        The membership level of the member.
     * @param event        The name of the competition event.
     * @param placement    The placement achieved by the member in the competition.
     * @param time         The time the member spent during the competition.
     * @param date         The date when the competition took place.
     * @param activityType The type of activity (e.g., swimming, running) for the competition.
     */
    public CompetitionResults(Member member, MembershipLevel level, String event, int placement, double time, String date, ActivityType activityType) {
        this.member = member;
        this.event = event;
        this.placement = placement;
        this.time = time;
        this.date = date;
        this.level = level;
        this.activityType = activityType;
    }

    // Getters and setters

    /**
     * Gets the member associated with the competition result.
     *
     * @return The member.
     */
    public Member getMember() {
        return member;
    }

    /**
     * Gets the event name of the competition.
     *
     * @return The event name.
     */
    public String getEvent() {
        return event;
    }

    /**
     * Gets the placement achieved by the member.
     *
     * @return The placement.
     */
    public int getPlacement() {
        return placement;
    }

    /**
     * Gets the time the member took in the competition.
     *
     * @return The time in seconds.
     */
    public double getTime() {
        return time;
    }

    /**
     * Gets the date of the competition.
     *
     * @return The competition date.
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the membership level of the member.
     *
     * @return The membership level.
     */
    public MembershipLevel getLevel() {
        return level;
    }

    /**
     * Gets the type of activity for the competition.
     *
     * @return The activity type.
     */
    public ActivityType getActivityType() {
        return activityType;
    }

    /**
     * Sets the member associated with the competition result.
     *
     * @param member The member.
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * Sets the event name of the competition.
     *
     * @param event The event name.
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * Sets the placement achieved by the member.
     *
     * @param placement The placement.
     */
    public void setPlacement(int placement) {
        this.placement = placement;
    }

    /**
     * Sets the time the member took in the competition.
     *
     * @param time The time in seconds.
     */
    public void setTime(double time) {
        this.time = time;
    }

    /**
     * Sets the date of the competition.
     *
     * @param date The competition date.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Sets the membership level of the member.
     *
     * @param level The membership level.
     */
    public void setLevel(MembershipLevel level) {
        this.level = level;
    }

    /**
     * Sets the type of activity for the competition.
     *
     * @param activityType The activity type.
     */
    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    /**
     * Returns a string representation of the competition result.
     * This includes member name, level, activity type, time, and date.
     *
     * @return A string representation of the competition result.
     */
    @Override
    public String toString(){
        return "Member:" + member.getName() + " Level:" + level + " Discipline:" + activityType + " Time:" + time + " Date:" + date;
    }
}
