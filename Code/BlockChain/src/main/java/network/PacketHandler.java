package network;

import app.TxProposal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ledger.Block;
import ledger.BlockHeader;
import ledger.Transaction;

import java.net.DatagramPacket;
import java.util.Arrays;
import java.util.List;
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
        String  received = new String(packet.getData());
        return mapper.readValue(received, Message.class);
    }

    public static TxProposal parseTxProp(Message message) throws JsonProcessingException
    {
        return mapper.readValue(mapper.writeValueAsString(message.getPayload()), TxProposal.class);
    }

    public static Transaction parseTx(Message message) throws JsonProcessingException
    {
        return mapper.readValue(mapper.writeValueAsString(message.getPayload()), Transaction.class);
    }

    public static Block parseBlock(Message message) throws JsonProcessingException
    {
        BlockHeader       header = mapper.readValue(mapper.writeValueAsString(message.getPayload()), BlockHeader.class);

        String jsonTXs = mapper.writeValueAsString(message.getTransactions());
        List<Transaction> txs = mapper.readValue(jsonTXs, List.class);
        List<Transaction> parsed = mapper.convertValue(txs, new TypeReference<List<Transaction>>() {});
        Block             b      = new Block(header, parsed, message.getSigKey());
//        System.out.println("PARSE " + b);
        return b;
    }

    public static BlockHeader parseBlockHeader(Message message) throws JsonProcessingException
    {
        return mapper.readValue(mapper.writeValueAsString(message.getPayload()), BlockHeader.class);
    }
}
