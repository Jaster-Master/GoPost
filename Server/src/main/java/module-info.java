module Server {
    requires mysql.connector.java;
    requires org.json;

    exports net.htlgkr.gopost.data;
    exports net.htlgkr.gopost.database;
    exports net.htlgkr.gopost.notification;
    exports net.htlgkr.gopost.packet;
    exports net.htlgkr.gopost.server;
    exports net.htlgkr.gopost.util;
}