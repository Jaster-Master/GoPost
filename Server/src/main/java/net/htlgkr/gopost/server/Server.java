package net.htlgkr.gopost.server;

import net.htlgkr.gopost.packet.LoginPacket;
import net.htlgkr.gopost.util.Encrypt;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static final Map<Long, ClientConnection> clients = new HashMap<>();
    private long id = 0;
    private ServerSocket serverSocket;
    private static final int PORT = 6666;
    private boolean running = true;
    public void startServer(String[] args) {
        id = getIdFromDB();
        PrintWriter writer = null;
        try {
            serverSocket = new ServerSocket(PORT);
            writer = new PrintWriter(new FileWriter("log.txt"), true);
            System.out.println("Running");
            writer.println("" + LocalDateTime.now() + " ; com.goblin.Server Running");
            while (running) {
                Socket clientSocket = serverSocket.accept();
                writer.println("" + LocalDateTime.now() + " ; Client connected");
                ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
                ClientConnection clientConnection = new ClientConnection(this, clientSocket, outputStream, inputStream);
                Thread clientThread = new Thread(clientConnection);
                clientThread.start();
                addToClients(clientConnection);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.println("" + LocalDateTime.now() + " ; com.goblin.Server Stopped");
            writer.close();
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private long getIdFromDB() {
        // TODO implement get Primary Key From DB
        return 0;
    }



    public void closeServer() {
       running=false;
    }

    public void addToClients(ClientConnection clientConnection) {
//        long userId = nextUserId();
//        clientConnection.setUserId(userId);
//        clients.put(clientConnection.getUserId(), clientConnection);
    }
}
