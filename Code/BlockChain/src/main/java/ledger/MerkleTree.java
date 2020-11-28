package ledger;

import app.TxProposal;
import util.Sha256Hasher;

import java.util.ArrayList;
import java.util.List;

/**
 * A binary hash tree which optimises data verification/synchronization.
 * A block's transactions are represented with a single hash value ("Merkle Root").
 * The Merkle Root maintains the data's integrity. <br>
 * Reference: https://www.youtube.com/watch?v=_hGa1jfx584
 */
public class MerkleTree
{
    private List<String> transactionHashes;

    public MerkleTree(List<Transaction> transactions)
    {
        this.transactionHashes = new ArrayList<>();
        transactions.forEach(tx -> {
            transactionHashes.add( Sha256Hasher.hash(tx.stringify()) );
        });
    }

    public List<String> getMerkleRoot()
    {
        return construct(this.transactionHashes);
    }

    /**
     * Recursively merges neighbouring hashes from transactions list.
     *
     * @param transactions list of all transaction hashes
     * @return list of merged neighbour hashes => last recursion returns single element, the root
     */
    private List<String> construct(List<String> transactions)
    {
        if (transactions.size() == 1) return transactions;  // merkle root found

        List<String> updatedList = new ArrayList<>();   // contains half as much elements after each recursion

        // merges neighbouring items
        for (int i=0; i < transactions.size()-1; i+=2)
        {
            updatedList.add( mergeHash(transactions.get(i), transactions.get(i+1)) );
        }

        // if odd # transaction, last item is hashed with itself
        if( transactions.size() % 2 == 1 )
        {
            updatedList.add( mergeHash(transactions.get(transactions.size()-1), transactions.get(transactions.size()-1)) );
        }

        return construct(updatedList);  // recursion
    }

    private String mergeHash(String leftHash, String rightHash)
    {
        String mergedHash = leftHash + rightHash;
        return Sha256Hasher.hash( mergedHash );
    }


    public List<String> getTransactionHashes()
    {
        return transactionHashes;
    }
}
