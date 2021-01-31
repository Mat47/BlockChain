package util;

import accessPermission.CertificateAuthority;
import app.TxProposal;
import app.Wallet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import ledger.Asset;
import ledger.Transaction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonMapperTest
{
    private static List<Transaction> transactions;
    private static Transaction       tx1;
    private static Transaction tx2;
    private static Transaction tx3;

    @BeforeAll
    static void beforeAll()
    {
        CertificateAuthority ca     = new CertificateAuthority("GeoTrust");
        Wallet               wallet = new Wallet(ca.issueCertificate());

        TxProposal txp1 = new TxProposal("hugh", "janus", Asset.BTC, 75);
        tx1 = new Transaction(txp1, new SigKey(wallet.sign(txp1), wallet.getPub()));

        TxProposal txp2 = new TxProposal("frank", "lis", Asset.BTC, 50);
        tx2 = new Transaction(txp2, new SigKey(wallet.sign(txp2), wallet.getPub()));

        TxProposal txp3 = new TxProposal("baly", "chad", Asset.BTC, 349);
        tx3 = new Transaction(txp3, new SigKey(wallet.sign(txp3), wallet.getPub()));

        transactions = new ArrayList<>();
        transactions.add(tx1);
        transactions.add(tx2);
//        transactions.add(tx3);
    }

    @Test
    void deserializeList() throws JsonProcessingException
    {
        System.out.println("INIT\n" + transactions);
        JsonMapper mapper = new JsonMapper();

        String txStr = mapper.writeValueAsString(transactions);
        System.out.println("\nTXs AS STR\n" + txStr);

        byte[] txSerial = mapper.writeValueAsBytes(transactions);

        List<Transaction> txs = mapper.readValue(new String(txSerial), List.class);
        List<Transaction> parsed = mapper.convertValue(txs, new TypeReference<List<Transaction>>() {});
        for( Transaction tx : parsed )
        {}
        System.out.println("\nDESERIAL\n" + parsed);
    }
}