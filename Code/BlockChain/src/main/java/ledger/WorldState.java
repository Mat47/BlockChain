package ledger;

import app.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SigKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//todo singleton?
public class WorldState
{
    private Logger logger = LoggerFactory.getLogger(WorldState.class);

    private Map<String, Double> accounts;
    private List<Transaction>   mempool;

    public WorldState()
    {
        this.accounts = new HashMap<>();
        this.mempool = new ArrayList<>();
    }

    public Map<String, Double> getAccounts()
    {
        return accounts;
    }

    public void update(List<Transaction> mempool)
    {
        for (Transaction tx : mempool)
        {
            double amount = tx.getTxProposal().getAmount();
            String from   = tx.getTxProposal().getFromAddress();
            String to     = tx.getTxProposal().getToAddress();

            accounts.put(from, accounts.get(from) - amount);
            accounts.put(to, accounts.get(to) + amount);
        }
        this.mempool.clear();
        logger.info("Updated world state, mempool cleared.");
    }

    protected Block processMempool()
    {
        if (mempool.isEmpty())
        {
            logger.info("Cannot create new block, there are no pending transactions.");
            return null;
        }
        MerkleTree  merkleTree  = new MerkleTree(mempool);
        String      root        = merkleTree.getMerkleRoot().get(0);
        BlockHeader blockHeader = new BlockHeader(Controller.blockchain.getLatestBlock().getHeader().getHeight() + 1, root, Controller.blockchain.getLatestBlock().getHeader().getHash(), System.currentTimeMillis());
        SigKey      sigKey      = new SigKey(Controller.wallet.sign(blockHeader), Controller.wallet.getPub());
        Block       b           = new Block(blockHeader, mempool, sigKey);
        Controller.blockchain.add(b); //todo??? delete because block is sent/received to oneself
        return b;
    }

    public List<Transaction> getMempool()
    {
        return mempool;
    }
}
