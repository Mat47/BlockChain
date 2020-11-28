package app;

import ledger.*;
import network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Explorer;
import util.SigKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;
import java.util.HashSet;
import java.util.Iterator;

public class Controller
{
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

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

        Thread.sleep(600);  // waiting for setup to finish

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
                    System.out.println("- Peers ");
                    for (Node peer : PeerInfo.getPeers())
                    {
                        System.out.println(peer);
                    }
                    break;

                case "2":
                    Explorer.print(blockchain, worldState);
                    System.out.println();
                    Iterator<String> it = worldState.getAccounts().keySet().iterator();
                    while (it.hasNext())
                    {
                        String key = it.next();
                        System.out.println("Account: " + key + "\tBalance: " + worldState.getAccounts().get(key));
                    }
                    break;

                case "3":
                    TxProposal txProposal = TxProposal.createTxPropUI(node);
                    PeerInfo.activeProposals.put(txProposal, new HashSet<>());
                    // sign tx proposal
                    byte[] sig = wallet.sign(txProposal);
                    PortSender.proposeTx(node, txProposal, sig, wallet.getPub());
                    Thread.sleep(1600);
                    // waiting 2 seconds before checking if proposal was endorsed
                    if (PeerInfo.activeProposals.get(txProposal).size() >= Config.endorsers.size()/2)   // 50% endorsement policy
                    {
                        logger.debug("EndorsementStatus " + PeerInfo.activeProposals);
                        Transaction tx = new Transaction( txProposal, new SigKey(sig, wallet.getPub()) );
                        tx.setEndorsements(PeerInfo.activeProposals.get(txProposal));
                        PortSender.submitTxProposal(node, tx);
                    } else {
                        System.out.println(PeerInfo.activeProposals.get(txProposal) + " missing endorsements, forfeiting proposal.");
                    }
                    PeerInfo.activeProposals.remove(txProposal);
                    break;

                case "9":
//                    Block b = new Block()
                    Block b = SmartContract.orderNewBlock(node);
//                    Controller.blockchain.add(b);
                    MulticastSender.sendNewBlock(node, b);
                    break;

//                case "keys":
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
