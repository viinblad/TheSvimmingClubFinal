package swimclub.models;

public class CompetitionResults {
    private Member member;
    private String event;
    private int placement;
    private double time;
    private String date;
    private MembershipLevel level;
    private ActivityType activityType;

    // Competition results constructor
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
    public Member getMember() {
        return member;
    }

    public String getEvent() {
        return event;
    }

    public int getPlacement() {
        return placement;
    }

    public double getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public MembershipLevel getLevel() {
        return level;
    }

    public ActivityType getActivityType() {
        return activityType;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public void setPlacement(int placement) {
        this.placement = placement;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLevel(MembershipLevel level) {
        this.level = level;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    @Override
    public String toString(){
        return "Member:" + member.getName() + " Level:" + level + " Discipline:" + activityType + " Time:" + time + " Date:" + date;
    }
}
