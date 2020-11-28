package accessPermission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;

/**
 * An external entity issuing digital identities in the form of key pairs (PKI), e.g. Symantec, GeoTrust, GoDaddy.
 */
public class CertificateAuthority
{
    private static Logger logger = LoggerFactory.getLogger(CertificateAuthority.class);

    private String name;

    public CertificateAuthority(String name)
    {
        this.name = name;
    }

    public KeyPair issueCertificate()
    {
        // external validation methods IRL
        try
        {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DSA", "SUN");
            SecureRandom     random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(1024, random);
            logger.info("Issuing new digital ID (KeyPair).");
            return keyGen.generateKeyPair();

        } catch (Exception e)
        {
            logger.error("Issuance failed, Key(Pair) could not be generated.", e);
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
