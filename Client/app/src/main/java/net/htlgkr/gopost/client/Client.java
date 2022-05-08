package net.htlgkr.gopost.client;

import net.htlgkr.gopost.activity.LoginActivity;
import net.htlgkr.gopost.data.User;
import net.htlgkr.gopost.util.ObservableValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import javax.net.SocketFactory;

public class Client {

    private static final String INFO_FILE_NAME = "connection.conf";
    private static int port;
    private static String ipAddress;
    private static final ObservableValue<Boolean> isConnected = new ObservableValue<>();
    private static ServerConnection connection;
    public static User client;

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

    public static boolean openConnection(SocketFactory socketFactory) {
        connection = new ServerConnection(ipAddress, port, socketFactory);
        try {
            connection.connectBlocking(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        isConnected.setValue(true);
        return true;
    }

    public static boolean closeConnection() {
        try {
            connection.closeBlocking();
            isConnected.setValue(false);
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static ObservableValue<Boolean> getIsConnected() {
        return isConnected;
    }

    public static boolean isConnected() {
        return isConnected.getValue();
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
