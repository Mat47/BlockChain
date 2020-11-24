package util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import network.Message;

/**
 * Generic mapping to string and byte[] (serialization).
 *
 * @param <T> object to be mapped
 */
public class JsonMapper<T>
{
    ObjectMapper mapper;
    T obj;

    public JsonMapper(T obj)
    {
        mapper = new ObjectMapper();
        this.obj = obj;
    }

    public String stringify() throws JsonProcessingException
    {
        return mapper.writeValueAsString(obj);
    }

    public byte[] serialize() throws JsonProcessingException
    {
        return mapper.writeValueAsBytes(obj);
    }

    public String deserialize(byte[] received) throws JsonProcessingException
    {
        return mapper.writeValueAsString(received);
    }
}
