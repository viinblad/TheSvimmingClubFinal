package swimclub.models;

/**
 * A wrapper class for handling ActivityType data.
 */
public class ActivityTypeData {
    private ActivityType type;
    /**
     * Constructor for ActivityTypeData.
     *
     * @param type The ActivityType enum to initialize the object with.
     */
    public ActivityTypeData(ActivityType type){
        this.type = type;
    }
    /**
     * Retrieves the current ActivityType.
     *
     * @return The current ActivityType of this object.
     */
    public ActivityType type() {
        return type;
    }
    /**
     * Updates the ActivityType of this object.
     *
     * @param type The new ActivityType to set.
     */
    public void setType(ActivityType type) {
        this.type = type;
    }
    /**
     * Converts this object to its ActivityType representation.
     *
     * @return The ActivityType value of this object.
     */
    public ActivityType toActivityType(){
        return this.type;
    }
    /**
     * Converts the ActivityType to its string representation.
     *
     * @return The string representation of the current ActivityType.
     */
    @Override
    public String toString() {
        return type.name();
    }

    /**
     * Creates an ActivityTypeData object from a string.
     *
     * @param activityTypeStr The string representation of the ActivityType.
     *                        This should match the name of a valid ActivityType enum.
     * @return A new ActivityTypeData object based on the given string.
     * @throws IllegalArgumentException If the string is null, blank, or does not match a valid ActivityType format.
     */
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
