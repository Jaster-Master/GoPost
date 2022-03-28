package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.data.User;

import java.util.Objects;

public class UserPacket extends Packet {
    private String userName;

    public UserPacket() {
    }

    public UserPacket(String command, User sentByUser, String userName) {
        super(command, sentByUser);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPacket that = (UserPacket) o;
        return Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName);
    }
}
