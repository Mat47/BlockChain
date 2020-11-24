package ledger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The data structure containing the intertwined blocks.
 */
public class Blockchain
{
    private List<Block>         chain;
    private List<Transaction>   mempool;

    public Blockchain()
    {
        this.chain = new ArrayList<>();
        this.mempool = new ArrayList<>();

        Block genesis = new Block(0, null, null, null, null);
        chain.add(genesis);
    }

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
