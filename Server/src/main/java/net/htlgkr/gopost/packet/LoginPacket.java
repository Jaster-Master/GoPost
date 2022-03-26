package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.client.User;

public class LoginPacket extends Packet {
    private String userName;
    private String email;
    private String password;

    public LoginPacket() {
    }

    public LoginPacket(String command, User sentByUser, String userName, String email, String password) {
        super(command, sentByUser);
        this.userName = userName;
        this.email = email;
        this.password = password;
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
}
