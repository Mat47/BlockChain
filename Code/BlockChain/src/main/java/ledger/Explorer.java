package ledger;

public class Explorer
{
    public static void print(Blockchain bc)
    {
//        String state = bc.isStateValid() ? "VALID" : "INVALID";
//        System.out.println("-ChainState:" + state);
        for (Block b : bc.getChain())
        {
            System.out.println(b);
        }

//        System.out.println("-MemPool");
//        for (Transaction tx : bc.getMempool())
//        {
//            System.out.println(tx);
//        }
    }
}
