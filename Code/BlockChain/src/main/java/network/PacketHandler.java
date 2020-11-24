package network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.DatagramPacket;
import java.util.logging.Logger;

public class PacketHandler
{
    private static Logger       logger = Logger.getLogger(PacketHandler.class.getName());
    private static ObjectMapper mapper = new ObjectMapper();

    public static Message parseMessage(DatagramPacket packet) throws JsonProcessingException
    {
        String received = new String(packet.getData());
        Message message = mapper.readValue(received, Message.class);
        return message;
    }

}
