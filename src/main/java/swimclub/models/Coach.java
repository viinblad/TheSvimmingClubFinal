package swimclub.models;

public abstract class Coach {
    private String coachId;
    private Team team;
    private String name;
    private String email;
    private String city;
    private String street;
    private String region;
    private int zipcode;
    private int age;
    private int phoneNumber;


    public Coach(String coachId, Team team, String name, String email, String city, String street, String region, int zipcode, int age, int phoneNumber) {
       this.coachId = coachId;
       this.name = name;
       this.email = email;
       this.city = city;
       this.street = street;
       this.region = region;
       this.zipcode = zipcode;
       this.age = age;
       this.phoneNumber = phoneNumber;
    }
}
