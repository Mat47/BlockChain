package network;

import app.Node;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.net.*;

public class PortSender
{
    public static void respondHandshake(int toPort, Node responder) throws IOException
    {
        Message        message = new Message(MessageType.PeerResponse, responder, null);
        byte[]         buffer  = message.serialize();
        DatagramPacket packet  = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), toPort);
        new DatagramSocket().send(packet);
    }

}
