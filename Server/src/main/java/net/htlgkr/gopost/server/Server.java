package net.htlgkr.gopost.server;

import net.htlgkr.gopost.file.FileHandler;
import net.htlgkr.gopost.file.FileObject;
import net.htlgkr.gopost.packet.LoginPacket;

import java.beans.XMLEncoder;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static final Map<Long, ClientConnection> clients = new HashMap<>();
    private long counter = 0;
    private ServerSocket serverSocket;
    private static final int PORT = 6666;

    public void startServer(String[] args) {
        counter = getCounterFromFile();
        PrintWriter writer = null;
        try {
            serverSocket = new ServerSocket(PORT);
            writer = new PrintWriter(new FileWriter("log.txt"), true);
            System.out.println("Running");
            writer.println("" + LocalDateTime.now() + " ; com.goblin.Server Running");
            while (true) {
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

    private long getCounterFromFile() {
        FileHandler fileHandler = new FileHandler("counter.xml");
        FileObject counterObject = new FileObject(counter, false);
        return (long) fileHandler.readFromFile(counterObject);
    }

    public boolean closeServer() {
        return false;
    }

    public boolean closeConnection() {
        return false;
    }

    public void addToClients(ClientConnection clientConnection) {
        long userId = nextUserId();
        clientConnection.setUserId(userId);
        clients.put(clientConnection.getUserId(), clientConnection);
    }

    public long nextUserId() {
        return 0;
    }

    public void writeUserIntoFile(LoginPacket loginPacket) {
        FileObject loginObject = new FileObject(loginPacket, true);
        FileHandler writerRead = new FileHandler("userLoginData.xml", getXMLEncoder(), loginObject);
        writerRead.writeIntoFile();
    }

    public Object readUserFromFile(LoginPacket loginPacket) {
        FileHandler writerRead = new FileHandler();
        FileObject loginObject = new FileObject(loginPacket, true);
        return writerRead.readFromFile(loginObject);
    }

    public XMLEncoder getXMLEncoder() {
        try {
            return new XMLEncoder(new FileOutputStream("userLoginData.xml", true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }
}
