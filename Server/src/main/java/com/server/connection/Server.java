package com.server.connection;

import com.server.file.FileHandler;
import com.server.file.FileObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {

    private static final Map<Long, ClientConnection> clients = new HashMap<>();
    private long counter=0;
    private ServerSocket serverSocket;
    private int port = 6666;
    public void startServer(String[] args){
        counter = getCounterFromFile();
        Scanner s = new Scanner(System.in);
        PrintWriter bw = null;
        try {
            serverSocket = new ServerSocket(port);
            bw = new PrintWriter(new FileWriter("log.txt"), true);
            System.out.println("Running");
            bw.println("" + LocalDateTime.now() + " ; com.goblin.Server Running");
            bw.flush();
            while (true) {
                Socket socket = serverSocket.accept();
                bw.println("" + LocalDateTime.now() + " ; Client connected");
                ClientConnection clientConnection = new ClientConnection(socket, new ObjectOutputStream(socket.getOutputStream()),new ObjectInputStream(socket.getInputStream()));
                Thread clientThread = new Thread(clientConnection);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bw.println("" + LocalDateTime.now() + " ; com.goblin.Server Stopped");
            bw.flush();
            bw.close();
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private long getCounterFromFile() {
            FileHandler fileHandler = new FileHandler("counter.xml");
        return (long) fileHandler.readFromFile(new FileObject(counter,false));
    }

    public boolean closeServer(){
        return false;
    }
    public boolean closeConnection(){
        return false;
    }
    public void addToClients(ClientConnection clientConnection){
        clients.put(clientConnection.getId(),clientConnection);
    }
    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }
}
