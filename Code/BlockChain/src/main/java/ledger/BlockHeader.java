package ledger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import util.Sha256Hasher;

import java.util.Objects;

public class BlockHeader
{
    private int    height;
    private String merkleRoot;
    private String preHash;
    private long   timestamp;
    private String hash;

    public BlockHeader()
    {
    }

    public BlockHeader(int height, String merkleRoot, String preHash, long timestamp)
    {
        this.height = height;
        this.merkleRoot = merkleRoot;
        this.preHash = preHash;
        this.timestamp = timestamp;
        this.hash = genHash(height, merkleRoot, preHash, timestamp);
    }

    protected static String genHash(int height, String merkleRoot, String preHash, long timestamp)
    {
        return Sha256Hasher.hash(height + merkleRoot + preHash + timestamp);
    }

    public String stringify()
    {
        return "BlockHeader{" +
                "height=" + height +
                ", merkleRoot='" + merkleRoot + '\'' +
                ", preHash='" + preHash + '\'' +
                ", timestamp=" + timestamp +
                ", hash='" + hash + '\'' +
                '}';
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof BlockHeader)) return false;
        BlockHeader that = (BlockHeader) o;
        return Objects.equals(getHash(), that.getHash());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getHash());
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
