package net.htlgkr.gopost.client;

import net.htlgkr.gopost.data.Profile;
import net.htlgkr.gopost.util.ObservableValue;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {

    private static Profile client;
    private static ObservableValue<Boolean> isConnected;
    private static Socket clientSocket;
    private static final int PORT = 10443;
    private static final String IP_ADDRESS = "80.243.162.116";
    private static ServerConnection connection;

    public static boolean openConnection() {
        clientSocket = new Socket();
        try {
            clientSocket.connect(new InetSocketAddress(IP_ADDRESS, PORT));
            connection = new ServerConnection();
            isConnected.setValue(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean closeConnection() {
        try {
            clientSocket.close();
            connection = null;
            isConnected.setValue(false);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static Profile getClient() {
        return client;
    }

    public static ObservableValue<Boolean> getIsConnected() {
        return isConnected;
    }

    public static Socket getClientSocket() {
        return clientSocket;
    }

    public static int getPort() {
        return PORT;
    }

    public static String getIpAddress() {
        return IP_ADDRESS;
    }

    public static ServerConnection getConnection() {
        return connection;
    }
}
