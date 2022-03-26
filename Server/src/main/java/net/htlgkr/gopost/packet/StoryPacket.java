package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.client.Mark;
import net.htlgkr.gopost.client.User;

import java.time.LocalDateTime;
import java.util.Locale;

public class StoryPacket extends Packet {
    private byte[][] story;
    private String url;
    private User fromUser;
    private LocalDateTime releaseDate;
    private User[] likes;
    private Mark[] marks;
    private Locale location;

    public StoryPacket() {
    }

    public StoryPacket(String command, User sentByUser, byte[][] story, String url, User fromUser, LocalDateTime releaseDate, User[] likes, Mark[] marks, Locale location) {
        super(command, sentByUser);
        this.story = story;
        this.url = url;
        this.fromUser = fromUser;
        this.releaseDate = releaseDate;
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

    public Mark[] getMarks() {
        return marks;
    }

    public void setMarks(Mark[] marks) {
        this.marks = marks;
    }

    public Locale getLocation() {
        return location;
    }

    public void setLocation(Locale location) {
        this.location = location;
    }
}
