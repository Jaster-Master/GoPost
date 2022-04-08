package net.htlgkr.gopost.server;

import net.htlgkr.gopost.data.User;
import net.htlgkr.gopost.database.DBObject;
import net.htlgkr.gopost.packet.*;

import java.io.EOFException;
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
        while (!clientSocket.isClosed()) {
            try {
                Object readObject = reader.readObject();
                System.out.println("Object received");
                if (!(readObject instanceof Packet packet)) continue;
                System.out.println("Packet received: " + packet.getCommand());
                if (readObject instanceof UserPacket userPacket) {
                    handleBlockPacket(userPacket);
                } else if (readObject instanceof LoginPacket loginPacket) {
                    handleLoginPacket(loginPacket);
                } else if (readObject instanceof PostPacket postPacket) {
                    handlePostPacket(postPacket);
                } else if (readObject instanceof ProfilePacket profilePacket) {
                    handleProfilePacket(profilePacket);
                } else if (readObject instanceof ReportPacket reportPacket) {
                    handleReportPacket(reportPacket);
                } else if (readObject instanceof StoryPacket storyPacket) {
                    handleStoryPacket(storyPacket);
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                if (e instanceof EOFException) {
                    return;
                }
            }
        }
    }

    private void closeConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
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
        switch (command) {
            case "firstTimeLogin":
                System.out.println("FirstTimeLogin");
                String insertStatement = "INSERT INTO GoUser(GoUserName,GoProfileName,GoUserEmail,GoUserPassword,GoUserIsPrivate,GoUserDateTime) VALUES(?,?,?,?,?,?)";
                Server.DB_HANDLER.executeStatementsOnDB(insertStatement,
                        loginPacket.getUserName(),
                        loginPacket.getProfileName(),
                        loginPacket.getEmail(),
                        loginPacket.getPassword(),
                        false,
                        Timestamp.valueOf(LocalDateTime.now()));
            case "checkIfCorrectPassword":
                System.out.println("checkIfCorrectPassword");
                String selectStatement = "SELECT GoUserId, GoUserProfilePicture FROM GoUser WHERE GoUserName = ? AND GoUserPassword = ?";
                List<DBObject> result = Server.DB_HANDLER.readFromDB(selectStatement, loginPacket.getUserName(), loginPacket.getPassword(), "1;BigInt", "2;Blob");
                setUserId(result.get(0).getLong());
                server.addClient(this);

                User user = new User(userId, loginPacket.getUserName(), loginPacket.getProfileName(), loginPacket.getEmail(), loginPacket.getPassword(), result.get(1).getBlob());
                sendPacket(new Packet("answer", user));
        }
    }

    private void handleBlockPacket(UserPacket userPacket) {
        String command = userPacket.getCommand();
        switch (command) {
            case "":
                break;
        }
    }

    private boolean sendPacket(Packet packet) {
        try {
            writer.writeObject(packet);
            writer.flush();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
