package ledger;

public enum Asset
{
    BTC("Bitcoin", 20000),
    ETH("Ethereum", 500),
    Prop0815("PropertyToken", 12500);

    private String name;
    private double price;

    Asset(String name, double value)
    {
        this.name = name;
        this.price = value;
    }

}
