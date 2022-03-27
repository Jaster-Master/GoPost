package net.htlgkr.gopost.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class Post implements Serializable {

    private byte[][] pictures;
    private String url;
    private String description;
    private User fromUser;
    private LocalDateTime releaseDate;
    private User[] likes;
    private Comment[] comments;
    private User[] marks;
    private Locale location;
    private LocalDateTime createdDate;

    public Post() {
    }

    public Post(byte[][] pictures, String url, String description, User fromUser, LocalDateTime releaseDate, User[] likes, Comment[] comments, User[] marks, Locale location, LocalDateTime createdDate) {
        this.pictures = pictures;
        this.url = url;
        this.description = description;
        this.fromUser = fromUser;
        this.releaseDate = releaseDate;
        this.likes = likes;
        this.comments = comments;
        this.marks = marks;
        this.location = location;
        this.createdDate = createdDate;
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

    public User[] getMarks() {
        return marks;
    }

    public void setMarks(User[] marks) {
        this.marks = marks;
    }

    public Locale getLocation() {
        return location;
    }

    public void setLocation(Locale location) {
        this.location = location;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Arrays.equals(pictures, post.pictures) && Objects.equals(url, post.url) && Objects.equals(description, post.description) && Objects.equals(fromUser, post.fromUser) && Objects.equals(releaseDate, post.releaseDate) && Arrays.equals(likes, post.likes) && Arrays.equals(comments, post.comments) && Arrays.equals(marks, post.marks) && Objects.equals(location, post.location) && Objects.equals(createdDate, post.createdDate);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(url, description, fromUser, releaseDate, location, createdDate);
        result = 31 * result + Arrays.hashCode(pictures);
        result = 31 * result + Arrays.hashCode(likes);
        result = 31 * result + Arrays.hashCode(comments);
        result = 31 * result + Arrays.hashCode(marks);
        return result;
    }
}
