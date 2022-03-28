package net.htlgkr.gopost.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

public class Post implements Serializable {

    private byte[][] pictures;
    private String url;
    private String description;
    private User fromUser;
    private LocalDateTime createdDate;
    private User[] likes;
    private Comment[] comments;
    private User[] marks;
    private GoLocation location;

    public Post() {
    }

    public Post(byte[][] pictures, String url, String description, User fromUser, LocalDateTime releaseDate, User[] likes, Comment[] comments, User[] marks, GoLocation location) {
        this.pictures = pictures;
        this.url = url;
        this.description = description;
        this.fromUser = fromUser;
        this.createdDate = releaseDate;
        this.likes = likes;
        this.comments = comments;
        this.marks = marks;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
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

    public GoLocation getLocation() {
        return location;
    }

    public void setLocation(GoLocation location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Arrays.equals(pictures, post.pictures) && Objects.equals(url, post.url) && Objects.equals(description, post.description) && Objects.equals(fromUser, post.fromUser) && Objects.equals(createdDate, post.createdDate) && Arrays.equals(likes, post.likes) && Arrays.equals(comments, post.comments) && Arrays.equals(marks, post.marks) && Objects.equals(location, post.location);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(url, description, fromUser, createdDate, location);
        result = 31 * result + Arrays.hashCode(pictures);
        result = 31 * result + Arrays.hashCode(likes);
        result = 31 * result + Arrays.hashCode(comments);
        result = 31 * result + Arrays.hashCode(marks);
        return result;
    }
}
