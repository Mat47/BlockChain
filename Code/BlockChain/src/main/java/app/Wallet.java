package app;

import ledger.Asset;
import util.Sha256Hasher;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class Wallet
{
    private KeyPair             keyPair;
    private PublicKey           pub;
    private String              address;
    private Map<Asset, Integer> assets;

    public Wallet()
    {
        // needed for (de)serialization (Jackson)
    }

    public Wallet(KeyPair keyPair)
    {
        this.keyPair = keyPair;
        this.pub = keyPair.getPublic();
        this.address = Sha256Hasher.hash(new String(pub.getEncoded()));
        this.assets = new HashMap<>();
        assets.put(Asset.ETH, 32);  // adding initial assets for demo purposes
    }

    public KeyPair getKeyPair()
    {
        return keyPair;
    }

    public PublicKey getPub()
    {
        return pub;
    }

    public String getAddress()
    {
        return address;
    }

    public Map<Asset, Integer> getAssets()
    {
        return assets;
    }
}
