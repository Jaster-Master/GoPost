package net.htlgkr.gopost.client;

import android.util.Log;

import net.htlgkr.gopost.activity.LoginActivity;
import net.htlgkr.gopost.data.User;
import net.htlgkr.gopost.util.ObservableValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client {

    private static final String INFO_FILE_NAME = "connection.conf";
    private static int port;
    private static String ipAddress;
    private static final ObservableValue<Boolean> isConnected = new ObservableValue<>();
    private static Socket clientSocket;
    private static ServerConnection connection;
    private static User client;

    static {
        readInfo();
    }

    private static void readInfo() {
        try {
            InputStream inputStream = LoginActivity.instance.getAssets().open(INFO_FILE_NAME);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = reader.readLine();
            String[] info = line.split(":");
            ipAddress = info[0];
            port = Integer.parseInt(info[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean openConnection() {
        clientSocket = new Socket();
        try {
            clientSocket.connect(new InetSocketAddress(ipAddress, port));
            connection = new ServerConnection();
            isConnected.setValue(true);
            Log.e("AMONG", "SOCKET");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
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

    public static User getClient() {
        return client;
    }

    public static ObservableValue<Boolean> getIsConnected() {
        return isConnected;
    }

    public static Socket getClientSocket() {
        return clientSocket;
    }

    public static int getPort() {
        return port;
    }

    public static String getIpAddress() {
        return ipAddress;
    }

    public static ServerConnection getConnection() {
        return connection;
    }
}
