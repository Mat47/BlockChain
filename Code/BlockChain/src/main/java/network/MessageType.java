package network;

public enum MessageType
{
    PeerDiscovery,
    PeerResponse,

    ChainSyncRequest,
    ChainSyncResponse,

    TxProposal,
    TxEndorsement,
    TxSubmission,

    Disconnect,

    NewBlock
}
