package swimclub.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtils {

    // This method generates a random salt
    public static String generateSalt() {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[16];
            sr.nextBytes(salt);
            return Base64.getEncoder().encodeToString(salt); // Encode the salt in Base64 format
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating salt", e);
        }
    }

    // This method hashes the password with a given salt
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(Base64.getDecoder().decode(salt)); // Decode the salt back from Base64
            byte[] hashedPassword = md.digest(password.getBytes()); // Hash the password with the salt
            return Base64.getEncoder().encodeToString(hashedPassword); // Return the hashed password as Base64
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public static boolean validatePassword(String inputPassword, String storedSalt, String storedHashedPassword) {
        String hashedInputPassword = hashPassword(inputPassword, storedSalt); // Hash the input password with the stored salt
        return hashedInputPassword.equals(storedHashedPassword); // Compare hashes
    }
}
