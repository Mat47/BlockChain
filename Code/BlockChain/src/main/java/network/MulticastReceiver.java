package network;

import app.Controller;
import app.Node;
import app.TxProposal;
import ledger.Block;
import ledger.BlockHeader;
import ledger.SmartContract;
import ledger.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastReceiver extends Thread
{
    private Logger logger = LoggerFactory.getLogger(MulticastReceiver.class);

    protected MulticastSocket socket = null;
    protected Node            node;

    public MulticastReceiver(Node user)
    {
        this.node = user;
    }

    @Override
    public void run()
    {
        logger.info("Thread (for receiving multicasts) started.");

        try
        {
            socket = new MulticastSocket(Config.multicastPort);
            InetAddress group = InetAddress.getByName(Config.multicastHost);
            socket.joinGroup(group);

            while (true)
            {
                byte[]         buffer   = new byte[9800];
                DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);

                socket.receive(incoming);
                String received = new String(incoming.getData(), 0, incoming.getLength());
                if ("end".equals(received))
                {
                    break;
                }
                Message message = PacketHandler.parseMessage(incoming);
                switch (message.getMsgType())
                {
                    case PeerDiscovery:
                        logger.info("incoming peer discovery from {}.", message.getSender());
                        if (!PeerInfo.peers.contains(message.getSender()))
                        {
                            PortSender.respondHandshake(message.getSender().getPort(), node);
                            PeerInfo.peers.add(message.getSender());
                            // adds address to chain accounts
                            Controller.worldState.getAccounts().put(message.getSender().getAddress(), 100.);
                        }
                        break;

//                    case ChainSyncRequest:
//                        // do not process request from oneself
//                        if (!message.getSender().equals(node))
//                        {
//                            BlockHeader rcvBlockHeader = PacketHandler.parseBlockHeader(message);
//                            logger.info("incoming chain sync request, {}.", rcvBlockHeader);
//
//                            int chainHeight     = Controller.blockchain.getChain().size();
//                            int bestHeightPeer  = rcvBlockHeader.getHeight() + 1;
//                            int noMissingBlocks = chainHeight - bestHeightPeer;
//
//                            if (noMissingBlocks == 0)
//                            {
//                                // send info (null block) that new peer's chain is up to date
//                                logger.info("{}'s chain is up to date.", message.getSender());
//                                PortSender.respondChainSync(message.getSender().getPort(), node, null);
//
//                            } else {
//                                // respond with next block in line
//                                logger.debug("{} is {} block(s) behind.", message.getSender(), noMissingBlocks);
//                                Block localBlock = Controller.blockchain.fetchBlock(rcvBlockHeader.getHash());
//                                if (localBlock == null)
//                                {
//                                    logger.warn("requester sent invalid block => has invalid chain.");
//                                } else {
//                                    Block nextBlock = Controller.blockchain.getChain()
//                                            .get( localBlock.getHeader().getHeight()+1 );
//
////                                    logger.debug("responding with {}.", nextBlock);
//                                    PortSender.respondChainSync( message.getSender().getPort(), node, nextBlock);
//                                }
//
////                            Block nextBlock = Main.blockchain.getChain().get(peerHeight);
////                            System.out.println("Sending next block: " + nextBlock);
////                            Main.nodeClient.sendChainSyncResponse(peerPort, nextBlock, noMissingBlocks);
//                            }
//                        }
//                        break;

                    case NewBlock:
                        Block rcvBlock = PacketHandler.parseBlock(message);
                        logger.info("receiving new Block # {}.", rcvBlock.getHeader().getHash());
//                        System.out.println("RCV NEW BLOCK " + received);
                        if (SmartContract.verifyNewBlockSig(rcvBlock))
                        {
                            Controller.blockchain.add(rcvBlock);
                            Controller.worldState.update(rcvBlock.getTxs());
                            Controller.worldState.getMempool().clear();
                        } else
                        {
                            logger.error("New Block invalid.");
                        }
                        break;
                }
            }

            socket.leaveGroup(group);
            socket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
