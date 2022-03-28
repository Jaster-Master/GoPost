package net.htlgkr.gopost.client;

import net.htlgkr.gopost.data.Profile;
import net.htlgkr.gopost.util.ObservableValue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {

    private Profile client;
    private ObservableValue<Boolean> isConnected;
    private Socket clientSocket;
    private final int port;
    private final String ipAddress;
    private ServerConnection connection;

    public Client(int port, String ipAddress) {
        this.port = port;
        this.ipAddress = ipAddress;
    }

    public boolean openConnection() {
        clientSocket = new Socket();
        try {
            clientSocket.connect(new InetSocketAddress(ipAddress, port));
            connection = new ServerConnection(this);
            isConnected.setValue(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean closeConnection() {
        try {
            clientSocket.close();
            connection = null;
            isConnected.setValue(false);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Profile getClient() {
        return client;
    }

    public ObservableValue<Boolean> getIsConnected() {
        return isConnected;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public int getPort() {
        return port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public ServerConnection getConnection() {
        return connection;
    }
}
