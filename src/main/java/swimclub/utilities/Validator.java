package swimclub.utilities;

public class Validator {

    // Validate name: Must not be null or empty
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }

    // Validate age: Must be between 0 and 120
    public static boolean isValidAge(int age) {
        return age >= 0 && age <= 120;
    }

    // Validate membership type: Must be one of predefined values
    public static boolean isValidMembershipType(String membershipType) {
        return membershipType != null &&
                (membershipType.equalsIgnoreCase("junior") ||
                        membershipType.equalsIgnoreCase("senior"));
    }
    // Validate email
    public static boolean isValidEmail(String email) {
        return email != null && email.contains("@");
    }
    //Validate phone number. Must be 8 digits
    public static boolean isValidPhoneNumber(int phoneNumber) {
        String phoneNumberString = String.valueOf(phoneNumber);
        return phoneNumberString.length() == 8;

    }




    // Generic validation with error throwing
    public static void validateMemberData(String name, int age, String membershipType, String email, int phoneNumber) throws IllegalArgumentException {
        if (!isValidName(name)) {
            throw new IllegalArgumentException("Invalid name: Name cannot be null or empty.");
        }
        if (!isValidAge(age)) {
            throw new IllegalArgumentException("Invalid age: Age must be between 0 and 120.");
        }
        if (!isValidMembershipType(membershipType)) {
            throw new IllegalArgumentException("Invalid membership type: Must be 'junior' or 'senior'.");
        }
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email: Email must be a valid email address.");
        }
        if (!isValidPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number: Phone number must be 8 digits.");
        }
    }
}
