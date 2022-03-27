package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.data.Comment;
import net.htlgkr.gopost.data.User;

import java.time.LocalDateTime;
import java.util.Locale;

public class PostPacket extends Packet {
    private byte[][] pictures;
    private String url;
    private String description;
    private User fromUser;
    private LocalDateTime releaseDate;
    private User[] likes;
    private Comment[] comments;
    private Locale location;

    public PostPacket() {
    }

    public PostPacket(String command, User sentByUser, byte[][] pictures, String url, String description, User fromUser, LocalDateTime releaseDate, User[] likes, Comment[] comments, Locale location) {
        super(command, sentByUser);
        this.pictures = pictures;
        this.url = url;
        this.description = description;
        this.fromUser = fromUser;
        this.releaseDate = releaseDate;
        this.likes = likes;
        this.comments = comments;
        this.location = location;
    }

    public byte[][] getPictures() {
        return pictures;
    }

    public void setPictures(byte[][] pictures) {
        this.pictures = pictures;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    public User[] getLikes() {
        return likes;
    }

    public void setLikes(User[] likes) {
        this.likes = likes;
    }

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }

    public Locale getLocation() {
        return location;
    }

    public void setLocation(Locale location) {
        this.location = location;
    }
}
