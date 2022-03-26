package com.server.packages;

import com.server.file.FileObject;
import com.server.file.FileHandler;
import com.server.user.User;

public class LoginPacket extends Packet{
    private String userName;
    private String email;
    private String password;

    public LoginPacket() {
    }

    public LoginPacket(String command, User sentByUser, String userName, String email, String password) {
        super(command, sentByUser);
        this.userName = userName;
        this.email = email;
        this.password=password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void writeUserIntoFile(String password) {
        FileHandler writerRead = new FileHandler("userLoginData.xml",getXMLEncoder(),new FileObject(password,true),
                new FileObject(userName,false),
                new FileObject(email,false));
        writerRead.writeIntoFile();
    }

    public Object readUserFromFile(String password) {
        FileHandler writerRead = new FileHandler();
        return writerRead.readFromFile(new FileObject(password,true));
    }
}
