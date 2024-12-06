package swimclub.models;

import javax.management.relation.Role;

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
    private Role role;           // Roles for the staff

    /**
     * Constructor to initialize the details of a Staff member.
     * This constructor initializes all attributes of the Staff class.
     *
     * @param name        The name of the staff member
     * @param email       The email address of the staff member
     * @param city        The city where the staff member resides
     * @param street      The street address of the staff member
     * @param region      The region or state where the staff member lives
     * @param zipcode     The zip code of the staff member's address
     * @param age         The age of the staff member
     * @param phoneNumber The phone number of the staff member
     * @param role        The admin role access for the staff
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
    public Role getId() {
        return role;
    }
    /**
     * Gets the name of the staff member.
     *
     * @return The role of the staff member
     */
    public Role getRole() {
        return role;
    }

    /**
     * Gets the name of the staff member.
     *
     * @return The name of the staff member
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the email address of the staff member.
     *
     * @return The email address of the staff member
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Gets the city where the staff member resides.
     *
     * @return The city where the staff member resides
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Gets the street address of the staff member.
     *
     * @return The street address of the staff member
     */
    public String getStreet() {
        return this.street;
    }

    /**
     * Gets the region or state of the staff member's address.
     *
     * @return The region or state of the staff member
     */
    public String getRegion() {
        return this.region;
    }

    /**
     * Gets the zip code for the staff member's address.
     *
     * @return The zip code of the staff member's address
     */
    public int getZipcode() {
        return this.zipcode;
    }

    /**
     * Gets the age of the staff member.
     *
     * @return The age of the staff member
     */
    public int getAge() {
        return this.age;
    }

    /**
     * Gets the phone number of the staff member.
     *
     * @return The phone number of the staff member
     */
    public int getPhoneNumber() {
        return this.phoneNumber;
    }

    // Setters
    /**
     * Sets the role of the staff member.
     *
     * @param role The role of the staff member
     */
    public void setRole(Role role) {
        this.role = role;
    }
}