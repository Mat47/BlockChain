package ledger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorldState
{
    private final Logger logger = LoggerFactory.getLogger(WorldState.class);

    private Map<String, Double> balances;   // hashMap<address, balance>
    private Map<String, Asset> assets;

    private List<Transaction>   mempool;

    public WorldState()
    {
        this.balances = new HashMap<>();
        this.mempool = new ArrayList<>();
    }

    public Map<String, Double> getBalances()
    {
        return balances;
    }

    public void update(List<Transaction> mempool)
    {
        for (Transaction tx : mempool)
        {
            double amount = tx.getTxProposal().getAmount();
            String from   = tx.getTxProposal().getFromAddress();
            String to     = tx.getTxProposal().getToAddress();

            balances.put(from, balances.get(from) - amount);
            balances.put(to, balances.get(to) + amount);
        }
        this.mempool.clear();
        logger.info("Updated world state, mempool cleared.");
    }

    /**
     * Fetches the address' account balance taking the mempool into consideration hence avoiding double-spend
     *
     * @param address the account
     * @return address' balance
     */
    public double fetchBalance(String address)
    {
        double balance = balances.get(address);
        for (Transaction tx : mempool)
        {
            if (tx.getTxProposal().getFromAddress().equals(address))
            {
                balance -= tx.getTxProposal().getAmount();
            }
        }
        return balance;
    }

//    public void addAccount(Wallet w)
//    {
//        accounts.add(w);
//    }

//    public Wallet fetchAccount(String address)
//    {
//        for (Wallet wallet : accounts)
//        {
//            if (wallet.getAddress().equals(address))
//            {
//                return wallet;
//            }
//        }
//        return null;
//    }

    public List<Transaction> getMempool()
    {
        return mempool;
    }
}
