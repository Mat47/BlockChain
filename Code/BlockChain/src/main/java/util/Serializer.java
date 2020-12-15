package util;

import app.Node;
import ledger.Blockchain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;

/**
 * Implements persistent storage feature.
 */
public class Serializer
{
    private static final Logger logger = LoggerFactory.getLogger(Serializer.class);

    public static void serialize(Node node, KeyPair keyPair, Blockchain blockchain)
    {
        String dir  = "./" + node.getPort() + "/";
        File   file = new File(dir);
        if (file.mkdir())
        {
            logger.info("created local user directory {}", dir);
        }

        // serialize params into dir
        serialize(node);
        serialize(node, keyPair);
        serialize(node, blockchain);
        logger.info("user blockchain information locally saved at {}", dir);
    }

    public static void serialize(Node node, Blockchain chain)
    {
        String path = "./" + node.getPort() + "/chain.ser";

        FileOutputStream   fileOut   = null;
        ObjectOutputStream objectOut = null;

        try
        {
            fileOut = new FileOutputStream(path);
            objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(chain);
            objectOut.close();

        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static void serialize(Node node, KeyPair keyPair)
    {
        String path = "./" + node.getPort() + "/keypair.ser";

        FileOutputStream fileOut = null;
        ObjectOutputStream objectOut = null;

        try
        {
            fileOut = new FileOutputStream(path);
            objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(keyPair);
            objectOut.close();

        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static void serialize(Node node)
    {
        String fileSerial = "./" + node.getPort() + "/node.ser";

        FileOutputStream   fileOut   = null;
        ObjectOutputStream objectOut = null;

        try
        {
            fileOut = new FileOutputStream(fileSerial);
            objectOut = new ObjectOutputStream(fileOut);

            objectOut.writeObject(node);
            objectOut.close();

        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

}
