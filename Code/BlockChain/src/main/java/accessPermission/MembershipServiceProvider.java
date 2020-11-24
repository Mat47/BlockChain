package accessPermission;

import java.security.PublicKey;
import java.util.*;
import java.util.logging.Logger;

/**
 * Lists (public keys of) network members, i.e. stores the permissioned identities and also their rights.
 */
public class MembershipServiceProvider
{
    Logger logger = Logger.getLogger(MembershipServiceProvider.class.getName());

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
        System.out.println("[MSP] Validating Digital Identity (PubKey)...complete.");
        return true;
    }

}
