package swimclub.models;

/**
 * Abstract base class for staff members in the swim club.
 */
public abstract class Staff {
    private int id;              // Staff ID
    private String name;         // Name of the staff member
    private String email;        // Email address of the staff member
    private String city;         // City where the staff member resides
    private String street;       // Street address of the staff member
    private String region;       // Region or state of the staff member
    private int zipcode;         // Zip code for the staff member's address
    private int age;             // Age of the staff member
    private int phoneNumber;     // Phone number of the staff member
    private Role role;           // Role for the staff

    /**
     * Constructor to initialize the details of a Staff member.
     *
     * @param id          Unique ID of the staff member
     * @param name        The name of the staff member
     * @param email       The email address of the staff member
     * @param city        The city where the staff member resides
     * @param street      The street address of the staff member
     * @param region      The region or state where the staff member lives
     * @param zipcode     The zip code of the staff member's address
     * @param age         The age of the staff member
     * @param phoneNumber The phone number of the staff member
     * @param role        The role of the staff member
     */
    public Staff(int id, String name, String email, String city, String street, String region, int zipcode, int age, int phoneNumber, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.city = city;
        this.street = street;
        this.region = region;
        this.zipcode = zipcode;
        this.age = age;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }

    // Getters

    /**
     * Gets the ID of the staff member.
     *
     * @return The ID of the staff member
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the name of the staff member.
     *
     * @return The name of the staff member
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the email address of the staff member.
     *
     * @return The email address of the staff member
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the city where the staff member resides.
     *
     * @return The city where the staff member resides
     */
    public String getCity() {
        return city;
    }

    /**
     * Gets the street address of the staff member.
     *
     * @return The street address of the staff member
     */
    public String getStreet() {
        return street;
    }

    /**
     * Gets the region or state of the staff member's address.
     *
     * @return The region or state of the staff member's address
     */
    public String getRegion() {
        return region;
    }

    /**
     * Gets the zip code for the staff member's address.
     *
     * @return The zip code of the staff member's address
     */
    public int getZipcode() {
        return zipcode;
    }

    /**
     * Gets the age of the staff member.
     *
     * @return The age of the staff member
     */
    public int getAge() {
        return age;
    }

    /**
     * Gets the phone number of the staff member.
     *
     * @return The phone number of the staff member
     */
    public int getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets the role of the staff member.
     *
     * @return The role of the staff member
     */
    public Role getRole() {
        return role;
    }

    // Setters

    /**
     * Sets the ID of the staff member.
     *
     * @param id The unique ID of the staff member
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the role of the staff member.
     *
     * @param role The role of the staff member
     */
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", region='" + region + '\'' +
                ", zipcode=" + zipcode +
                ", age=" + age +
                ", phoneNumber=" + phoneNumber +
                ", role=" + role +
                '}';
    }
}
