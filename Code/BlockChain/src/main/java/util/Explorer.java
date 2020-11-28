package util;

import ledger.Block;
import ledger.Blockchain;
import ledger.Transaction;
import ledger.WorldState;

public class Explorer
{
    public static void print(Blockchain bc, WorldState ws)
    {
//        String state = bc.isStateValid() ? "VALID" : "INVALID";
//        System.out.println("-ChainState:" + state);
        System.out.println("- Chain");
        for (int i=0; i < bc.getChain().size(); i++)
        {
            System.out.println(i + " " + bc.getChain().get(i));
        }

        System.out.println("- MemPool");
        for (Transaction tx : ws.getMempool())
        {
            System.out.println(tx.getTxProposal());
        }
    }
}
