package swimclub.utilities;

import swimclub.controllers.TeamController;
import swimclub.models.*;
import swimclub.repositories.MemberRepository;
import swimclub.repositories.StaffRepository;
import swimclub.repositories.TeamRepository;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * FileHandler handles saving and loading Member, Payment, Reminder, and Team data to and from a file.
 */
public class FileHandler {
    private String memberFilePath;
    private String paymentFilePath;
    private String reminderFilePath;
    private String paymentRatesFilePath;
    private String teamsFilePath;
    private String staffFilePath;
    private String competitionResultsFilePath;
    private String trainingResultsFilePath;

    /**
     * Constructor for FileHandler.
     *
     * @param memberFilePath       Path to the file for saving/loading member data.
     * @param paymentFilePath      Path to the file for saving/loading payment data.
     * @param reminderFilePath     Path to the file for saving/loading reminder data.
     * @param paymentRatesFilePath Path to the file for saving/loading payment rates.
     * @param teamFilePath         Path to the file for saving/loading team data.
     */
    public FileHandler(String memberFilePath, String paymentFilePath, String reminderFilePath,
                       String paymentRatesFilePath, String teamFilePath, String competitionResultsFilePath, String staffFilePath, String trainingResultsFilePath) {

        this.memberFilePath = memberFilePath;
        this.paymentFilePath = paymentFilePath;
        this.reminderFilePath = reminderFilePath;
        this.paymentRatesFilePath = paymentRatesFilePath;
        this.teamsFilePath = teamFilePath;
        this.staffFilePath = staffFilePath;
        this.competitionResultsFilePath = competitionResultsFilePath;
        this.trainingResultsFilePath = trainingResultsFilePath;
    }

    // ---------------------------
    // Member Related Methods
    // ---------------------------

    /**
     * Saves all members to the specified file.
     *
     * @param members List of Member objects to save.
     */
    public void saveMembers(List<Member> members) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(memberFilePath))) {
            for (Member member : members) {
                writer.write(formatMember(member)); // Format and save each member
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving members: " + e.getMessage());
        }
    }

    /**
     * Loads members from the specified file.
     *
     * @return List of Member objects loaded from the file.
     */
    public List<Member> loadMembers() {
        List<Member> members = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(memberFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Member member = parseMember(line);
                    if (member != null) {
                        // Ensure the team name is not null or empty
                        if (member.getTeamName() == null || member.getTeamName().isEmpty()) {
                            member.setTeamName("No team");  // Set "No team" if it's missing
                        }
                        members.add(member);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading members: " + e.getMessage());
        }
        return members;
    }

    /**
     * Deletes a member from the file based on provided memberID.
     *
     * @param memberToDelete The member to delete.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteMember(Member memberToDelete) {
        boolean memberDeleted = false;
        List<Member> members = loadMembers(); // Load all members from file

        List<Member> updatedMembers = new ArrayList<>();
        int id = memberToDelete.getMemberId(); // Get the ID for the member to delete

        for (Member member : members) {
            if (member.getMemberId() == id) {
                memberDeleted = true; // Mark the member as deleted
            } else {
                updatedMembers.add(member); // Keep all other members
            }
        }

        if (memberDeleted) {
            saveMembers(updatedMembers); // Save the updated list without the deleted member
        }
        return memberDeleted;
    }

    // ---------------------------
    // Payment Related Methods
    // ---------------------------

    /**
     * Saves all payments to a file.
     *
     * @param payments List of Payment objects.
     * @param filePath Path to the payment file.
     */
    public void savePayments(List<Payment> payments, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Payment payment : payments) {
                writer.write(formatPayment(payment));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving payments: " + e.getMessage());
        }
    }

    /**
     * Loads payments from a file.
     *
     * @param filePath         Path to the payment file.
     * @param memberRepository Repository to link payments to members.
     * @return List of Payment objects.
     */
    public List<Payment> loadPayments(String filePath, MemberRepository memberRepository) {
        List<Payment> payments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Payment payment = parsePayment(line, memberRepository);
                if (payment != null) {
                    payments.add(payment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading payments: " + e.getMessage());
        }
        return payments;
    }

    // ---------------------------
    // Reminder Related Methods
    // ---------------------------

    /**
     * Saves all reminders to the specified file.
     *
     * @param reminders List of reminders to save.
     */
    public void saveReminders(List<String> reminders) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reminderFilePath))) {
            for (String reminder : reminders) {
                writer.write(reminder);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving reminders: " + e.getMessage());
        }
    }

    /**
     * Loads reminders from the specified file.
     *
     * @return List of reminders loaded from the file.
     */
    public List<String> loadReminders() {
        List<String> reminders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(reminderFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    reminders.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading reminders: " + e.getMessage());
        }
        return reminders;
    }

    /**
     * Deletes a specific reminder from the file.
     *
     * @param reminder The reminder to delete.
     * @return true if deletion was successful, false otherwise.
     */
    public boolean deleteReminder(String reminder) {
        List<String> reminders = loadReminders();
        boolean reminderDeleted = reminders.remove(reminder);

        if (reminderDeleted) {
            saveReminders(reminders);
        }
        return reminderDeleted;
    }

    // ---------------------------
    // Helper Methods
    // ---------------------------

    private String formatMember(Member member) {
        String teamName = (member.getTeamName() != null && !member.getTeamName().isEmpty())
                ? member.getTeamName()
                : "No team";  // If teamName is null or empty, use "No team"

        return member.getMemberId() + ";" +
                member.getName() + ";" +
                member.getEmail() + ";" +
                member.getCity() + ";" +
                member.getStreet() + ";" +
                member.getRegion() + ";" +
                member.getZipcode() + ";" +
                member.getAge() + ";" +
                member.getPhoneNumber() + ";" +
                member.getMembershipType().getLevel() + " " +
                member.getMembershipType().getCategory() + ";" +
                member.getMembershipStatus() + ";" +
                member.getActivityType() + ";" +
                member.getPaymentStatus() + ";" +
                teamName;  // Use the teamName or "No team" if null/empty
    }
   // String teamName, TeamType teamType, Coach coach
    /**
     * Parses a line of text into a Member object (Junior or Senior).
     *
     * @param line A semicolon-separated string representing member details.
     * @return A Member object if parsing is successful; otherwise, null.
     */
    private Member parseMember(String line) {
        // Split the line into parts using semicolon as a delimiter
        String[] parts = line.split(";");

        // Validate that the line has at least 13 fields (excluding optional team name)
        if (parts.length < 13) {
            System.err.println("Skipping invalid member data: " + line);
            return null;
        }

        try {
            // Parse basic member details
            int id = Integer.parseInt(parts[0]);
            String name = parts[1];
            String email = parts[2];
            String city = parts[3];
            String street = parts[4];
            String region = parts[5];
            int zipcode = Integer.parseInt(parts[6]);
            int age = Integer.parseInt(parts[7]);
            int phoneNumber = Integer.parseInt(parts[8]);

            // Parse membership type (e.g., "SENIOR COMPETITIVE")
            String membershipDescription = parts[9];
            String[] membershipParts = membershipDescription.split(" ");

            // Construct MembershipType from parsed values
            MembershipType membershipType = new MembershipType(
                    MembershipCategory.valueOf(membershipParts[1].toUpperCase()), // COMPETITIVE/RECREATIONAL
                    MembershipLevel.valueOf(membershipParts[0].toUpperCase())    // JUNIOR/SENIOR
            );

            // Parse membership status (ACTIVE/INACTIVE), activity type, and payment status
            MembershipStatus membershipStatus = MembershipStatus.valueOf(parts[10].toUpperCase());
            ActivityType activityType = ActivityType.valueOf(parts[11].toUpperCase());
            PaymentStatus paymentStatus = PaymentStatus.valueOf(parts[12].toUpperCase());

            // Extract team name, which can be null or empty
            String teamName = (parts.length > 13 && !parts[13].trim().isEmpty()) ? parts[13].trim() : "No team";

            // Validate the parsed data using a Validator utility
            Validator.validateMemberData(name, age, membershipDescription, email, city, street, region, zipcode,
                    phoneNumber, membershipStatus, activityType.toString(), paymentStatus);

            // Create the appropriate subclass of Member based on membership level (Junior/Senior)
            if (membershipType.getLevel() == MembershipLevel.JUNIOR) {
                return new JuniorMember(
                        String.valueOf(id), name, email, city, street, region, zipcode, membershipType,
                        membershipStatus, activityType, paymentStatus, age, phoneNumber, teamName
                );
            } else {
                return new SeniorMember(
                        String.valueOf(id), name, email, city, street, region, zipcode, membershipType,
                        membershipStatus, activityType, paymentStatus, age, phoneNumber, teamName
                );
            }

        } catch (Exception e) {
            // Log an error if parsing fails and return null
            System.err.println("Error parsing member: " + line + " - " + e.getMessage());
            return null;
        }
    }


    private String formatPayment(Payment payment) {
        return payment.getPaymentId() + ";" +
                payment.getMember().getMemberId() + ";" +
                payment.getAmountPerYear() + ";" +
                payment.getPaymentDate() + ";" +
                payment.getPaymentStatus();
    }

    private Payment parsePayment(String line, MemberRepository memberRepository) {
        String[] parts = line.split(";");
        try {
            int paymentId = Integer.parseInt(parts[0]);
            int memberId = Integer.parseInt(parts[1]);
            double amount = Double.parseDouble(parts[2]);
            LocalDate paymentDate = LocalDate.parse(parts[3]);
            PaymentStatus status = PaymentStatus.valueOf(parts[4].toUpperCase());
            Member member = memberRepository.findById(memberId);

            return new Payment(paymentId, status, member, paymentDate, amount);
        } catch (Exception e) {
            System.err.println("Error parsing payment: " + line + " - " + e.getMessage());
            return null;
        }
    }


    /**
     * @return a double arrayList which can be used in paymentService to load juniorRate and seniorRate.
     */
    public double[] loadPaymentRates() {
        double[] rates = new double[2];
        try (BufferedReader reader = new BufferedReader(new FileReader(paymentRatesFilePath))) {
            String line;
            boolean foundJuniorRate = false;
            boolean foundSeniorRate = false;

            while ((line = reader.readLine()) != null) { // proceeds until the paymentRates.dat is read to the bottom.
                line = line.trim(); // cleans whitespaces in the document
                if (line.startsWith("Junior Rate:")) {
                    try {
                        rates[0] = Double.parseDouble(line.split(":")[1].trim()); /*turns the data into double and splits it
                        into an array before and after ":" and then takes the second element */
                        foundJuniorRate = true;
                    } catch (NumberFormatException e) { // in case that the string can't be converted to a double.
                        System.out.println("Invalid format for Junior Rate. Using default value.");
                    }
                } else if (line.startsWith("Senior Rate:")) {
                    try {
                        rates[1] = Double.parseDouble(line.split(":")[1].trim()); /*turns the data into double and splits it
                        into an array before and after ":" and then takes the second element */
                        foundSeniorRate = true;
                    } catch (NumberFormatException e) {  // in case that the string can't be converted to a double.
                        System.out.println("Invalid format for Senior Rate. Using default value.");
                    }
                }
            }

            // Set default values if rates were not found
            if (!foundJuniorRate) {
                rates[0] = 1000; // Default Junior rate if no rates were found in the document.
            }
            if (!foundSeniorRate) {
                rates[1] = 1600; // Default Senior rate if no rates were found in the document.
            }

        } catch (IOException e) {
            System.out.println("Error loading payment rates from file: " + e.getMessage());
            // Default values in case of error
            rates[0] = 1000; // default junior rate
            rates[1] = 1600; // default senior rate

        }
        return rates;
    }

    /**
     * This method saves the juniorRate and seniorRate from paymentService class to paymentRates.dat document.
     *
     * @param juniorRate - the price for how much a junior member has to pay.
     * @param seniorRate - the price for how much a senior member has to pay.
     */
    public void savePaymentRates(double juniorRate, double seniorRate) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(paymentRatesFilePath, false))) {
            //false means that it overwrites everything in the file every time.
            writer.write("Junior Rate: " + juniorRate);
            writer.newLine();
            writer.write("Senior Rate: " + seniorRate);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving payment rates: " + e.getMessage());
        }
    }

    // ---------------------------
    // Team Related Methods
    // ---------------------------

    /**
     * Saves all teams to the specified file.
     *
     * @param teams List of Team objects to save.
     */
    public void saveTeams(List<Team> teams) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(teamsFilePath))) {
            for (Team team : teams) {
                StringBuilder sb = new StringBuilder();
                sb.append(team.getTeamName()).append(";") // Team name
                        .append(team.getTeamType().name()).append(";"); // Team type

                // Append coach ID (or "null" if no coach)
                if (team.getTeamCoach() != null) {
                    sb.append(team.getTeamCoach().getCoachId());
                } else {
                    sb.append("null");
                }
                sb.append(";");

                // Append member IDs as a comma-separated string
                for (Member member : team.getMembers()) {
                    sb.append(member.getMemberId()).append(",");
                }

                // Remove trailing comma after last member (if any)
                if (team.getMembers().size() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }

                // Write the team data to the file
                writer.write(sb.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving teams: " + e.getMessage());
        }
    }

    /**
     * Loads teams from the specified file.
     *
     * @param allMembers List of all members to link teams with.
     * @return List of Team objects loaded from the file.
     */
    public List<Team> loadTeams(List<Member> allMembers, StaffRepository staffRepository) {
        List<Team> teams = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(teamsFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length < 3) continue; // Skip lines with insufficient data

                // Extract team data
                String teamName = parts[0];
                TeamType teamType = TeamType.valueOf(parts[1].toUpperCase()); // Assumes TeamType enum

                // Parse the coach ID safely
                int coachId = parseInteger(parts[2]); // Parsing coachId from parts[2]
                Coach coach = null;
                if (coachId != -1) {
                    // Retrieve the coach object using the parsed coachId if valid
                    coach = staffRepository.findCoachById(coachId);
                    if (coach == null) {
                        System.err.println("Coach with ID " + coachId + " not found for team " + teamName);
                    }
                } else {
                    System.out.println("No valid coach ID found for team " + teamName + ", assigning null coach.");
                }

                // Parse member IDs if any exist
                String[] memberIds = parts.length > 3 ? parts[3].split(",") : new String[0];

                // Create a new Team object with or without a coach
                Team team = new Team(teamName, teamType, coach);

                // Add members to the team
                for (String memberId : memberIds) {
                    if (!memberId.isEmpty()) {
                        Member member = findMemberById(allMembers, Integer.parseInt(memberId));
                        if (member != null) {
                            team.addMember(member);
                        }
                    }
                }

                // Add the team to the list
                teams.add(team);
            }
        } catch (IOException e) {
            System.err.println("Error loading teams: " + e.getMessage());
        }
        return teams; // Return the loaded list of teams
    }

    private int parseInteger(String value) {
        try {
            // If the value is "null" or empty, return -1 to signify invalid input
            if (value == null || value.trim().isEmpty() || value.equals("null")) {
                return -1;
            }
            return Integer.parseInt(value); // Parse the string to an integer
        } catch (NumberFormatException e) {
            System.err.println("Invalid integer value: " + value); // Log the invalid value
            return -1; // Return -1 if parsing fails
        }
    }

    /**
     * Helper method to find a member by ID from a list of members.
     *
     * @param members List of members to search in.
     * @param id      ID of the member to find.
     * @return The Member object with the specified ID, or null if not found.
     */
    private Member findMemberById(List<Member> members, int id) {
        return members.stream()
                .filter(member -> member.getMemberId() == id)
                .findFirst()
                .orElse(null);
    }

// // ---------------------------
//    // Staff Related Methods
//    // ---------------------------

    /**
     * Saves all coaches on the staff repository to a file.
     * This method will iterate through the list of coaches and write their information to the specified file.
     *
     * @param coaches A list of `Coach` objects to be saved to the file. Each `Coach` object contains the details
     *                of a coach, which will be formatted and written to the file.
     */
    public void saveCoaches(List<Coach> coaches) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(staffFilePath))) {
            // Iterate through the list of coaches and write their formatted details to the file
            for (Coach coach : coaches) {
                writer.write(formatCoach(coach)); // Format and save each coach's information
                writer.newLine(); // Add a new line after each coach's data
            }
        } catch (IOException e) {
            // Handle potential IOExceptions that could occur during the file writing process
            System.err.println("Error saving coaches: " + e.getMessage());
        }
    }

    /**
     * Loads coaches from the file into a list of `Coach` objects.
     * This method reads the file, parses each line, and adds the parsed `Coach` object to a list.
     *
     * @return A list of `Coach` objects loaded from the file. Each `Coach` object represents a coach's details
     * parsed from the file. If any errors occur during the reading process, an empty list will be returned.
     */
    public List<Coach> loadCoaches() {
        List<Coach> coaches = new ArrayList<>(); // Create an empty list to store the loaded coaches
        try (BufferedReader reader = new BufferedReader(new FileReader(staffFilePath))) {
            String line;
            // Read each line from the file until the end
            while ((line = reader.readLine()) != null) {
                // Check if the line is not empty (ignores blank lines)
                if (!line.trim().isEmpty()) {
                    Coach coach = parseCoach(line); // Parse the line to create a `Coach` object
                    if (coach != null) {
                        coaches.add(coach); // Add the parsed coach to the list
                    }
                }
            }
        } catch (IOException e) {
            // Handle potential IOExceptions that could occur during the file reading process
            System.err.println("Error loading coaches: " + e.getMessage());
        }
        return coaches; // Return the list of coaches loaded from the file
    }

    /**
     * Formats the details of a Coach object into a semicolon-separated string.
     *
     * @param coach The Coach object whose details are to be formatted.
     * @return A formatted string containing the coach's details, separated by semicolons.
     */
    private String formatCoach(Coach coach) {
        //save all necessary information in a single line, separated by semicolons
        return coach.getCoachId() + ";" +
                coach.getTeamName() + ";" +
                coach.getName() + ";" +
                coach.getEmail() + ";" +
                coach.getCity() + ";" +
                coach.getStreet() + ";" +
                coach.getRegion() + ";" +
                coach.getZipcode() + ";" +
                coach.getAge() + ";" +
                coach.getPhoneNumber();
    }

    /**
     * Parses a semicolon-separated string into a Coach object.
     *
     * @param line The string representing the coach's details, separated by semicolons.
     * @return A Coach object with the details parsed from the string, or null if parsing fails.
     */
    private Coach parseCoach(String line) {
        String[] parts = line.split(";");

        // Ensure the expected number of fields (10 fields: coachId, teamName, email, name, city, street, region, zipcode, age, phoneNumber)
        if (parts.length < 10) {
            System.err.println("Skipping invalid coach data: " + line);
            return null;  // Return null if the line doesn't have the expected number of fields
        }

        try {
            // Parse basic coach details
            int coachId = Integer.parseInt(parts[0]);                        // Coach's unique ID

            // Assuming Team is an object, you need to find the team by name or ID
            String teamName = parts[1];                                       // Team name associated with the coach
            String name = parts[2];                                        // Coach's name
            String email = parts[3];                                          // Coach's email address
            String city = parts[4];                                           // Coach's city
            String street = parts[5];                                         // Coach's street address
            String region = parts[6];                                         // Coach's region
            int zipcode = Integer.parseInt(parts[7]);                         // Coach's zipcode
            int age = Integer.parseInt(parts[8]);                             // Coach's age
            int phoneNumber = Integer.parseInt(parts[9]);                     // Coach's phone number

            // Create a new Coach object with the parsed data
            Coach coach = new Coach(coachId, teamName, name, email, city, street, region, zipcode, age, phoneNumber);
            return coach;                                                     // Return the Coach object
        } catch (Exception e) {
            System.err.println("Error parsing coach: " + line + " - " + e.getMessage());
            return null;  // Return null if any error occurs during parsing
        }
    }


    /**
     * Deletes a coach from the system.
     * This method removes a coach from the list of coaches and saves the updated list.
     * It returns true if the coach was successfully deleted, and false if no coach with the specified ID was found.
     *
     * @param coachToDelete The coach to be deleted from the system.
     * @return boolean True if the coach was deleted, false otherwise.
     */
    public boolean deleteCoach(Coach coachToDelete) {
        boolean coachDeleted = false;
        List<Coach> coachList = loadCoaches(); // Load all coaches from the file

        List<Coach> updatedCoaches = new ArrayList<>(); // List to store the updated coaches after deletion
        int id = coachToDelete.getCoachId(); // Get the ID of the coach to delete

        // Iterate through the current list of coaches
        for (Coach coach : coachList) {
            if (coach.getCoachId() != id) {
                updatedCoaches.add(coach); // Keep coaches that don't match the ID
            } else {
                coachDeleted = true; // Mark the coach as deleted
            }
        }

        // If the coach was deleted, save the updated list
        if (coachDeleted) {
            saveCoaches(updatedCoaches); // Save the updated list without the deleted coach
        }

        return coachDeleted; // Return true if coach was deleted, false if not
    }

    /**
     * Saves the list of competition results to a file.
     * The competition results are saved in a CSV-like format where each line represents a competition result.
     *
     * @param results The list of competition results to be saved.
     * @param filePath The file path where the competition results should be saved.
     */
    public void saveCompetitionResults(List<CompetitionResults> results, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (CompetitionResults result : results) {
                writer.write(result.getMember().getMemberId() + ";" +
                        result.getActivityType() + ";" +
                        result.getEvent() + ";" +
                        result.getPlacement() + ";" +
                        result.getDate() + ";" +
                        result.getTime());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving competition results: " + e.getMessage());
        }
    }

    /**
     * Loads competition results from a file and maps them to {@link CompetitionResults} objects.
     * Each result is parsed from a semicolon-separated format and added to the list if the corresponding member is found in the repository.
     *
     * @param filePath The file path from which to load the competition results.
     * @param memberRepository The member repository used to look up members by their ID.
     * @return A list of {@link CompetitionResults} objects loaded from the file.
     */
    public List<CompetitionResults> loadCompetitionResults(String filePath, MemberRepository memberRepository) {
        List<CompetitionResults> results = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                // Parse required data
                int memberId = Integer.parseInt(parts[0]);
                String event = parts[1];
                ActivityType activityType = ActivityType.valueOf(parts[2].toUpperCase()); // Assuming activity type is stored as a string
                int placement = Integer.parseInt(parts[3]);
                double time = Double.parseDouble(parts[4]);
                String date = parts[5];
                MembershipLevel level = MembershipLevel.valueOf(parts[6].toUpperCase()); // Assuming level is stored as a string

                // Resolve the member from MemberRepository
                Member member = memberRepository.findById(memberId);
                if (member != null) {
                    // Add competition result to the list
                    results.add(new CompetitionResults(member, level, event, placement, time, date, activityType));
                }
            }
        } catch (Exception e) {
            System.err.println("Error loading competition results: " + e.getMessage());
        }
        return results;
    }

    /**
     * Saves the list of training results to a file.
     * Each training result is saved in a CSV-like format, where each line represents a training result.
     *
     * @param results The list of training results to be saved.
     */
    public void saveTrainingResults(List<TrainingResults> results) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(trainingResultsFilePath))) {
            for (TrainingResults result : results) {
                writer.write(result.getMember().getMemberId() + ";" +
                        result.getLevel() + ";" +
                        result.getActivityType() + ";" +
                        result.getTime() + ";" +
                        result.getDate());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving training results: " + e.getMessage());
        }
    }

    /**
     * Loads training results from a file and maps them to {@link TrainingResults} objects.
     * Each result is parsed from a semicolon-separated format and added to the list if the corresponding member is found in the repository.
     *
     * @param filePath The file path from which to load the training results.
     * @param memberRepository The member repository used to look up members by their ID.
     * @return A list of {@link TrainingResults} objects loaded from the file.
     */
    public List<TrainingResults> loadTrainingResults(String filePath, MemberRepository memberRepository) {
        List<TrainingResults> results = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                String memberIdStr = parts[0];
                MembershipLevel level = MembershipLevel.valueOf(parts[1]);
                ActivityType activityType = ActivityType.valueOf(parts[2]);
                double time = Double.parseDouble(parts[3]);
                String date = parts[4];

                // Resolve the member from MemberRepository
                Member member = memberRepository.findById(Integer.parseInt(memberIdStr));
                if (member != null) {
                    results.add(new TrainingResults(member, level, activityType, time, date));
                }
            }

        } catch (IOException e) {
            System.err.println("Error saving training results: " + e.getMessage());
        }
        return results;
    }


}

