package net.htlgkr.gopost.client;

import net.htlgkr.gopost.data.Profile;
import net.htlgkr.gopost.util.ObservableValue;

import java.net.Socket;

public class Client {

    private Profile client;
    private ObservableValue<Boolean> isConnected;
    private Socket clientSocket;
    private int port;
    private String ipAddress;
    private ServerConnection connection;

    public boolean openConnection(){
        return false;
    }

    public boolean closeConnection(){
        return false;
    }
}
