package net.htlgkr.gopost.server;

import net.htlgkr.gopost.database.DBHandler;
import net.htlgkr.gopost.util.ObservableValue;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.*;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static ClientConnection connection;
    public static final int PORT = 16663;
    public static ObservableValue<Boolean> isRunning;
    public static DBHandler DB_HANDLER = new DBHandler();
    public static final String TEMPLATE_URL = "https://gopost.zeige.info/";
    public static final String GOPOST_ICON = "ic_image_gopost_icon";

    public static void startServer(int port) {
        connection = new ClientConnection(port);
        connection.start();
        isRunning = new ObservableValue<>(true);
    }

    public static void closeServer(){
        try {
            connection.stop();
            isRunning.setValue(false);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
