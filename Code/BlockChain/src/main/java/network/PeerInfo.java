package network;

import app.Node;
import app.TxProposal;
import ledger.Block;
import util.SigKey;

import java.util.*;

public class PeerInfo
{
    protected static Set<Node> peers = new HashSet<>();

    public static Map<TxProposal, Set<SigKey>> activeProposals = new HashMap<>();

    public static List<Block> blocksInSync = new ArrayList<>();

    public static Set<Node> getPeers()
    {
        return peers;
    }

}
