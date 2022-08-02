package Login;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Hasher {
    public static String hash(String input) {
        MessageDigest digest;
        try {digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {e.printStackTrace(); return "hashFail";}
        return Base64.getEncoder().encodeToString(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
    }
}
