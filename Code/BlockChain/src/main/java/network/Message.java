package network;

import app.Node;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import ledger.BlockHeader;
import ledger.Transaction;
import util.JsonMapper;
import util.SigKey;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message<T>
{
    private MessageType       msgType;
    private Node              sender;
    private T                 payload;
    private List<Transaction> transactions;
    private SigKey            sigKey;

    public Message()
    {
        // needed for (de)serialization (Jackson)
    }

    public Message(MessageType msgType, Node sender, T payload)
    {
        this.msgType = msgType;
        this.sender = sender;
        this.payload = payload;
        this.transactions = null;
        this.sigKey = null;
    }

    public Message(MessageType msgType, Node sender, T payload, List<Transaction> txs)
    {
        this.msgType = msgType;
        this.sender = sender;
        this.payload = payload;
        this.transactions = txs;
        this.sigKey = null;
    }

    public Message(MessageType msgType, Node sender, T payload, SigKey sigKey)
    {
        this.msgType = msgType;
        this.sender = sender;
        this.payload = payload;
        this.transactions = null;
        this.sigKey = sigKey;
    }

    public Message(MessageType newBlock, Node orderer, T header, List<Transaction> txs, SigKey ordererSig)
    {
        this.msgType = newBlock;
        this.sender = orderer;
        this.payload = header;
        this.transactions = txs;
        this.sigKey = ordererSig;
    }

    public String stringify() throws JsonProcessingException
    {
        JsonMapper mapper = new JsonMapper(this);
        return mapper.stringify();
    }

    public byte[] serialize() throws JsonProcessingException
    {
        JsonMapper mapper = new JsonMapper(this);
        return mapper.serialize();
    }

    @Override
    public String toString()
    {
        return "Message{" +
                "msgType=" + msgType +
                ", sender=" + sender +
                ", payload=" + payload +
                ", payload2=" + transactions +
                ", sigKey=" + sigKey +
                '}';
    }

    //
    public MessageType getMsgType()
    {
        return msgType;
    }

    public Node getSender()
    {
        return sender;
    }

    public T getPayload()
    {
        return payload;
    }

    public List<Transaction> getTransactions()
    {
        return transactions;
    }

    public SigKey getSigKey()
    {
        return sigKey;
    }
}
