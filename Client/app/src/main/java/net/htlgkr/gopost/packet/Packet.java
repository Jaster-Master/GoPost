package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.data.User;

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

    public String getCommand() {
        return command;
    }

    public User getSentByUser() {
        return sentByUser;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public void setSentByUser(User sentByUser) {
        this.sentByUser = sentByUser;
    }
}
