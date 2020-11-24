package network;

import app.Node;
import com.fasterxml.jackson.core.JsonProcessingException;
import util.JsonMapper;

public class Message<T>
{
    private MessageType msgType;
    private Node        sender;
    private T payload;

    public Message()
    {
        // needed for (de)serialization (Jackson)
    }

    public Message(MessageType msgType, Node sender, T payload)
    {
        this.msgType = msgType;
        this.sender = sender;
        this.payload = payload;
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

}
