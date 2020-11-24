package ledger;

import java.util.HashMap;
import java.util.Map;

public class WorldState
{
    private Map<String, Double> accounts;

    public WorldState()
    {
        this.accounts = new HashMap<>();
    }

    public Map<String, Double> getAccounts()
    {
        return accounts;
    }
}
