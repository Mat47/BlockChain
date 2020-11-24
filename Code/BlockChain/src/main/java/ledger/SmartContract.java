package ledger;

import app.TxProposal;
import com.fasterxml.jackson.databind.ObjectMapper;
import util.SigKey;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.logging.Logger;

/**
 * Validation contract which defines the rules (e.g. enforcing endorsement policy) between organisations in executable code. Apps invoke a smart contract to interact with the blockchain.
 */
public class SmartContract
{
    private static Logger logger = Logger.getLogger(SmartContract.class.getName());

    public boolean addBlock(Block b)
    {
        if (!isSigValid(b.stringify(), b.getOrdererSig()))
        {
            return false;
        }
//        blockchain.getChain().add(b);
        return true;
    }

    public static boolean verifyTxProposal(TxProposal txProp, SigKey sigKey)
    {
        if( !isSigValid(txProp.stringify(), sigKey) ) //& todo balanceCheck
        {
            return false;
        }
        return true;
    }

//    verifyBlock

    private static boolean isSigValid(String data, SigKey ordererSig)
    {
        try
        {
            Signature sig = Signature.getInstance("SHA1withDSA", "SUN");
            sig.initVerify(extractPubKey(ordererSig.getPub()));
            sig.update(data.getBytes());
            boolean verifies = sig.verify(ordererSig.getSig());
            logger.info(data + " => SigVerification " + verifies);
            return verifies;

        } catch (Exception e)
        {
            logger.warning("Exception while verifying Signature.");
            e.printStackTrace();
        }
        return false;
    }

    public boolean areEndorsementsValid(Transaction tx)
    {
        for(SigKey sigKey : tx.getEndorsements())
        {
            if(!isSigValid(tx.getTxProposal().stringify(), sigKey))
            {
                return false;
            }
        }
        return true;
    }

    public static PublicKey extractPubKey(byte[] encKey)
    {
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
        KeyFactory         keyFactory = null;

        try
        {
            keyFactory = KeyFactory.getInstance("DSA", "SUN");
            return keyFactory.generatePublic(pubKeySpec);

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
