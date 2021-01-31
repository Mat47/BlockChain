package app;

import accessPermission.Role;
import ledger.*;
import network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Explorer;
import util.SigKey;

import java.io.*;
import java.security.KeyPair;
import java.util.HashSet;

/**
 * Central instance controlling the business logic.
 */
public class Controller
{
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);

    public static Wallet     wallet;
    public static Blockchain blockchain;
    public static WorldState worldState;

    public static void launchApp(Node node, KeyPair keyPair, Blockchain chain) throws IOException, InterruptedException
    {
        wallet = new Wallet(keyPair);
        blockchain = chain;
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
        //todo refactor handshake own thread
        try {
            Thread.sleep(1000);  // to finish handshake and initialize peers, avoids exception (IllegalArgumentException: Bound must be positive) PeerInfo.peers size 0
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        PortSender.requestChainSync(node, Controller.blockchain.getLatestBlock().getHeader());
//        Thread.sleep(700);  // to finish setup

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
                    for (Node peer : NetworkInfo.getPeers())
                    {
                        System.out.println(peer);
                    }
                    break;

                case "2":   // displays wallet info
                    System.out.println("Account/Address: " + wallet.getAddress());
                    wallet.getAssets().forEach((asset, balance) -> System.out.println(asset + ":\t" + balance));
                    break;

                case "3":
                    Explorer.print(Controller.blockchain, worldState);
                    break;

                case "4":
                    TxProposal txProposal = TxProposal.createTxPropUI(node, wallet);
                    NetworkInfo.activeProposals.put(txProposal, new HashSet<>());
                    // sign tx proposal
                    byte[] sig = wallet.sign(txProposal);
                    PortSender.proposeTx(node, txProposal, sig, wallet.getPub());
                    Thread.sleep(1100); // ...before checking if proposal was endorsed
                    if (NetworkInfo.activeProposals.get(txProposal).size() >= Config.endorsers.size()/2)   //50% endorsement policy
                    {
                        logger.debug("EndorsementStatus " + NetworkInfo.activeProposals);
                        Transaction tx = new Transaction(txProposal, new SigKey(sig, wallet.getPub()));
                        tx.setEndorsements(NetworkInfo.activeProposals.get(txProposal));
                        PortSender.submitTxProposal(node, tx);
                    } else
                    {
                        logger.info("missing endorsements, forfeiting proposal.");
                    }
                    NetworkInfo.activeProposals.remove(txProposal);
                    break;

                case "9":
                    if (!node.getRights().contains(Role.ORDERER))
                    {
                        System.out.println("You are not authorised to order blocks.");
                        break;
                    }
                    if (Controller.worldState.getMempool().isEmpty())
                    {
                        System.out.println("There are no pending transactions.");
                        break;
                    }
                    Block b = SmartContract.orderNewBlock(node);
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

        MulticastSender.sendDisconnect(node);   // informs peers about disconnect
    }

}
