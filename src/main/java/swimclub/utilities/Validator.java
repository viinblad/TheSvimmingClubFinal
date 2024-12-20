package swimclub.utilities;

import swimclub.models.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for validating data in the swim club application.
 */
public class Validator {

    /**
     * Validates the name of the member.
     * The name must not be null or empty.
     *
     * @param name The name of the member.
     * @return true if the name is valid, false otherwise.
     */
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    /**
     * Validates the age of the member.
     * The age must be between 0 and 120.
     *
     * @param age The age of the member.
     * @return true if the age is valid, false otherwise.
     */
    public static boolean isValidAge(int age) {
        return age >= 0 && age <= 120;
    }

    /**
     * Validates the membership type of the member.
     * The membership type must be valid for both category and level.
     *
     * @param membershipType The membership type to validate.
     * @return true if the membership type is valid, false otherwise.
     */
    public static boolean isValidMembershipType(MembershipType membershipType) {
        return membershipType != null &&
                (membershipType.getCategory() == MembershipCategory.COMPETITIVE ||
                        membershipType.getCategory() == MembershipCategory.EXERCISE) &&
                (membershipType.getLevel() == MembershipLevel.JUNIOR ||
                        membershipType.getLevel() == MembershipLevel.SENIOR);
    }

    public static boolean isValidMemberActivityType(ActivityTypeData activityType){
        return activityType != null &&
                (activityType.toActivityType() == ActivityType.BACKCRAWL ||
                        activityType.toActivityType() == ActivityType.BREASTSTROKE ||
                        activityType.toActivityType() == ActivityType.BUTTERFLY ||
                        activityType.toActivityType() == ActivityType.CRAWL);
    }

    /**
     * Validates the membership status of the member.
     * The status must be either ACTIVE or PASSIVE.
     *
     * @param membershipStatus The membership status to validate.
     * @return true if the membership status is valid, false otherwise.
     */
    public static boolean isValidMembershipStatus(MembershipStatus membershipStatus) {
        return membershipStatus == MembershipStatus.ACTIVE || membershipStatus == MembershipStatus.PASSIVE;
    }

    /**
     * Validates the payment status of the member.
     * The status must be one of the predefined PaymentStatus values.
     *
     * @param paymentStatus The payment status to validate.
     * @return true if the payment status is valid, false otherwise.
     */
    public static boolean isValidPaymentStatus(PaymentStatus paymentStatus) {
        return paymentStatus == PaymentStatus.COMPLETE ||
                paymentStatus == PaymentStatus.PENDING ||
                paymentStatus == PaymentStatus.FAILED;
    }

    /**
     * Validates the email address of the member.
     * The email must contain an '@' symbol.
     *
     * @param email The email address to validate.
     * @return true if the email is valid, false otherwise.
     */
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    /**
     * Validates the phone number of the member.
     * The phone number must be 8 digits long.
     *
     * @param phoneNumber The phone number to validate.
     * @return true if the phone number is valid, false otherwise.
     */
    public static boolean isValidPhoneNumber(int phoneNumber) {
        String phoneNumberString = String.valueOf(phoneNumber);
        return phoneNumberString.length() == 8;
    }

    /**
     * Validates the data of a member.
     * This method checks the validity of all the member's attributes and throws an exception if any are invalid.
     *
     * @param name             The name of the member.
     * @param age              The age of the member.
     * @param membershipType   The membership type of the member.
     * @param email            The email address of the member.
     * @param city             The city of the member.
     * @param street           The street of the member.
     * @param region           The region of the member.
     * @param zipcode          The zip code of the member.
     * @param phoneNumber      The phone number of the member.
     * @param membershipStatus The membership status of the member.
     * @param paymentStatus    The payment status of the member.
     * @param activityType     The preferred activity type of the member (e.g., Breaststroke, Crawl).
     * @throws IllegalArgumentException if any validation fails.
     *
     */

    public static void validateMemberData(String name, int age, String membershipType,
                                          String email, String city, String street, String region, int zipcode, int phoneNumber,
                                          MembershipStatus membershipStatus, String activityType, PaymentStatus paymentStatus) throws IllegalArgumentException {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Invalid name: Name cannot be null or empty.");
        }
        if (!isValidAge(age)) {
            throw new IllegalArgumentException("Invalid age: Age must be between 0 and 120.");
        }
        MembershipType type = MembershipType.fromString(membershipType);
        if (!isValidMembershipType(type)) {
            throw new IllegalArgumentException("Invalid membership type: Must be 'junior' or 'senior' and category must be 'competitive' or 'exercise'.");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email: Email must be a valid email address.");
        }
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number: Phone number must be 8 digits.");
        }
        if (!isValidMembershipStatus(membershipStatus)) {
            throw new IllegalArgumentException("Invalid membership status: Must be 'ACTIVE' or 'PASSIVE'.");
        }
        if (!isValidPaymentStatus(paymentStatus)) {
            throw new IllegalArgumentException("Invalid payment status: Must be 'COMPLETE', 'PENDING', or 'FAILED'.");
        }
        ActivityTypeData activity = ActivityTypeData.fromString(activityType);
        if (!isValidMemberActivityType(activity)) {
            throw new IllegalArgumentException("Invalid activitytype: Must be 'Crawl', 'Backcrawl', 'Breathstroke' or 'Butterfly'");
        }
    }

    /**
     * Validates the payment amount.
     * The amount must be greater than 0.
     *
     * @param amount The payment amount to validate.
     * @return true if the payment amount is valid, false otherwise.
     */
    public static boolean isValidPaymentAmount(double amount) {
        return amount > 0;
    }

    /**
     * Validates a payment.
     * Ensures the payment amount and status are valid.
     *
     * @param amount        The payment amount.
     * @param paymentStatus The payment status.
     * @throws IllegalArgumentException if validation fails.
     */
    public static void validatePayment(double amount, PaymentStatus paymentStatus) {
        if (!isValidPaymentAmount(amount)) {
            throw new IllegalArgumentException("Invalid payment amount: Amount must be greater than 0.");
        }
        if (!isValidPaymentStatus(paymentStatus)) {
            throw new IllegalArgumentException("Invalid payment status: Must be 'COMPLETE', 'PENDING', or 'FAILED'.");
        }
    }

    /**
     * Validates the payment reminder message.
     * Ensures the message is not too short, too long, and is not empty.
     *
     * @param reminder The reminder message to validate.
     * @throws IllegalArgumentException if validation fails.
     */
    public static void validatePaymentReminder(String reminder) {
        if (reminder == null || reminder.trim().isEmpty() || reminder.length() < 5 || reminder.length() > 255) {
            throw new IllegalArgumentException("Invalid reminder: Reminder must be between 5 and 255 characters long.");
        }
    }

        /**
         * Validates a competition result.
         * Ensures that the result is not null.
         *
         * @param result The competition result to validate.
         * @throws IllegalArgumentException if the result is null.
         */
        public static void validateCompetitionResult(CompetitionResults result) {
            if (result == null) {
                throw new IllegalArgumentException("Invalid competition result: Result cannot be empty.");
            }
        }

        /**
         * Validates that a member is not null.
         * Ensures that the provided member is not null.
         *
         * @param member The member to validate.
         * @throws IllegalArgumentException if the member is null.
         */
        public static void validateMemberNotNull(Member member) {
            if (member == null) {
                throw new IllegalArgumentException("Invalid member: Member cannot be empty.");
            }
        }

        /**
         * Validates the event name.
         * Ensures that the event name is not null or empty.
         *
         * @param event The event name to validate.
         * @throws IllegalArgumentException if the event name is null or empty.
         */
        public static void validateEventName(String event) {
            if (event == null || event.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid event name: Event name cannot be empty.");
            }
        }

        /**
         * Validates the placement in a competition.
         * Ensures that the placement is greater than or equal to 1.
         *
         * @param placement The placement to validate.
         * @throws IllegalArgumentException if the placement is less than 1.
         */
        public static void validatePlacement(int placement) {
            if (placement < 1) {
                throw new IllegalArgumentException("Invalid placement: Placement must be greater than 0.");
            }
        }

        /**
         * Validates the time in a competition or training session.
         * Ensures that the time is a non-negative value.
         *
         * @param time The time to validate.
         * @throws IllegalArgumentException if the time is negative.
         */
        public static void validateTime(double time) {
            if (time < 0) {
                throw new IllegalArgumentException("Invalid time: Time must be greater than 0.");
            }
        }

        /**
         * Validates the activity type in a competition or training session.
         * Ensures that the activity type is one of the predefined valid values (Crawl, Backcrawl, Breaststroke, Butterfly).
         *
         * @param activityType The activity type to validate.
         * @throws IllegalArgumentException if the activity type is not one of the valid types.
         */
        public static void validateActivityType(ActivityType activityType) {
            List<ActivityType> validActivityType = Arrays.asList(ActivityType.CRAWL, ActivityType.BACKCRAWL, ActivityType.BREASTSTROKE, ActivityType.BUTTERFLY);
            if (!validActivityType.contains(activityType)) {
                throw new IllegalArgumentException("Invalid activity type: Activity type must be Crawl, Backcrawl, Breaststroke, or Butterfly.");
            }
        }

        /**
         * Validates the date format and checks if the date is not in the future.
         * Ensures that the date is in the correct format ("dd-MM-yyyy") and is not later than the current date.
         * Prints a message if the date is in the future or if the format is incorrect.
         *
         * @param date The date to validate in the "dd-MM-yyyy" format.
         */
        public static void validateDate(String date) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

            try {
                LocalDateTime inputDate = LocalDateTime.parse(date, formatter);
                if (inputDate.isAfter(now)) {
                    System.out.println("Has to be present time.");
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter 'yyyy-MM-dd'.");
            }
        }
    /**
     * Validates a training result.
     * Ensures that the training result is not null.
     *
     * @param result The training result to validate.
     * @throws IllegalArgumentException if the result is null.
     */
    public static void validateTrainingResult(TrainingResults result) {
        if (result == null) {
            throw new IllegalArgumentException("Invalid competition result: Result cannot be null.");
        }
    }
    public static void validateUsername(String username) {
        // Check if username is already taken (this can be done by checking the AuthRepository)
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid username: Username cannot be empty.");
        }
    }

    /**
     * Validates the password (you can add strength checks here).
     */
    public static void validatePassword(String password) {
        // Simple password validation: at least 6 characters
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Invalid password: Password must be at least 6 characters.");
        }
    }

    /**
     * Validates the role of the user.
     */
    public static void validateRole(Role role) {
        if (role == null) {
            throw new IllegalArgumentException("Invalid role: Role cannot be null.");
        }
    }

}

