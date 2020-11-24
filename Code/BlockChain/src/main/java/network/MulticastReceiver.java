package network;

import app.Controller;
import app.Node;
import app.TxProposal;
import ledger.SmartContract;
import ledger.Transaction;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Logger;

public class MulticastReceiver extends Thread
{
    Logger logger = Logger.getLogger(MulticastReceiver.class.getName());

    protected MulticastSocket socket = null;
    protected Node node;

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
                byte[]         buffer   = new byte[2056];
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
                        logger.info("[MC-Receiver] incoming peer discovery from " + message.getSender());
                        if (!PeerInfo.peers.contains(message.getSender()))
                        {
                            PortSender.respondHandshake(message.getSender().getPort(), node);
                            PeerInfo.peers.add(message.getSender());
                            // adds address to chain accounts
                            Controller.worldState.getAccounts().put(message.getSender().getAddress(), 100.);
                        }
                        break;

                    case ChainSyncRequest:
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
