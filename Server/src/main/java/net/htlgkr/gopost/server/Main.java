package net.htlgkr.gopost.server;

import net.htlgkr.gopost.database.DBHandler;
import net.htlgkr.gopost.util.Encrypt;

import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        DBHandler dbHandler = new DBHandler();
        String name = "David";
        String profName = "dk";
        String email = "Kogler@nom.at";
        String password = Encrypt.SHA512("nomnom");
        boolean isPrivate = false;
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        dbHandler.executeStatementsOnDB("INSERT INTO GoUser(GoUserName,GoProfileName,GoUserEmail,GoUserPassword,GoUserIsPrivate,GoUserDateTime) VALUES(?,?,?,?,?,?)",name,profName,email,password,isPrivate,timestamp);
        List<Object> list =dbHandler.readFromDB("Select * FROM GoUser","1;BigInt","2;String","3;String");
        for (Object o : list) {
            System.out.println(o.toString());
        }
        //new Server().startServer(args);
    }
}
