package app;

import java.util.Objects;

public class Node
{
    private int    port;
    private String username;

    public Node()
    {
        // needed for (de)serialization (Jackson)
    }

    public Node(int port, String username)
    {
        this.port = port;
        this.username = username;
    }

    @Override
    public String toString()
    {
        return "Node{" +
                "port=" + port +
                ", username='" + username + '\'' +
                '}';
    }

    //
    public int getPort()
    {
        return port;
    }

    public String getUsername()
    {
        return username;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Node)) return false;
        Node node = (Node) o;
        return getPort() == node.getPort() &&
                Objects.equals(getUsername(), node.getUsername());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getPort(), getUsername());
    }
}
