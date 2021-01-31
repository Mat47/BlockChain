package app;

import ledger.Asset;
import ledger.BlockHeader;
import util.Sha256Hasher;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Signature;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Public Key Cryptography Reference: https://docs.oracle.com/javase/tutorial/security/apisign/index.html
 */
public class Wallet implements Serializable
{
    private Logger logger = Logger.getLogger(Wallet.class.getName());

    private KeyPair             keyPair;
    private String              address;
    private Map<Asset, Integer> assets;

//    private List<Asset> assets
//    private spendable;

    public Wallet()
    {
        // needed for (de)serialization (Jackson)
    }

    public Wallet(KeyPair keyPair)
    {
        this.keyPair = keyPair;
        this.address = Sha256Hasher.hash(new String(keyPair.getPublic().getEncoded()));
        this.assets = new HashMap<>();
//        balance.put(Asset.ETH, 32);  // adding initial assets for demo purposes
        for (Asset eachAsset : Asset.values())
        {
            assets.put(eachAsset, 0);
        }
    }

    public byte[] sign(TxProposal txProp)
    {
        try
        {
            // specifies sig algo (DSA) using the digest algo (SHA-1)
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(keyPair.getPrivate());
            dsa.update(txProp.stringify().getBytes()); // supplies tx data to the Sig object
            return dsa.sign();

        } catch (Exception e)
        {
            logger.warning("Tx proposal could not be signed.");
            e.printStackTrace();
        }
        return null;
    }
    public byte[] sign(BlockHeader blockHeader)
    {
        try
        {
            // specifies sig algo (DSA) using the digest algo (SHA-1)
            Signature dsa = Signature.getInstance("SHA1withDSA", "SUN");
            dsa.initSign(keyPair.getPrivate());
            dsa.update(blockHeader.stringify().getBytes()); // supplies tx data to the Sig object
            return dsa.sign();

        } catch (Exception e)
        {
            logger.warning("Tx could not be signed.");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString()
    {
        return "Wallet{" +
                //"keyPair=" + keyPair +
                ", address='" + address + '\'' +
                ", balance=" + assets +
                '}';
    }

    //


    public KeyPair getKeyPair()
    {
        return keyPair;
    }

    public PublicKey getPub()
    {
        return keyPair.getPublic();
    }

    public String getAddress()
    {
        return Sha256Hasher.hash(new String(keyPair.getPublic().getEncoded()));
    }

    public Map<Asset, Integer> getAssets()
    {
        return assets;
    }
}
