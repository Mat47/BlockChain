package ledger;

import util.Sha256Hasher;

public class BlockHeader
{
    private int    height;
    private String merkleRoot;
    private String preHash;
    private long   timestamp;
    private String hash;

    public BlockHeader(int height, String merkleRoot, String preHash)
    {
        this.height = height;
        this.merkleRoot = merkleRoot;
        this.preHash = preHash;
        this.timestamp = System.currentTimeMillis();
        this.hash = Sha256Hasher.hash(height + merkleRoot + preHash + timestamp);
    }

    @Override
    public String toString()
    {
        return "BlockHeader{" +
                "height=" + height +
                ", merkleRoot='" + merkleRoot + '\'' +
                ", preHash='" + preHash + '\'' +
                ", timestamp=" + timestamp +
                ", hash='" + hash + '\'' +
                '}';
    }

    //
    public int getHeight()
    {
        return height;
    }

    public String getMerkleRoot()
    {
        return merkleRoot;
    }

    public String getPreHash()
    {
        return preHash;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public String getHash()
    {
        return hash;
    }
}
