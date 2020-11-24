package network;

import app.Node;
import app.TxProposal;
import util.SigKey;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PeerInfo
{
    protected static Set<Node> peers = new HashSet<>();

    //todo? refactor into own package
    public static Map<TxProposal, Set<SigKey>> activeProposals = new HashMap<>();

    public static Set<Node> getPeers()
    {
        return peers;
    }

}
