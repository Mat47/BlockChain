package util;

import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256Hasher
{
    /**
     * Generates a 256-bit hash. <br>https://www.baeldung.com/sha-256-hashing-java
     *
     * @param dataToBeHashed
     * @return string of the hash's hex-format
     */
    public static String hash(String dataToBeHashed)
    {
        MessageDigest digest = null;
        try
        {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        byte[] hash = digest.digest(dataToBeHashed.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(hash));
    }
}
