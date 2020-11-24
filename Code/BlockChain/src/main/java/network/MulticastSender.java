package network;

import app.Node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

public class MulticastSender
{
    private static Logger logger = Logger.getLogger(MulticastSender.class.getName());

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
            logger.info("[MCast] Sending handshake request...");
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
