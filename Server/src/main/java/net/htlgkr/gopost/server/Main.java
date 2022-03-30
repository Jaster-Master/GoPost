package net.htlgkr.gopost.server;

import net.htlgkr.gopost.database.DBHandler;
import net.htlgkr.gopost.database.DBObject;

import java.sql.Blob;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static Server runningServer;

    public static void main(String[] args) {
        runningServer = new Server();
        runningServer.startServer(args);
    }
}
