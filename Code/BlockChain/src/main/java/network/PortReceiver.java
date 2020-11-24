package network;

import app.Controller;
import app.Node;
import app.TxProposal;
import ledger.SmartContract;
import util.SigKey;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Set;
import java.util.logging.Logger;

public class PortReceiver extends Thread
{
    private static final Logger logger = Logger.getLogger(PortReceiver.class.getName());

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
            byte[] buffer = new byte[4112];

            while (true)
            {
                DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
                serverSocket.receive(incoming);

                Message message = PacketHandler.parseMessage(incoming);

                switch (message.getMsgType())
                {
                    case PeerResponse:
                        logger.info("New peer response from " + message.getSender());
                        PeerInfo.peers.add(message.getSender());

                        // adds address to chain accounts
                        Controller.worldState.getAccounts().put(message.getSender().getAddress(), 100.);
                        break;

                    case TxProposal:
                        logger.info("Received tx proposal " + message.getPayload());
                        TxProposal txProp = PacketHandler.parseTxProp(message);
                        if (SmartContract.verifyTxProposal(txProp, message.getSigKey())) //todo check balance and if addr exists
                        {
                            logger.info("Proposal validated, responding with endorsement...");
                            SigKey sigKey = new SigKey(Controller.wallet.sign(txProp), Controller.wallet.getPub());
                            PortSender.endorseTx(message.getSender().getPort(), node, txProp, sigKey);
                        }
                        break;

                    case TxEndorsement:
                        logger.info("Received endorsement " + message);

                        // add endorsement sigKey
                        Set<SigKey> endorsements = PeerInfo.activeProposals.get(PacketHandler.parseTxProp(message));
                        endorsements.add(message.getSigKey());
                        PeerInfo.activeProposals.put(PacketHandler.parseTxProp(message), endorsements);
                        break;

                    case TxSubmission:
                        logger.info("Received tx submission " + message);
                        //todo
                        // 1. check sigs, 2. store rcved tx in mempool
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
