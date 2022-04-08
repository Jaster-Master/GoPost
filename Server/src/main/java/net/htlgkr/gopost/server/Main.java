package net.htlgkr.gopost.server;

public class Main {

    public static Server runningServer;

    public static void main(String[] args) {
        runningServer = new Server();
        runningServer.startServer(args);
    }
}
