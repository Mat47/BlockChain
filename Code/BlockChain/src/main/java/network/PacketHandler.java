package network;

import app.TxProposal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ledger.Transaction;
import util.JsonMapper;

import java.net.DatagramPacket;
import java.util.logging.Logger;

public class PacketHandler
{
    private static Logger       logger = Logger.getLogger(PacketHandler.class.getName());
    private static ObjectMapper mapper = new ObjectMapper();

//    public static MessageType parseMsgType(DatagramPacket packet) throws JsonProcessingException
//    {
//        MessageType messageType = mapper.readValue(mapper.writeValueAsString(packet.getData()), Me)
//    }

    public static Message parseMessage(DatagramPacket packet) throws JsonProcessingException
    {
        String received = new String(packet.getData());
        Message message = mapper.readValue(received, Message.class);
        return message;
    }

    public static TxProposal parseTxProp(Message message) throws JsonProcessingException
    {
        return mapper.readValue(mapper.writeValueAsString(message.getPayload()), TxProposal.class);
    }
}
