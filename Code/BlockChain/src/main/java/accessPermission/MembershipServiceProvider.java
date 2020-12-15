package accessPermission;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PublicKey;
import java.util.*;

/**
 * Lists (public keys of) network members, i.e. stores the permissioned identities and also their rights.
 */
public class MembershipServiceProvider
{
    Logger logger = LoggerFactory.getLogger(MembershipServiceProvider.class);

    private Set<CertificateAuthority> acceptedCAs;

    private Set<PublicKey> permissioned;
    private Set<PublicKey> revoked;

    public MembershipServiceProvider()
    {
        this.acceptedCAs = new HashSet<>();
        acceptedCAs.add(new CertificateAuthority("GoDaddy"));

        this.permissioned = new HashSet<>();
        this.revoked = new HashSet<>();
    }

    /**
     * Checks if the applicant's digital ID (public key) is valid.
     *
     * @param pubKey the applicant's digital identity
     * @return true if pubKey is valid, false otherwise
     */
    public boolean apply(PublicKey pubKey, int port, String name)
    {
        if (verifyId(pubKey))
        {
            permissioned.add(pubKey);
            return true;
        }
        return false;
    }

    /**
     * Checks if a digital ID (public key) is valid/accepted.
     *
     * @param pubKey
     * @return
     */
    private boolean verifyId(PublicKey pubKey)
    {
        // external validation methods IRL
        logger.info("[MSP] Validating Digital Identity (PubKey)...complete.");
        return true;
    }

}
