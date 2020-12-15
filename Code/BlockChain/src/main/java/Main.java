import accessPermission.CertificateAuthority;
import accessPermission.MembershipServiceProvider;
import app.Controller;
import app.Node;
import ledger.Blockchain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.Deserializer;
import util.Serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;

public class Main
{
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    private static Node node;
    private static Blockchain blockchain;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        KeyPair digitalId = null;

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            System.out.print("\tenter your port number > ");
            int nodePort = Integer.parseInt(reader.readLine());

            System.out.print("\tenter your username > ");
            String username = reader.readLine();

            Node n = new Node(nodePort, username);

            // getting a public key
            CertificateAuthority ca = new CertificateAuthority("GoDaddy");

            // check for local data if user/node is already registered
            final String dir  = "./" + nodePort;
            Path         path = Paths.get(dir);
            if (Files.exists(path))
            {
                // deserialize existing/local data
                digitalId = Deserializer.deserializeKeyPair(n);
                node = Deserializer.deserializeNode(n);
                blockchain = Deserializer.deserializeChain(n);
            }
            else {
                // new user
                digitalId = ca.issueCertificate();
                node = new Node(nodePort, username, digitalId.getPublic());
                blockchain = new Blockchain();

                // requesting permission
                MembershipServiceProvider msp = new MembershipServiceProvider();
                msp.apply(digitalId.getPublic(), nodePort, username);
            }

//            node = new Node(nodePort, username, digitalId.getPublic());

        } catch (IOException e)
        {
            System.out.println("Please enter a port no > 5000.");
            e.printStackTrace();
        }

        Controller.launchApp(node, digitalId, blockchain);
        Serializer.serialize(node, Controller.wallet.getKeyPair(), Controller.blockchain);  // persistent/local store
    }

}
