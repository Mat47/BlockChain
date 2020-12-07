package network;

import app.Node;
import app.TxProposal;
import ledger.Block;
import ledger.BlockHeader;
import ledger.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.SigKey;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.PublicKey;

public class PortSender
{
    private static final Logger logger = LoggerFactory.getLogger(PortSender.class);

    public static void respondHandshake(int toPort, Node responder) throws IOException
    {
        Message        message = new Message(MessageType.PeerResponse, responder, null);
        byte[]         buffer  = message.serialize();

        DatagramPacket packet  = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), toPort);
        new DatagramSocket().send(packet);
    }

    public static void requestChainSync(Node requester, BlockHeader blockHeader) throws IOException
    {
        Message<BlockHeader> msg    = new Message<>(MessageType.ChainSyncRequest, requester, blockHeader);
        byte[]               buffer = msg.serialize();

        int toPort = PeerInfo.fetchRandPeer().getPort();
        DatagramPacket packet  = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), toPort);
        new DatagramSocket().send(packet);
    }

    public static void respondChainSync(int toPort, Node responder, Block b) throws IOException
    {
        Message<BlockHeader> message;
        if (b == null)
        {
            message = new Message<>(MessageType.ChainSyncResponse, responder, null);
        }
        else {
            message = new Message<>(MessageType.ChainSyncResponse, responder, b.getHeader(), b.getTxs(), b.getOrdererSig());
        }
        byte[] buffer = message.serialize();

        DatagramPacket packet  = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), toPort);
        new DatagramSocket().send(packet);
    }

    public static void proposeTx(Node node, TxProposal txProposal, byte[] sign, PublicKey pub) throws IOException
    {
        for (int port : Config.endorsers)
        {
            Message<TxProposal> message = new Message<>(MessageType.TxProposal, node, txProposal, new SigKey(sign, pub));
            byte[]              buffer  = message.serialize();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), port);
            new DatagramSocket().send(packet);
        }
    }

    public static void endorseTx(int toPort, Node endorser, TxProposal txProp, SigKey sigKey) throws IOException
    {
        Message<TxProposal> message = new Message<>(MessageType.TxEndorsement, endorser, txProp, sigKey);
        byte[]              buffer  = message.serialize();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), toPort);
        new DatagramSocket().send(packet);
    }

    public static void submitTxProposal(Node requester, Transaction tx) throws IOException
    {
        Message<Transaction> message = new Message<>(MessageType.TxSubmission, requester, tx);
        byte[]               buffer  = message.serialize();

        int ordererPort = Config.getRandOrderer();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), ordererPort);
        new DatagramSocket().send(packet);
    }

}
