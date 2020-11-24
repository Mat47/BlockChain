import accessPermission.CertificateAuthority;
import accessPermission.MembershipServiceProvider;
import app.Node;
import app.UI;
import app.Wallet;
import com.fasterxml.jackson.core.JsonProcessingException;
import network.MulticastReceiver;
import network.MulticastSender;
import network.PeerInfo;
import network.PortReceiver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;

public class Main
{
    public static Node node;
    //public static chain

    public static void main(String[] args) throws IOException
    {
        // getting a public key
        CertificateAuthority ca = new CertificateAuthority("GoDaddy");
        KeyPair digitalId = ca.issueCertificate();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            System.out.print("Enter your port number > ");
            int nodePort = Integer.parseInt(reader.readLine());

            System.out.print("Enter your username > ");
            String username = reader.readLine();

            // requesting permission
            MembershipServiceProvider msp = new MembershipServiceProvider();
            msp.apply(digitalId.getPublic(), nodePort, username);

            node = new Node(nodePort, username);

        } catch (IOException e)
        {
            System.out.println("Please enter a port no > 5000.");
            e.printStackTrace();
        }

        Wallet wallet = new Wallet(digitalId);
//        JsonMapper mapper = new JsonMapper(wallet.getPub().getEncoded());
//        System.out.println(mapper.stringify());
//        System.out.println(mapper.serialize());

        //
        // server threads setup
        //
        MulticastReceiver mcListener = new MulticastReceiver(node);
        mcListener.setDaemon(true);
        mcListener.start();

        PortReceiver portListener = new PortReceiver(node.getPort());
        portListener.setDaemon(true);
        portListener.start();


        //
        MulticastSender.requestHandshake(node);
        //chainsyncreq


        //
        String userMenuSelection = "h";
        while (!userMenuSelection.equals("0"))
        {
            switch (userMenuSelection)
            {
                case "h":
                    UI.display();
                    break;

                case "1":
                    System.out.println(node + "\tPeers " + PeerInfo.getPeers());
                    break;

//                case "2":
//                    Explorer.print(blockchain);
//                    break;
//
//                case "3":
//                    Transaction tx = Transaction.createTxUI(node);
//                    multicastClient.requestTransaction(tx, wallet.signTx(tx), wallet.getPubKey());
//
//                    blockchain.addTransaction(tx);
//                    break;
//
//                case "9":
//                    if( msp.hasAdminRights(node.getUsername()) )
//                    {
//                        blockchain.processMempool(username);
//                        multicastClient.sendNewBlock(blockchain.getLatestBlock());
//                    } else
//                    {
//                        System.out.println("You are not authorised to mine/order blocks.");
//                    }
//                    break;
//
//                case "keys": //todo delete
//                    System.out.println("private: " + wallet.getPrivKey());
//                    System.out.println("public: " + wallet.getPubKey());

                default:
                    //
            }

            System.out.print("> ");
            userMenuSelection = reader.readLine();
        }
    }

}
