package network;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Config
{
    public static final int    multicastPort = 4999;
    public static final String multicastHost = "230.0.0.0";

    public static final Set<Integer> admins    = new HashSet<>(Arrays.asList(5000));
    public static final Set<Integer> orderers  = new HashSet<>(Arrays.asList(5000));
    public static final Set<Integer> endorsers = new HashSet<>(Arrays.asList(5010, 5011));
//    public static final Set<Integer> endorsers = new HashSet<>(Arrays.asList(5010, 5011, 5012, 5013));

    public static int getRandOrderer()
    {
        int size = orderers.size();
        int item = new Random().nextInt(size); // In real life, the Random object should be rather more shared than this
        int i = 0;
        for(Integer obj : orderers)
        {
            if (i == item)
                return obj;
            i++;
        }
        return 5000;
    }
}
