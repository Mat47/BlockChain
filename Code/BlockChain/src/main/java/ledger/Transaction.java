package ledger;

import app.TxProposal;
import util.Sha256Hasher;
import util.SigKey;

import java.util.HashSet;
import java.util.Set;

public class Transaction
{
    private TxProposal  txProposal;
    private SigKey      sigKey;
    private String      txId;
    private Set<SigKey> endorsements;

    public Transaction()
    {
    }

    public Transaction(TxProposal txProposal, SigKey sigKey)
    {
        this.txProposal = txProposal;
        this.sigKey = sigKey;
        this.txId = Sha256Hasher.hash(txProposal.stringify());
        this.endorsements = new HashSet<>();
    }

    public String stringify()
    {
        return "Transaction{" +
                "txProposal=" + txProposal +
                ", sigKey=" + sigKey +
                ", txId='" + txId + '\'' +
                ", endorsements=" + endorsements +
                '}';
    }

    @Override
    public String toString()
    {
        return "Transaction{" +
                "txProposal=" + txProposal +
                ", sigKey=" + sigKey +
                ", txId='" + txId + '\'' +
                ", endorsements=" + endorsements +
                '}';
    }

    //
    public TxProposal getTxProposal()
    {
        return txProposal;
    }

    public SigKey getSigKey()
    {
        return sigKey;
    }

    public String getTxId()
    {
        return txId;
    }

    public Set<SigKey> getEndorsements()
    {
        return endorsements;
    }

    public void setEndorsements(Set<SigKey> endorsements)
    {
        this.endorsements = endorsements;
    }
}
