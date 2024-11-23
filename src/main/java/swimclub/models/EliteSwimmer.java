package swimclub.models;

public class EliteSwimmer extends Member {
    ActivityType activityType;

    //Team team;
    //BestResult bestResult;
    //Coach coach

    public EliteSwimmer (String memberId, String name, String email, int age, int phoneNumber, ActivityType activityType) {
        super(memberId, name, email, MembershipType.ELITESWIMMER, age, phoneNumber); //adds eliteswimmer as a default value for membershipType.
        this.activityType = activityType;
    }

}
