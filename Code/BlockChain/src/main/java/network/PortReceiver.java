package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Logger;

public class PortReceiver extends Thread
{
    private static Logger logger = Logger.getLogger(PortReceiver.class.getName());

    private DatagramSocket serverSocket;
    private int            port;

    public PortReceiver(int port)
    {
        this.port = port;
    }

    @Override
    public void run()
    {
        logger.info("Thread (for receiving packets on port " + port + ") started.");

        try
        {
            serverSocket = new DatagramSocket(port);
            byte[] buffer = new byte[1024];

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
