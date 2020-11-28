package ledger;

import accessPermission.CertificateAuthority;
import app.TxProposal;
import app.Wallet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Sha256Hasher;
import util.SigKey;

import java.util.ArrayList;
import java.util.List;

class MerkleTreeTest
{
    private static List<Transaction> transactions;
    private static Transaction tx1;
    private static Transaction tx2;
    private static Transaction tx3;

    @BeforeAll
    static void beforeAll()
    {
        CertificateAuthority ca = new CertificateAuthority("GeoTrust");
        Wallet wallet = new Wallet(ca.issueCertificate());
        transactions = new ArrayList<>();

        TxProposal txp1 = new TxProposal("hugh", "janus", 75);
        tx1 = new Transaction(txp1, new SigKey(wallet.sign(txp1), wallet.getPub()));

        TxProposal txp2 = new TxProposal("frank", "lis", 50);
        tx2 = new Transaction(txp2, new SigKey(wallet.sign(txp2), wallet.getPub()));

        TxProposal txp3 = new TxProposal("baly", "chad", 349.3);
        tx3 = new Transaction(txp3, new SigKey(wallet.sign(txp3), wallet.getPub()));
    }

    @BeforeEach
    void setUp()
    {
    }

    @Test
    void merkleRootCalculation_shouldProduceSameRootHashAsHashingIndividually()
    {
        String tx1hash = Sha256Hasher.hash(tx1.stringify());
        String tx2hash = Sha256Hasher.hash(tx2.stringify());
        String tx3hash = Sha256Hasher.hash(tx3.stringify());

        String tx12hash = Sha256Hasher.hash(tx1hash + tx2hash);
        String tx33hash = Sha256Hasher.hash(tx3hash + tx3hash);

        String testRootHash = Sha256Hasher.hash(tx12hash + tx33hash);
        System.out.println("manualRoot \t= " + testRootHash);

        transactions.add(tx1);
        transactions.add(tx2);
        transactions.add(tx3);
        MerkleTree merkleTree = new MerkleTree(transactions);
        String     merkleRoot = merkleTree.getMerkleRoot().get(0);
        System.out.println("merkleRoot \t= " + merkleRoot);

        Assertions.assertEquals(testRootHash, merkleRoot);
    }

}