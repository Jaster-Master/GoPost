package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.data.User;
import net.htlgkr.gopost.util.Command;

import java.io.Serializable;
import java.util.Objects;

public class Packet implements Serializable {
    private Command command;
    private User sentByUser;

    public Packet() {
    }

    public Packet(Command command, User sentByUser) {
        this.command = command;
        this.sentByUser = sentByUser;
    }

    public Command getCommand() {
        return command;
    }

    public User getSentByUser() {
        return sentByUser;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void setSentByUser(User sentByUser) {
        this.sentByUser = sentByUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Packet packet = (Packet) o;
        return command == packet.command && Objects.equals(sentByUser, packet.sentByUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(command, sentByUser);
    }
}
