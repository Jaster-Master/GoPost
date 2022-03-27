package net.htlgkr.gopost.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class Comment implements Serializable {
    private User fromUser;
    private String message;
    private LocalDateTime createdDate;

    public Comment(User fromUser, String message, LocalDateTime createdDate) {
        this.fromUser = fromUser;
        this.message = message;
        this.createdDate = createdDate;
    }

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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
        Comment comment = (Comment) o;
        return Objects.equals(fromUser, comment.fromUser) && Objects.equals(message, comment.message) && Objects.equals(createdDate, comment.createdDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromUser, message, createdDate);
    }
}
