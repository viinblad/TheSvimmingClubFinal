package swimclub.utilities;

import swimclub.models.MembershipCategory;
import swimclub.models.MembershipLevel;
import swimclub.models.MembershipType;

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
        // Ensure both category and level are valid
        return membershipType != null &&
                (membershipType.getCategory() == MembershipCategory.COMPETITIVE ||
                        membershipType.getCategory() == MembershipCategory.EXERCISE) &&
                (membershipType.getLevel() == MembershipLevel.JUNIOR ||
                        membershipType.getLevel() == MembershipLevel.SENIOR);
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
     * @param name           The name of the member.
     * @param age            The age of the member.
     * @param membershipType The membership type of the member.
     * @param email          The email address of the member.
     * @param phoneNumber    The phone number of the member.
     * @throws IllegalArgumentException if any validation fails.
     */
    public static void validateMemberData(String name, int age, String membershipType,
                                          String email, int phoneNumber) throws IllegalArgumentException {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Invalid name: Name cannot be null or empty.");
        }
        if (!isValidAge(age)) {
            throw new IllegalArgumentException("Invalid age: Age must be between 0 and 120.");
        }
        // Convert the membershipType string to a MembershipType object
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
    }
}
