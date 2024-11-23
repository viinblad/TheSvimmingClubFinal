package swimclub.utilities;

import swimclub.models.MembershipType;
import swimclub.models.MembershipCategory;
import swimclub.models.MembershipLevel;

public class Validator {

    // Validate name: Must not be null or empty
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    // Validate age: Must be between 0 and 120
    public static boolean isValidAge(int age) {
        return age >= 0 && age <= 120;
    }

    // Validate membership type: Must be one of the predefined values for both category and level
    public static boolean isValidMembershipType(MembershipType membershipType) {
        // Ensure both category and level are valid
        return membershipType != null &&
                (membershipType.getCategory() == MembershipCategory.COMPETITIVE ||
                        membershipType.getCategory() == MembershipCategory.EXERCISE) &&
                (membershipType.getLevel() == MembershipLevel.JUNIOR ||
                        membershipType.getLevel() == MembershipLevel.SENIOR);
    }

    // Validate email: Must contain '@' symbol
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }

    // Validate phone number: Must be 8 digits
    public static boolean isValidPhoneNumber(int phoneNumber) {
        String phoneNumberString = String.valueOf(phoneNumber);
        return phoneNumberString.length() == 8;
    }

    // Generic validation with error throwing
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
