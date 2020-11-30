package ledger;

import util.SigKey;

import java.util.List;

public class Block
{
    private BlockHeader       header;
    private List<Transaction> txs;
    private SigKey ordererSig;

    public Block()
    {
    }

    public Block(BlockHeader header, List<Transaction> txs, SigKey sigKey)
    {
        this.header = header;
        this.txs = txs;
        this.ordererSig = sigKey;
    }

    public Block(int height, String merkle, String preHash, long timestamp, List<Transaction> txs, SigKey sigKey)
    {
        this.header = new BlockHeader(height, merkle, preHash, timestamp);
        this.txs = txs;
        this.ordererSig = sigKey;
    }

    public String stringify()
    {
        return "Block{" +
                "header=" + header +
                ", TXs=" + txs +
                '}';
    }

    @Override
    public String toString()
    {
        return "Block{" +
                "header=" + header +
                ", TXs=" + txs +
                '}';
    }

    //
    public BlockHeader getHeader()
    {
        return header;
    }

    public List<Transaction> getTxs()
    {
        return txs;
    }

    public SigKey getOrdererSig()
    {
        return ordererSig;
    }
}
