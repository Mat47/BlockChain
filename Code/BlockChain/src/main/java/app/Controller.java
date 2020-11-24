package app;

import ledger.*;
import network.*;
import util.SigKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.util.HashSet;
import java.util.Set;

public class Controller
{
    public static Wallet     wallet;
    public static Blockchain blockchain;
    public static WorldState worldState;

    public static void launchApp(Node node, KeyPair keyPair) throws IOException, InterruptedException
    {
        wallet = new Wallet(keyPair);
        blockchain = new Blockchain();
        worldState = new WorldState();

//        JsonMapper mapper = new JsonMapper(wallet.getPub().getEncoded());
//        System.out.println(mapper.stringify());
//        System.out.println(mapper.serialize());

        //
        // server threads setup
        //
        MulticastReceiver mcListener = new MulticastReceiver(node);
        mcListener.setDaemon(true);
        mcListener.start();

        PortReceiver portListener = new PortReceiver(node);
        portListener.setDaemon(true);
        portListener.start();


        //
        MulticastSender.requestHandshake(node);
//        MulticastSender.requestChainSync(node);


        //
        BufferedReader reader            = new BufferedReader(new InputStreamReader(System.in));
        String         userMenuSelection = "h";
        while (!userMenuSelection.equals("0"))
        {
            switch (userMenuSelection)
            {
                case "h":
                    UI.display();
                    break;

                case "1":
                    System.out.println("_Peers " + PeerInfo.getPeers());
                    break;

                case "2":
                    Explorer.print(blockchain);
                    System.out.println(worldState.getAccounts());
                    break;

                case "3":
                    TxProposal txProposal = TxProposal.createTxPropUI(node);
                    PeerInfo.activeProposals.put(txProposal, new HashSet<>());

                    byte[] sig = wallet.sign(txProposal);
                    PortSender.proposeTx(node, txProposal, sig, wallet.getPub());
                    Thread.sleep(1900);
                    // waiting 2 seconds before checking if proposal was endorsed
                    if (PeerInfo.activeProposals.get(txProposal).size() >= Config.endorsers.size()/2)   // 50% endorsement policy
                    {
                        System.out.println("EndorsementStatus " + PeerInfo.activeProposals);
                        Transaction tx = new Transaction( txProposal, new SigKey(sig, wallet.getPub()) );
                        tx.setEndorsements(PeerInfo.activeProposals.get(txProposal));
                        PortSender.submitTxProposal(node, tx);
                    } else {
                        System.out.println(PeerInfo.activeProposals.get(txProposal) + " missing endorsements, forfeiting proposal.");
                    }
                    PeerInfo.activeProposals.remove(txProposal);
//                    blockchain.addTransaction(tx);
                    break;

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
