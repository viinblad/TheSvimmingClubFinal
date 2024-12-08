package swimclub.models;

public class Coach extends Staff {
    private int coachId; // Unique identifier for the coach
    private String teamName; // The team that the coach is assigned to

    /**
     * Constructor for the Coach class.
     * Initializes the coach's ID, team, and the common attributes from the Staff class.
     *
     * @param coachId     Unique identifier for the coach (e.g., "C123")
     * @param teamName    The team that this coach manages or is assigned to
     * @param name        The name of the coach
     * @param email       The email address of the coach
     * @param city        The city where the coach resides
     * @param street      The street address of the coach
     * @param region      The region of the coach's address
     * @param zipcode     The zipcode for the coach's address
     * @param age         The age of the coach
     * @param phoneNumber The phone number of the coach
     */
    public Coach(int coachId, String teamName, String name, String email, String city, String street, String region, int zipcode, int age, int phoneNumber) {
        // Call the superclass constructor (Staff) to initialize the shared attributes
        super(name, email, city, street, region, zipcode, age, phoneNumber);

        // Initialize the coachId and team specific to the Coach class
        this.coachId = coachId;
        this.teamName = teamName;
    }

    /**
     * Gets the coach's unique ID.
     *
     * @return The coach's ID (e.g., "C123")
     */
    public int getCoachId() {
        return this.coachId;
    }

    /**
     * Gets the team assigned to this coach.
     *
     * @return The Team object associated with the coach
     */
    public String getTeamName() {
        return this.teamName;
    }

    // Getters for all the attributes from the parent class (Staff)

    /**
     * Gets the name of the coach.
     *
     * @return The name of the coach
     */


    /**
     * Gets the email address of the coach.
     *
     * @return The email address of the coach
     */
    public String getEmail() {
        return super.getEmail();
    }

    /**
     * Gets the city where the coach resides.
     *
     * @return The city of the coach's residence
     */
    public String getCity() {
        return super.getCity();
    }

    /**
     * Gets the street address of the coach.
     *
     * @return The street address of the coach
     */
    public String getStreet() {
        return super.getStreet();
    }

    /**
     * Gets the region of the coach's address.
     *
     * @return The region of the coach's address
     */
    public String getRegion() {
        return super.getRegion();
    }

    /**
     * Gets the zipcode for the coach's address.
     *
     * @return The zipcode of the coach's address
     */
    public int getZipcode() {
        return super.getZipcode();
    }

    /**
     * Gets the age of the coach.
     *
     * @return The age of the coach
     */
    public int getAge() {
        return super.getAge();
    }

    /**
     * Gets the phone number of the coach.
     *
     * @return The phone number of the coach
     */
    public int getPhoneNumber() {
        return super.getPhoneNumber();
    }

    public void setCoachId(int newCoachId) {
        this.coachId = newCoachId;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    @Override
    public String toString() {
        return "Coach: " + getName() + ", " +  // Coach's name
                "Id: " + getCoachId() + ", " +  // Coach ID
                "Team: " + (getTeamName() != null ? getTeamName() : "No team") + ", " +  // Coach's team name or "No team"
                "Age: " + getAge() + ", " +  // Coach's age
                "Phone Number: " + getPhoneNumber();  // Coach's phone number
    }
}