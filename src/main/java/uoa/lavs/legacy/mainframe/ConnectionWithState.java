package uoa.lavs.legacy.mainframe;

public interface ConnectionWithState extends Connection {
    // checks if the connection can communicate with the remote destination
    boolean isConnected();
}
