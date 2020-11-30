package network;

import app.Node;
import app.TxProposal;
import ledger.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SigKey;

import java.util.*;

/**
 * Stores network information (peers, active tx proposals, blocks during syncing process).
 */
public class PeerInfo
{
    private static final Logger logger = LoggerFactory.getLogger(PeerInfo.class);

    protected static Set<Node> peers = new HashSet<>();

    public static Map<TxProposal, Set<SigKey>> activeProposals = new HashMap<>();

//    public static List<Block> blocksInSync = new ArrayList<>();

    public static Node fetchRandPeer()
    {
        int size = peers.size();
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for(Node eachN : peers)
        {
            if (i == item)
                return eachN;
            i++;
        }
        logger.warn("did not find any peer.");
        return null;
    }

    public static Set<Node> getPeers()
    {
        return peers;
    }

}
