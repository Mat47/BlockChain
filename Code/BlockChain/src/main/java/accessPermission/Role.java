package accessPermission;

import java.io.Serializable;

public enum Role implements Serializable
{
    ADMIN,
    ORDERER,
    ENDORSER,
    PEER
}
