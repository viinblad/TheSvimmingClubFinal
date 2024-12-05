package swimclub.models;

public class TrainingResults {
    private Member member;
    private ActivityType activityType;
    private int length;
    private double time;
    private String date;
    private MembershipLevel level;

    // Training results constructor
    public TrainingResults(Member member,MembershipLevel level, ActivityType activityType,int length, double time, String date){
        this.member = member;
        this.activityType = activityType;
        this.time = time;
        this.length = length;
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
    public int getLength(){
        return length;
    }

    public MembershipLevel getLevel() {
        return level;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setLevel(MembershipLevel level) {
        this.level = level;
    }

    public String toString(){
        return "Member:" + member.getName() + " Level:" + level + " Discipline:" + activityType + " Length:" + length + " Time:" + time + " Date:" + date;
    }
}
