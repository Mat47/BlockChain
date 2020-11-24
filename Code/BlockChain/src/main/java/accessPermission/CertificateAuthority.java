package accessPermission;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.util.logging.Logger;

/**
 * An external entity issuing digital identities in the form of key pairs (PKI), e.g. Symantec, GeoTrust, GoDaddy.
 */
public class CertificateAuthority
{
    private static Logger logger = Logger.getLogger(CertificateAuthority.class.getName());

    private String name;

    public CertificateAuthority(String name)
    {
        this.name = name;
    }

    public KeyPair issueCertificate()
    {
        System.out.print("[CA] Verifying application...");  // external validation methods IRL
        try
        {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom     random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);
            System.out.println("issuing new digital ID.");
            return keyGen.generateKeyPair();

        } catch (Exception e)
        {
            logger.warning("Issuance failed, Key(Pair) could not be generated.");
            e.printStackTrace();
        }
        return null;
    }

    //
    public String getName()
    {
        return name;
    }
}
