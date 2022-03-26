package com.server.packages;

import com.server.user.User;

import java.beans.XMLEncoder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;

public class Packet implements Serializable {
    private String command;
    private User sentByUser;

    public Packet() {
    }

    public Packet(String command, User sentByUser) {
        this.command = command;
        this.sentByUser = sentByUser;
    }
    public XMLEncoder getXMLEncoder(){
        try {
            return new XMLEncoder(new FileOutputStream("userLoginData.xml",true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
    public String getCommand(){
        return command;
    }

    public User getSentByUser(){
        return sentByUser;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setSentByUser(User sentByUser) {
        this.sentByUser = sentByUser;
    }
}
