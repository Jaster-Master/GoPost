package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.client.User;

public class LoginPacket extends Packet {
    private String userName;
    private String profileName;
    private String email;
    private String password;
    private boolean isPrivate;

    public LoginPacket() {
    }

    public LoginPacket(String command, User sentByUser, String userName, String profileName, String email, String password, boolean isPrivate) {
        super(command, sentByUser);
        this.userName = userName;
        this.profileName = profileName;
        this.email = email;
        this.password = password;
        this.isPrivate = isPrivate;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
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
