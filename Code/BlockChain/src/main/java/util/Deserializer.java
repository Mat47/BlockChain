package util;

import app.Node;
import ledger.Blockchain;
import ledger.WorldState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;

public class Deserializer
{
    private static final Logger logger = LoggerFactory.getLogger(Deserializer.class);

    public static Blockchain deserializeChain(Node node)
    {
        Blockchain chain = null;
        FileInputStream   fileIn   = null;
        ObjectInputStream objectIn = null;
        String path = "./" + node.getPort() + "/chain.ser";

        try
        {
            fileIn = new FileInputStream(path);
            objectIn = new ObjectInputStream(fileIn);
            chain = (Blockchain) objectIn.readObject();
            System.out.print("> deserialized chain: ");
            Explorer.print(chain, new WorldState());
            return chain;

        } catch (IOException | ClassNotFoundException ioe)
        {
            logger.info("no local chain, new user/node.");
//            ioe.printStackTrace();
        }
        return null;
    }

    public static KeyPair deserializeKeyPair(Node node)
    {
        KeyPair           keyPair;
        FileInputStream   fileIn   = null;
        ObjectInputStream objectIn = null;
        String path = "./" + node.getPort() + "/keypair.ser";

        try
        {
            fileIn = new FileInputStream(path);
            objectIn = new ObjectInputStream(fileIn);
            keyPair = (KeyPair) objectIn.readObject();
            System.out.println("> deserialized key pair: " + keyPair);
//            System.out.println("pub: " + keyPair.getPublic());
//            System.out.println("priv: " + keyPair.getPrivate());
            return keyPair;

        } catch (IOException | ClassNotFoundException ioe)
        {
            logger.info("no local key pair, new user/node.");
//            ioe.printStackTrace();
        }
        return null;
    }

    public static Node deserializeNode(Node node)
    {
        Node n;
        FileInputStream   fileIn   = null;
        ObjectInputStream objectIn = null;
        String path = "./" + node.getPort() + "/node.ser";

        try
        {
            fileIn = new FileInputStream(path);
            objectIn = new ObjectInputStream(fileIn);
            n = (Node) objectIn.readObject();
            System.out.println("> deserialized node: " + n);
            return n;

        } catch (IOException | ClassNotFoundException ioe)
        {
            ioe.printStackTrace();
        }
        return null;
    }

}
