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
        DBHandler dbHandler = new DBHandler();

        String name = "David";
        name.split(";");
        String profName = "dk";
        String email = "Kogler@nom.at";
        String password = "nomnom";
        boolean isPrivate = false;
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        dbHandler.executeStatementsOnDB("INSERT INTO GoUser(GoUserName,GoProfileName,GoUserEmail,GoUserPassword,GoUserIsPrivate,GoUserDateTime) VALUES(?,?,?,?,?,?)", name, profName, email, password, isPrivate, timestamp);
        List<DBObject> list = dbHandler.readFromDB("Select * FROM GoUser WHERE GoUserId = ?",  "1","1;BigInt", "2;String", "3;String");
        for (DBObject o : list) {
            System.out.println(o.getObject().toString());
        }
        //runningServer = new Server();
        //runningServer.startServer(args);
    }
}
