package swimclub.models;

public class ActivityTypeData {
    private ActivityType type;

    public ActivityTypeData(ActivityType type){
        this.type = type;
    }

    public ActivityType type() {
        return type;
    }

    public void setType(ActivityType type) {
        this.type = type;
    }

    public ActivityType toActivityType(){
        return this.type;
    }
    @Override
    public String toString() {
        return type.name();
    }

    public static ActivityTypeData fromString(String activityTypeStr){
        if(activityTypeStr == null || activityTypeStr.isBlank()){
            throw new IllegalArgumentException("Activitytype cannot be empty.");
        }
        String[] parts = activityTypeStr.split(" ");

        if (parts.length != 1) {
            throw new IllegalArgumentException("Invalid activitytype format:" + activityTypeStr);
        }

        ActivityType activityType = ActivityType.valueOf(parts[0].toUpperCase());

        return new ActivityTypeData(activityType);
    }
}
