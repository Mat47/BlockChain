package network;

import app.Node;

import java.util.HashSet;
import java.util.Set;

public class PeerInfo
{
    protected static Set<Node> peers = new HashSet<>();

    public static Set<Node> getPeers()
    {
        return peers;
    }
}
