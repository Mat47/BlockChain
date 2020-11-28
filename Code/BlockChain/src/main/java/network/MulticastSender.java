package network;

import app.Node;
import com.fasterxml.jackson.databind.ObjectMapper;
import ledger.Block;
import org.slf4j.LoggerFactory;
import util.SigKey;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastSender
{
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(MulticastSender.class);

    private static DatagramSocket socket;
    private static InetAddress    group;

    public static void requestHandshake(Node requester)
    {
        try
        {
            socket = new DatagramSocket();
            group = InetAddress.getByName(Config.multicastHost);

            Message msg    = new Message(MessageType.PeerDiscovery, requester, null);
            byte[]  buffer = msg.serialize();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, Config.multicastPort);
            logger.info("Sending handshake request...");
            socket.send(packet);

        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            socket.close();
        }
    }

    public static void sendNewBlock(Node orderer, Block latestBlock)
    {
        try
        {
            socket = new DatagramSocket();
            group = InetAddress.getByName(Config.multicastHost);

            Message msg    = new Message( MessageType.NewBlock, orderer, latestBlock.getHeader(), latestBlock.getTxs(), latestBlock.getOrdererSig());
            byte[]  buffer = msg.serialize();

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, Config.multicastPort);
            socket.send(packet);

        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            socket.close();
        }
    }
}
