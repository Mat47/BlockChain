package network;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Config
{
    public static final int    multicastPort = 4999;
    public static final String multicastHost = "230.0.0.0";

    public static final Set<Integer> admins    = new HashSet<>(Arrays.asList(5000));
    public static final Set<Integer> orderers  = new HashSet<>(Arrays.asList(5000));
    public static final Set<Integer> endorsers = new HashSet<>(Arrays.asList(5010, 5011));
}
