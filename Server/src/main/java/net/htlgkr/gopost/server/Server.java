package net.htlgkr.gopost.server;

import net.htlgkr.gopost.util.ObservableValue;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static final Map<Long, ClientConnection> CLIENTS = new HashMap<>();
    private ServerSocket serverSocket;
    private static final int PORT = 10443;
    private ObservableValue<Boolean> isRunning;

    public void startServer(String[] args) {
        isRunning = new ObservableValue<>(true);
        PrintWriter writer = null;
        try {
            serverSocket = new ServerSocket(PORT);
            writer = new PrintWriter(new FileWriter("log.txt"), true);
            System.out.println("Running");
            writer.println(LocalDateTime.now() + " ; Server started");
            waitForClientConnection(writer);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.println(LocalDateTime.now() + " ; Server stopped");
            writer.close();
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void waitForClientConnection(PrintWriter writer) {
        while (isRunning.getValue()) {
            try {
                Socket clientSocket = serverSocket.accept();
                writer.println(LocalDateTime.now() + " ; Client connected");
                ClientConnection clientConnection = new ClientConnection(this, clientSocket);
                new Thread(clientConnection).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addClient(ClientConnection connection) {
        CLIENTS.put(connection.getUserId(), connection);
    }

    public void closeServer() {
        isRunning.setValue(false);
    }
}
