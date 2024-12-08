package swimclub.models;

public class Coach extends Staff {
    private int coachId; // Unique identifier for the coach
    private String teamName; // The team that the coach is assigned to

    /**
     * Constructor for the Coach class.
     *
     * @param coachId     Unique identifier for the coach.
     * @param teamName    The team that this coach manages or is assigned to.
     * @param name        The name of the coach.
     * @param email       The email address of the coach.
     * @param city        The city where the coach resides.
     * @param street      The street address of the coach.
     * @param region      The region of the coach's address.
     * @param zipcode     The zipcode for the coach's address.
     * @param age         The age of the coach.
     * @param phoneNumber The phone number of the coach.
     * @param role        The role of the coach (e.g., COACH).
     */
    public Coach(int coachId, String teamName, String name, String email, String city, String street, String region,
                 int zipcode, int age, int phoneNumber, Role role) {
        // Call the superclass constructor (Staff) to initialize shared attributes
        super(coachId, name, email, city, street, region, zipcode, age, phoneNumber, role);
        this.coachId = coachId;
        this.teamName = teamName;
    }

    // Getters and Setters

    /**
     * Gets the coach's unique ID.
     *
     * @return The coach's ID.
     */
    public int getCoachId() {
        return this.coachId;
    }

    /**
     * Sets the coach's unique ID.
     *
     * @param coachId The new coach ID.
     */
    public void setCoachId(int coachId) {
        this.coachId = coachId;
    }

    /**
     * Gets the team assigned to this coach.
     *
     * @return The name of the team assigned to the coach.
     */
    public String getTeamName() {
        return this.teamName;
    }

    /**
     * Sets the team assigned to this coach.
     *
     * @param teamName The new team name assigned to the coach.
     */
    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "Coach: " + getName() + ", " +
                "ID: " + getCoachId() + ", " +
                "Team: " + (getTeamName() != null ? getTeamName() : "No team") + ", " +
                "Age: " + getAge() + ", " +
                "Phone Number: " + getPhoneNumber() + ", " +
                "Role: " + getRole();
    }
}
