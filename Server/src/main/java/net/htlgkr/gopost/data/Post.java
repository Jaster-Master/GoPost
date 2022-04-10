package net.htlgkr.gopost.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Post implements Serializable {

    private List<byte[]> pictures;
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

    public Post(List<byte[]> pictures, User fromUser, String url, LocalDateTime createdDate, GoLocation location, String description, User[] likes, Comment[] comments, User[] marks) {
        this.pictures = pictures;
        this.url = url;
        this.description = description;
        this.fromUser = fromUser;
        this.createdDate = createdDate;
        this.likes = likes;
        this.comments = comments;
        this.marks = marks;
        this.location = location;
    }

    public void addToPost(byte[] postMedia) {
        pictures.add(postMedia);
    }

    public List<byte[]> getPictures() {
        return pictures;
    }

    public void setPictures(List<byte[]> pictures) {
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
        return Objects.equals(pictures, post.pictures) && Objects.equals(url, post.url) && Objects.equals(description, post.description) && Objects.equals(fromUser, post.fromUser) && Objects.equals(createdDate, post.createdDate) && Arrays.equals(likes, post.likes) && Arrays.equals(comments, post.comments) && Arrays.equals(marks, post.marks) && Objects.equals(location, post.location);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(pictures, url, description, fromUser, createdDate, location);
        result = 31 * result + Arrays.hashCode(likes);
        result = 31 * result + Arrays.hashCode(comments);
        result = 31 * result + Arrays.hashCode(marks);
        return result;
    }
}
