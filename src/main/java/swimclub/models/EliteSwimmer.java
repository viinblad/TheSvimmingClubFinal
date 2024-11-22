package swimclub.models;

public class EliteSwimmer extends Member {
    ActivityType activityType;

    //Team team;
    //BestResult bestResult;
    //Coach coach

    public EliteSwimmer (String memberId, String name, String email, int age, int phoneNumber, boolean active, ActivityType activityType) {
        super(memberId, name, email, age, phoneNumber, active);
        this.activityType = activityType;
    }

}
