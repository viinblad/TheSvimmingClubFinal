package swimclub.models;

public class TrainingResults {
    private Member member;
    private ActivityType activityType;
    private double time;
    private String date;
    private MembershipLevel level;

    // Training results constructor
    public TrainingResults(Member member,MembershipLevel level, ActivityType activityType, double time, String date){
        this.member = member;
        this.activityType = activityType;
        this.time = time;
        this.date = date;
        this.level = level;
    }
    // Getters and setters
    public Member getMember(){
        return member;
    }
    public ActivityType getActivityType(){
        return activityType;
    }

    public String getDate() {
        return date;
    }

    public double getTime() {
        return time;
    }

    public MembershipLevel getLevel() {
        return level;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public void setLevel(MembershipLevel level) {
        this.level = level;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String toString(){
        return "MemberID:" + member.getMemberId() + " Name:" + member.getName() + " Level:" + level + " Discipline:" + activityType + " Time:" + time + " Date:" + date;
    }
}
