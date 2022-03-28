package net.htlgkr.gopost.server;

import net.htlgkr.gopost.database.DBHandler;
import net.htlgkr.gopost.packet.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class ClientConnection implements Runnable {

    private final Server server;
    private final Socket clientSocket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private long userId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public ObjectOutputStream getWriter() {
        return writer;
    }

    public ObjectInputStream getReader() {
        return reader;
    }

    public ClientConnection(Server server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
        try {
            this.writer = new ObjectOutputStream(clientSocket.getOutputStream());
            this.reader = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Object packet = reader.readObject();
                if (!(packet instanceof Packet)) continue;
                if (packet instanceof BlockPacket blockPacket) {
                    handleBlockPacket(blockPacket);
                } else if (packet instanceof LoginPacket loginPacket) {
                    handleLoginPacket(loginPacket);
                } else if (packet instanceof PostPacket postPacket) {
                    handlePostPacket(postPacket);
                } else if (packet instanceof ProfilePacket profilePacket) {
                    handleProfilePacket(profilePacket);
                } else if (packet instanceof ReportPacket reportPacket) {
                    handleReportPacket(reportPacket);
                } else if (packet instanceof StoryPacket storyPacket) {
                    handleStoryPacket(storyPacket);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void handleStoryPacket(StoryPacket storyPacket) {
        String command = storyPacket.getCommand();
        switch (command) {
            case "":
                break;
        }
    }

    private void handleReportPacket(ReportPacket reportPacket) {
        String command = reportPacket.getCommand();
        switch (command) {
            case "":
                break;
        }
    }

    private void handleProfilePacket(ProfilePacket profilePacket) {
        String command = profilePacket.getCommand();
        switch (command) {
            case "":
                break;
        }
    }

    private void handlePostPacket(PostPacket postPacket) {
        String command = postPacket.getCommand();
        switch (command) {
            case "":
                break;
        }
    }

    private void handleLoginPacket(LoginPacket loginPacket) {
        String command = loginPacket.getCommand();
        DBHandler dbHandler = new DBHandler();
        switch (command) {
            case "firstTimeLogin":
                String insertStatement = "INSERT INTO GoUser(GoUserName,GoProfileName,GoUserEmail,GoUserPassword,GoUserIsPrivate,GoUserDateTime) VALUES(?,?,?,?,?,?)";
                dbHandler.executeStatementsOnDB(insertStatement,
                        loginPacket.getUserName(),
                        loginPacket.getProfileName(),
                        loginPacket.getEmail(),
                        loginPacket.getPassword(),
                        Timestamp.valueOf(LocalDateTime.now()));
            case "checkIfCorrectPassword":
                String selectStatement = "SELECT GoUserId FROM GoUser WHERE GoUserName = ? AND GoUserPassword = ?";
                List<Object> result = dbHandler.readFromDB(selectStatement, loginPacket.getUserName(), loginPacket.getPassword(), "1;BigInt");
                setUserId((Long) result.get(0));
                server.addClient(this);
                break;
        }
    }

    private void handleBlockPacket(BlockPacket blockPacket) {
        String command = blockPacket.getCommand();
        switch (command) {
            case "":
                break;
        }
    }
}
