package net.htlgkr.gopost.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public class Story implements Serializable {
    private List<byte[]> story;
    private String url;
    private User fromUser;
    private LocalDateTime createdDate;
    private GoLocation location;

    public Story() {
    }

    public Story(List<byte[]> story, String url, User fromUser, LocalDateTime createdDate, GoLocation location) {
        this.story = story;
        this.url = url;
        this.fromUser = fromUser;
        this.createdDate = createdDate;
        this.location = location;
    }

    public void addToStory(byte[] storyMedia) {
        story.add(storyMedia);
    }

    public List<byte[]> getStory() {
        return story;
    }

    public void setStory(List<byte[]> story) {
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
        Story story1 = (Story) o;
        return Objects.equals(story, story1.story) && Objects.equals(url, story1.url) && Objects.equals(fromUser, story1.fromUser) && Objects.equals(createdDate, story1.createdDate) && Objects.equals(location, story1.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(story, url, fromUser, createdDate, location);
    }
}
