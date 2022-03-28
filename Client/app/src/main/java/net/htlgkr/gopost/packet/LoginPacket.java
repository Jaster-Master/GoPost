package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.data.User;
import net.htlgkr.gopost.util.Encrypt;

import java.util.Objects;

public class LoginPacket extends Packet {

    private String profileName;
    private String userName;
    private String email;
    private String password;

    public LoginPacket() {
    }

    public LoginPacket(String command, User sentByUser, String profileName, String userName, String email, String password) {
        super(command, sentByUser);
        this.profileName = profileName;
        this.userName = userName;
        this.email = email;
        this.password = Encrypt.SHA512(password);
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

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginPacket that = (LoginPacket) o;
        return Objects.equals(profileName, that.profileName) && Objects.equals(userName, that.userName) && Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profileName, userName, email, password);
    }
}