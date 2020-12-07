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
import util.SigKey;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Set;

public class PortReceiver extends Thread
{
    private static final Logger logger = LoggerFactory.getLogger(PortReceiver.class);

    private final Node node;

    public PortReceiver(Node n)
    {
        this.node = n;
    }

    @Override
    public void run()
    {
        logger.info("Thread (for receiving packets on port " + node.getPort() + ") started.");

        try
        {
            DatagramSocket serverSocket = new DatagramSocket(node.getPort());
            byte[]         buffer       = new byte[16384];

            while (true)
            {
                DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(incoming);

                Message message = PacketHandler.parseMessage(incoming);

                switch (message.getMsgType())
                {
                    case PeerResponse:
                        logger.info("new peer response from " + message.getSender());
                        PeerInfo.peers.add(message.getSender());

                        // adds address to chain accounts
                        Controller.worldState.getAccounts().put(message.getSender().getAddress(), 100.);
                        break;

                    case ChainSyncRequest:
                        // do not process request from oneself
                        if (!message.getSender().equals(node))
                        {
                            BlockHeader rcvBlockHeader = PacketHandler.parseBlockHeader(message);
                            logger.info("incoming chain sync request, {}.", rcvBlockHeader);

                            Block localBlock = Controller.blockchain.fetchBlock(rcvBlockHeader.getHash());
                            if (localBlock == null)
                            {
                                logger.warn("requester sent invalid or corrupted block.");
                                break;
                            }

                            int chainHeight     = Controller.blockchain.getChain().size();
                            int bestHeightPeer  = rcvBlockHeader.getHeight() + 1;
                            int noMissingBlocks = chainHeight - bestHeightPeer;

                            if (noMissingBlocks == 0)
                            {
                                // send info (null block) that new peer's chain is up to date
                                logger.info("{}'s chain is up to date.", message.getSender());
                                PortSender.respondChainSync(message.getSender().getPort(), node, null);

                            } else
                            {
                                // respond with next block in line
                                logger.debug("{} is {} block(s) behind.", message.getSender(), noMissingBlocks);
                                Block nextBlock = Controller.blockchain.getChain()
                                        .get(localBlock.getHeader().getHeight() + 1);

//                                    logger.debug("responding with {}.", nextBlock);
                                PortSender.respondChainSync(message.getSender().getPort(), node, nextBlock);
                            }
                        }
                        break;

                    case ChainSyncResponse:
                        if (message.getPayload() == null)
                        {
                            logger.info("Sync response contains no block, local chain is up to date.");
                        } else
                        {
                            Block nextBlock = PacketHandler.parseBlock(message);
                            Controller.blockchain.add(nextBlock);
                            Controller.worldState.update(nextBlock.getTxs());
                            PortSender.requestChainSync(node, Controller.blockchain.getLatestBlock().getHeader());
                        }
                        break;

                    case TxProposal:
                        logger.info("incoming TX Proposal " + message.getPayload());
                        TxProposal txProp = PacketHandler.parseTxProp(message);
                        if (SmartContract.verifyTxProposal(txProp, message.getSigKey()))
                        {
                            logger.info("endorsing proposal; added to mempool");
                            Controller.worldState.getMempool().add(new Transaction(txProp, message.getSigKey()));
                            SigKey sigKey = new SigKey(Controller.wallet.sign(txProp), Controller.wallet.getPub());
                            PortSender.endorseTx(message.getSender().getPort(), node, txProp, sigKey);
                        }
                        break;

                    case TxEndorsement:
                        logger.info("incoming TX Endorsement " + message);
                        TxProposal endorsedProposal = PacketHandler.parseTxProp(message);

                        // update endorsement (add sigKey of endorser)
                        Set<SigKey> endorsements = PeerInfo.activeProposals.get(endorsedProposal);
                        endorsements.add(message.getSigKey());
                        PeerInfo.activeProposals.put(endorsedProposal, endorsements);
                        break;

                    case TxSubmission:
                        Transaction submittedTx = PacketHandler.parseTx(message);
                        logger.info("incoming TX Submission " + submittedTx);
                        //
                        if (SmartContract.verifyTxSubmission(submittedTx))
                        {
                            Controller.worldState.getMempool().add(submittedTx);
                            logger.debug("{} is valid, added to mempool.", submittedTx);
                        } else
                        {
                            logger.error("{} rejected.", submittedTx);
                        }
                        break;

                    default:
                        //
                }
            }

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
