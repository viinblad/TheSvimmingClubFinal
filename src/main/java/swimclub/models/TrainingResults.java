package swimclub.models;

/**
 * Represents the results of a training session for a member.
 * This class stores information about the member, their activity type, time spent, and the training session details.
 */
public class TrainingResults {
    private Member member;
    private ActivityType activityType;
    private double time;
    private String date;
    private MembershipLevel level;

    /**
     * Constructor to initialize a training result.
     *
     * @param member       The member who participated in the training session.
     * @param level        The membership level of the member.
     * @param activityType The type of activity performed during the training (e.g., swimming, running).
     * @param time         The time the member spent on the activity during the training.
     * @param date         The date when the training session took place.
     */
    public TrainingResults(Member member, MembershipLevel level, ActivityType activityType, double time, String date) {
        this.member = member;
        this.activityType = activityType;
        this.time = time;
        this.date = date;
        this.level = level;
    }

    // Getters and setters

    /**
     * Gets the member associated with the training result.
     *
     * @return The member.
     */
    public Member getMember() {
        return member;
    }

    /**
     * Gets the type of activity performed during the training.
     *
     * @return The activity type (e.g., swimming, running).
     */
    public ActivityType getActivityType() {
        return activityType;
    }

    /**
     * Gets the date of the training session.
     *
     * @return The date of the training session.
     */
    public String getDate() {
        return date;
    }

    /**
     * Gets the time the member spent on the activity during the training.
     *
     * @return The time in seconds.
     */
    public double getTime() {
        return time;
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
     * Sets the activity type for the training session.
     *
     * @param activityType The type of activity performed.
     */
    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    /**
     * Sets the member associated with the training result.
     *
     * @param member The member.
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * Sets the time the member spent on the activity during the training.
     *
     * @param time The time in seconds.
     */
    public void setTime(double time) {
        this.time = time;
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
     * Sets the date of the training session.
     *
     * @param date The date of the training session.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Returns a string representation of the training result.
     * This includes the member's ID, name, membership level, activity type, time, and date.
     *
     * @return A string representation of the training result.
     */
    @Override
    public String toString() {
        return "MemberID:" + member.getMemberId() + " Name:" + member.getName() + " Level:" + level + " Discipline:" + activityType + " Time:" + time + " Date:" + date;
    }
}
