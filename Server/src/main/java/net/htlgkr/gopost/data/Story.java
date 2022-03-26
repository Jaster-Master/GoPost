package net.htlgkr.gopost.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class Story implements Serializable {
    private byte[][] story;
    private String url;
    private User fromUser;
    private LocalDateTime createdDate;
    private User[] likes;
    private User[] marks;
    private Locale location;

    public Story() {
    }

    public Story(byte[][] story, String url, User fromUser, LocalDateTime createdDate, User[] likes, User[] marks, Locale location) {
        this.story = story;
        this.url = url;
        this.fromUser = fromUser;
        this.createdDate = createdDate;
        this.likes = likes;
        this.marks = marks;
        this.location = location;
    }

    public byte[][] getStory() {
        return story;
    }

    public void setStory(byte[][] story) {
        this.story = story;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Story story1 = (Story) o;
        return Arrays.equals(story, story1.story) && Objects.equals(url, story1.url) && Objects.equals(fromUser, story1.fromUser) && Objects.equals(createdDate, story1.createdDate) && Arrays.equals(likes, story1.likes) && Arrays.equals(marks, story1.marks) && Objects.equals(location, story1.location);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(url, fromUser, createdDate, location);
        result = 31 * result + Arrays.hashCode(story);
        result = 31 * result + Arrays.hashCode(likes);
        result = 31 * result + Arrays.hashCode(marks);
        return result;
    }
}
