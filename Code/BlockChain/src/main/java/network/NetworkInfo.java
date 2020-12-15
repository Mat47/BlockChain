package network;

import app.Node;
import app.TxProposal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SigKey;

import java.util.*;

/**
 * Stores network information (peers, active tx proposals, blocks during syncing process).
 */
public class NetworkInfo
{
    private static final Logger logger = LoggerFactory.getLogger(NetworkInfo.class);

    protected static Set<Node> peers = new HashSet<>();

    public static Map<TxProposal, Set<SigKey>> activeProposals = new HashMap<>();

//    public static List<Block> blocksInSync = new ArrayList<>();

    //todo DEBUG if item=size and node is last entry it continues and throws exception - no peer found
//    public static Node fetchRandPeer(Node requester)
//    {
//        int size = peers.size();
//        int item = new Random().nextInt(size);
//        int i = 0;
//        for(Node eachN : peers)
//        {
//            if (eachN.equals(requester) && size > 1)
//            {
//                continue;
//                // skips iteration if there are more peers than just oneself
//            }
//            if (i == item)
//            {
//                return eachN;
//            }
//            i++;
//        }
//        logger.warn("did not find any peer.");
//        return null;
//    }

    public static Node fetchRandPeer(Node requester)
    {
        int size = peers.size();
        if (size == 0 || size == 1)
            return requester;

        int item = new Random().nextInt(size);
        int i = 0;
        for(Node eachN : peers)
        {
            if (i == item)
            {
                if (eachN.equals(requester))
                {
                    return fetchRandPeer(requester);
                } else {
                    return eachN;
                }
            }
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
