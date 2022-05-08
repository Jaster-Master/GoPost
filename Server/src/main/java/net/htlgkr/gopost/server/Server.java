package net.htlgkr.gopost.server;

import net.htlgkr.gopost.database.DBHandler;
import net.htlgkr.gopost.util.ObservableValue;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Server {

    public static final Map<Long, ClientConnection> CLIENTS = new HashMap<>();
    public static ServerSocket serverSocket;
    public static final int PORT = 16663;
    public static ObservableValue<Boolean> isRunning;
    public static final DBHandler DB_HANDLER = new DBHandler();
    public static final String TEMPLATE_URL = "https://gopost.zeige.info/";
    public static final String GOPOST_ICON = "ic_image_gopost_icon";

    public static void startServer(String[] args) {
        isRunning = new ObservableValue<>(true);
        PrintWriter writer = null;
        try {
            //serverSocket.setSoTimeout(10000);
            serverSocket = new ServerSocket(PORT);
            writer = new PrintWriter(new FileWriter("log.txt"), true);
            System.out.println("Running");
            writer.println(LocalDateTime.now() + " ; Server started");
            waitForClientConnection(writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.println(LocalDateTime.now() + " ; Server stopped");
                writer.close();
            }
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void waitForClientConnection(PrintWriter writer) {
        while (isRunning.getValue()) {
            try {
                Socket clientSocket = serverSocket.accept();
                writer.println(LocalDateTime.now() + " ; Client connected");
                System.out.println(LocalDateTime.now() + " ; Client connected");
                ClientConnection clientConnection = new ClientConnection(clientSocket);
                new Thread(clientConnection).start();
            } catch (Exception e) {
                if (e instanceof SocketTimeoutException)
                    System.out.println("Timeout");
                else
                    e.printStackTrace();
            }
        }
    }

    public static void addClient(ClientConnection connection) {
        CLIENTS.put(connection.getUserId(), connection);
    }

    public static void closeServer() {
        isRunning.setValue(false);
    }
}
