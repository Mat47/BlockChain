package ledger;

import accessPermission.Role;
import app.Controller;
import app.Node;
import app.TxProposal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SigKey;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

/**
 * Validation contract which defines the rules (e.g. enforcing endorsement policy) between organisations in executable code. Apps invoke a smart contract to interact with the blockchain.
 */
public class SmartContract
{
    private static Logger logger = LoggerFactory.getLogger(SmartContract.class);

    /**
     * @param b block to be verified
     * @return true if block (header) and signature are authentic, else false
     */
    public static boolean verifyNewBlockSig(Block b)
    {
//        if (b.getOrdererSig() != null)
//        {
//            return false;
//        }
        if( Controller.blockchain.verifyBlock(b) && isSigValid(b.getHeader().stringify(), b.getOrdererSig()) )
        {
            return true;
        }
        return false;
    }

    /**
     * Orders (locally) then broadcasts a new block.
     * @param orderer the node attempting the ordering ("mining") process
     * @return
     */
    public static Block orderNewBlock(Node orderer)
    {
        if (!orderer.getRights().contains(Role.ORDERER))
        {
            System.out.println("You are not authorised to order blocks.");
        }
        else
        {
            MerkleTree  merkleTree  = new MerkleTree(Controller.worldState.getMempool());
            String      root        = merkleTree.getMerkleRoot().get(0);
            BlockHeader blockHeader = new BlockHeader(Controller.blockchain.getLatestBlock().getHeader().getHeight() + 1, root, Controller.blockchain.getLatestBlock().getHeader().getHash(), System.currentTimeMillis());
            SigKey      sigKey      = new SigKey(Controller.wallet.sign(blockHeader), Controller.wallet.getPub());
            return new Block(blockHeader, Controller.worldState.getMempool(), sigKey);
            //return Controller.worldState.processMempool();
        }
        return null;
    }

    public static boolean verifyTxProposal(TxProposal txProp, SigKey sigKey)
    {
        if (Controller.worldState.getAccounts().get(txProp.getFromAddress()).equals(null))
        {
            logger.error("Address {} does not exist.", txProp.getFromAddress());
            return false;
        }
        if( !isSigValid(txProp.stringify(), sigKey) )
        {
            logger.error("Invalid proposal, corrupted Signature.");
            return false;
        }
        if( Controller.worldState.getAccounts().get(txProp.getFromAddress()) < txProp.getAmount() )
        {
            logger.error("Invalid proposal, not enough funds on account.");
            return false;
        }
        return true;
    }

    public static boolean verifyTxSubmission(Transaction submittedTx)
    {
        if( !areEndorsementsValid(submittedTx) )
        {
            logger.info("Invalid tx, corrupted Signature.");
            return false;
        }
        if( !isSigValid(submittedTx.getTxProposal().stringify(), submittedTx.getSigKey()) )
        {
            logger.info("Invalid tx, corrupted Signature.");
            return false;
        }
        if( Controller.worldState.getAccounts().get(submittedTx.getTxProposal().getFromAddress()) < submittedTx.getTxProposal().getAmount() )
        {
            logger.info("Invalid proposal, not enough funds on account.");
            return false;
        }

        Controller.worldState.getMempool().add(submittedTx);
        logger.debug("{} is valid, added to mempool.", submittedTx);
        return true;
    }

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
            logger.error("Signature could not be verified.");
            e.printStackTrace();
        }
        return false;
    }

    private static boolean areEndorsementsValid(Transaction tx)
    {
        int i = 1;
        for(SigKey sigKey : tx.getEndorsements())
        {
            System.out.print(i + ". endorsement " + sigKey + " ");
            if(!isSigValid(tx.getTxProposal().stringify(), sigKey))
            {
                return false;
            }
            i++;
        }
        return true;
    }

    public static PublicKey extractPubKey(byte[] encKey)
    {
        X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(encKey);
        KeyFactory         keyFactory;

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
