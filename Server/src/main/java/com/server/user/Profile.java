package com.server.user;

public class Profile {
    private String userName;
    private String email;
    private String password;

    public Profile() {
    }

    public Profile(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password=password;
    }
}
