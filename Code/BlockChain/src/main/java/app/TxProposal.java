package app;

import util.Sha256Hasher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Objects;

public class TxProposal
{
    private String fromAddress;
    private String toAddress;
    private double amount;

    public TxProposal()
    {
    }

    public TxProposal(String fromAddress, String toAddress, double amount)
    {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
    }

    public static TxProposal createTxPropUI(Node user) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        String receiver = "";
        while (receiver.length() != Sha256Hasher.HASHLENGTH)
        {
            System.out.print("\tenter receiver's address > ");
            receiver = reader.readLine();
        }

        double amount = 0;
        while (amount <= 0)
        {
            System.out.print("\tenter amount > ");
            amount = Double.parseDouble(reader.readLine());
        }

        return new TxProposal(user.getAddress(), receiver, amount);
    }

    public String stringify()
    {
        return "{" +
                "fromAddress='" + fromAddress + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public String toString()
    {
        return "{" +
                "fromAddress='" + fromAddress + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof TxProposal)) return false;
        TxProposal that = (TxProposal) o;
        return Double.compare(that.getAmount(), getAmount()) == 0 &&
                Objects.equals(getFromAddress(), that.getFromAddress()) &&
                Objects.equals(getToAddress(), that.getToAddress());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(getFromAddress(), getToAddress(), getAmount());
    }

    //
    public String getFromAddress()
    {
        return fromAddress;
    }

    public String getToAddress()
    {
        return toAddress;
    }

    public double getAmount()
    {
        return amount;
    }

}
