package network;

import app.Controller;
import app.Node;
import ledger.Block;
import ledger.SmartContract;
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
                        if (!NetworkInfo.peers.contains(message.getSender()))
                        {
                            PortSender.respondHandshake(message.getSender().getPort(), node);
                            NetworkInfo.peers.add(message.getSender());
                            // adds address to chain accounts
                            Controller.worldState.getAccounts().put(message.getSender().getAddress(), 100.);    // adds initial starting balance for demo purposes
                        }
                        break;

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

                    case Disconnect:
                        NetworkInfo.peers.remove( message.getSender() );
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
