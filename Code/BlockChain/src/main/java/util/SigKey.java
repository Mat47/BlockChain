package util;

import java.security.PublicKey;

/**
 * Container class to hold a signature and its belonging public key.
 */
public class SigKey
{
    private byte[] sig;
    private byte[] pub;

    public SigKey()
    {
    }

    public SigKey(byte[] sig, PublicKey pub)
    {
        this.sig = sig;
        this.pub = pub.getEncoded();
    }


    //
    public byte[] getSig()
    {
        return sig;
    }

    public byte[] getPub()
    {
        return pub;
    }
}
