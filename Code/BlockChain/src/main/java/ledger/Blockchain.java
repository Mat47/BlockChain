package ledger;

import app.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SigKey;

import java.util.ArrayList;
import java.util.List;

/**
 * The data structure containing the intertwined blocks.
 */
public class Blockchain
{
    private Logger logger = LoggerFactory.getLogger(Blockchain.class);

    private List<Block> chain;

    public Blockchain()
    {
        this.chain = new ArrayList<>();

        Block genesis = new Block(0, "1337", null, 1337, new ArrayList<>(), new SigKey());   // use hard coded genesis values for consistent genesis hash (across nodes)
        chain.add(genesis);
    }

    public boolean add(Block b)
    {
        if (verifyBlock(b))
        {
            chain.add(b);
            logger.info("Added new {} to local chain.", b);
            return true;
        }
        return false;
    }

    protected boolean verifyBlock(Block newBlock)
    {
        String manHash = BlockHeader.genHash(
                newBlock.getHeader().getHeight(),
                newBlock.getHeader().getMerkleRoot(),
                newBlock.getHeader().getPreHash(),
                newBlock.getHeader().getTimestamp()
        );

        return
                newBlock.getHeader().getHeight() == getLatestBlock().getHeader().getHeight() + 1                // correct height?
                        && newBlock.getHeader().getPreHash().equals(getLatestBlock().getHeader().getHash())     // correct preHash?
                        && newBlock.getHeader().getHash().equals(manHash);                                      // correct hash?
    }

//    protected boolean verifyGenesis()
//    {
//        Block genesis = Controller.blockchain.getChain().get(0);
//
//        return genesis.getHeader().getHeight() == 0
//                && genesis.getHeader().getPreHash() == null
//                && genesis.getHeader().getHash().equals(Block.generateBlockHash(genesis));
//    }

    public Block getLatestBlock()
    {
        return chain.get(chain.size() - 1);
    }

    //
    public List<Block> getChain()
    {
        return chain;
    }

}
