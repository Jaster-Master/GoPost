package net.htlgkr.gopost.data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public class User implements Serializable {

    private long userId;
    private String userName;
    private String profileName;
    private String email;
    private String password;
    private byte[] profilePicture;

    public User() {
    }

    public User(long userId, String userName, String profileName, String email, String password, byte[] profilePicture) {
        this.userId = userId;
        this.userName = userName;
        this.profileName = profileName;
        this.email = email;
        this.password = password;
        this.profilePicture = profilePicture;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
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

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId == user.userId && Objects.equals(profileName, user.profileName) && Objects.equals(userName, user.userName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Arrays.equals(profilePicture, user.profilePicture);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(userId, profileName, userName, email, password);
        result = 31 * result + Arrays.hashCode(profilePicture);
        return result;
    }
}
