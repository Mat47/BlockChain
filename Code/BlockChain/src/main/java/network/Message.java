package network;

import app.Node;
import com.fasterxml.jackson.core.JsonProcessingException;
import util.JsonMapper;
import util.SigKey;

public class Message<T>
{
    private MessageType msgType;
    private Node        sender;
    private T           payload;
    private SigKey      sigKey;

    public Message()
    {
        // needed for (de)serialization (Jackson)
    }

    public Message(MessageType msgType, Node sender, T payload)
    {
        this.msgType = msgType;
        this.sender = sender;
        this.payload = payload;
        this.sigKey = null;
    }

    public Message(MessageType msgType, Node sender, T payload, SigKey sigKey)
    {
        this.msgType = msgType;
        this.sender = sender;
        this.payload = payload;
        this.sigKey = sigKey;
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

    public SigKey getSigKey()
    {
        return sigKey;
    }
}
