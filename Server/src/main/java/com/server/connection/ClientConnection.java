package com.server.connection;

import com.server.packages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection implements Runnable{
    private Socket clientSocket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ObjectOutputStream getWriter() {
        return writer;
    }

    public ObjectInputStream getReader() {
        return reader;
    }

    public ClientConnection(Socket clientSocket, ObjectOutputStream writer, ObjectInputStream reader) {
        this.clientSocket = clientSocket;
        this.writer = writer;
        this.reader = reader;
    }

    @Override
    public void run() {
        boolean running = true;
        while (running) {
            try {
                Packet packet = (Packet) reader.readObject();
                if(packet instanceof BlockPacket blockPacket){
                    handleBlockPacket(blockPacket);
                }else if(packet instanceof LoginPacket loginPacket){
                    handleLoginPacket(loginPacket);
                }else if(packet instanceof PostPacket postPacket){
                    handlePostPacket(postPacket);
                }else if(packet instanceof ProfilePacket profilePacket){
                    handleProfilePacket(profilePacket);
                }else if(packet instanceof ReportPacket reportPacket){
                    handleReportPacket(reportPacket);
                }else if(packet instanceof StoryPacket storyPacket){
                    handleStoryPacket(storyPacket);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleStoryPacket(StoryPacket storyPacket) {
        String command = storyPacket.getCommand();
        switch (command){
            case "":
                break;
        }
    }

    private void handleReportPacket(ReportPacket reportPacket) {
        String command = reportPacket.getCommand();
        switch (command){
            case "":
                break;
        }
    }

    private void handleProfilePacket(ProfilePacket profilePacket) {
        String command = profilePacket.getCommand();
        switch (command){
            case "":
                break;
        }
    }

    private void handlePostPacket(PostPacket postPacket) {
        String command = postPacket.getCommand();
        switch (command){
            case "":
                break;
        }
    }

    private void handleLoginPacket(LoginPacket loginPacket) {
        String command = loginPacket.getCommand();
        switch (command){
            case "firstTimeLogin":
                loginPacket.writeUserIntoFile(loginPacket.getPassword());
                break;
            case "checkIfCorrectPassword":
                try {
                    writer.writeObject(loginPacket.readUserFromFile(loginPacket.getPassword()));
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void handleBlockPacket(BlockPacket blockPacket) {
        String command = blockPacket.getCommand();
        switch (command){
            case "":
                break;
        }
    }
}
