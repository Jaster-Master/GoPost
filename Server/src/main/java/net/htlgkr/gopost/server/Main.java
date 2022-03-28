package net.htlgkr.gopost.server;

public class Main {

    public static Server runningServer;

    public static void main(String[] args) {
        /*DBHandler dbHandler = new DBHandler();
        String name = "David";
        name.split(";");
        String profName = "dk";
        String email = "Kogler@nom.at";
        String password = "nomnom";
        boolean isPrivate = false;
        LocalDateTime localDateTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);
        dbHandler.executeStatementsOnDB("INSERT INTO GoUser(GoUserName,GoProfileName,GoUserEmail,GoUserPassword,GoUserIsPrivate,GoUserDateTime) VALUES(?,?,?,?,?,?)", name, profName, email, password, isPrivate, timestamp);
        List<Object> list = dbHandler.readFromDB("Select * FROM GoUser WHERE GoUserId=?", "1", "1;BigInt", "2;String", "3;String");
        for (Object o : list) {
            System.out.println(o.toString());
        }*/
        runningServer = new Server();
        runningServer.startServer(args);
    }
}
