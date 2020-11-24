package app;

import accessPermission.Role;
import network.Config;
import util.Sha256Hasher;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Node
{
    private int       port;
    private String    username;
    private Set<Role> rights;
    private String    address;

    public Node()
    {
        // needed for (de)serialization (Jackson)
    }

    public Node(int port, String username, PublicKey pub)
    {
        this.port = port;
        this.username = username;

        this.rights = new HashSet<>();
        initRights();

        this.address = Sha256Hasher.hash(new String(pub.getEncoded()));
    }

    @Override
    public String toString()
    {
        return "Node{" +
                "port=" + port +
                ", username='" + username + '\'' +
                ", rights=" + rights +
                ", address='" + address + '\'' +
                '}';
    }

    private void initRights()
    {
        rights.add(Role.PEER);
        if (Config.admins.contains(port))
        {
            rights.add(Role.ADMIN);
        }
        if (Config.orderers.contains(port))
        {
            rights.add(Role.ORDERER);
        }
        if (Config.endorsers.contains(port))
        {
            rights.add(Role.ENDORSER);
        }
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

    public Set<Role> getRights()
    {
        return rights;
    }

    public String getAddress()
    {
        return address;
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
