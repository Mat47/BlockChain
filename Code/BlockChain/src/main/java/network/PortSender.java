package network;

import app.Node;
import app.TxProposal;
import com.fasterxml.jackson.core.JsonProcessingException;
import ledger.Transaction;
import util.SigKey;

import java.io.IOException;
import java.net.*;
import java.security.PublicKey;
import java.util.Set;

public class PortSender
{
    public static void respondHandshake(int toPort, Node responder) throws IOException
    {
        Message        message = new Message(MessageType.PeerResponse, responder, null);
        byte[]         buffer  = message.serialize();
        DatagramPacket packet  = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), toPort);
        new DatagramSocket().send(packet);
    }

    public static void proposeTx(Node node, TxProposal txProposal, byte[] sign, PublicKey pub) throws IOException
    {
        for (int port : Config.endorsers)
        {
            Message message = new Message(MessageType.TxProposal, node, txProposal, new SigKey(sign, pub));
            byte[]  buffer = message.serialize();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), port);
            new DatagramSocket().send(packet);
        }
    }

    public static void endorseTx(int toPort, Node endorser, TxProposal txProp, SigKey sigKey) throws IOException
    {
        Message message = new Message(MessageType.TxEndorsement, endorser, txProp, sigKey);
        byte[]  buffer = message.serialize();

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), toPort);
        new DatagramSocket().send(packet);
    }

    public static void submitTxProposal(Node node, Transaction tx) throws IOException
    {
        Message message = new Message(MessageType.TxSubmission, node, tx);
        byte[]  buffer = message.serialize();

        //todo send to orderer
        int ordererPort = Config.getRandOrderer();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), ordererPort);
        new DatagramSocket().send(packet);
    }

}
