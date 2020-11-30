import accessPermission.CertificateAuthority;
import accessPermission.MembershipServiceProvider;
import app.Controller;
import app.Node;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyPair;

public class Main
{
    public static Node node;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        // getting a public key
        CertificateAuthority ca        = new CertificateAuthority("GoDaddy");
        KeyPair              digitalId = ca.issueCertificate();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            System.out.print("\tenter your port number > ");
            int nodePort = Integer.parseInt(reader.readLine());

            System.out.print("\tenter your username > ");
            String username = reader.readLine();

            // requesting permission
            MembershipServiceProvider msp = new MembershipServiceProvider();
            msp.apply(digitalId.getPublic(), nodePort, username);

            node = new Node(nodePort, username, digitalId.getPublic());

        } catch (IOException e)
        {
            System.out.println("Please enter a port no > 5000.");
            e.printStackTrace();
        }

        Controller.launchApp(node, digitalId);
    }

}
