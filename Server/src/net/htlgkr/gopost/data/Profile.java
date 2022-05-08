package net.htlgkr.gopost.data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

public class Profile implements Serializable {
    private long userId;
    private byte[] profilePicture;
    private String userName;
    private String profileName;
    private String email;
    private String password;
    private String description;
    private Post[] posts;
    private Story[] stories;
    private boolean isPrivate;
    private Post[] savedPosts;
    private User[] friends;
    private User[] followers;
    private User[] followed;
    private LocalDateTime createdDate;

    public Profile() {
    }

    public Profile(long userId, String userName, String profileName, String email, String password, String description, boolean isPrivate, LocalDateTime createdDate, byte[] profilePicture, Post[] posts, Story[] stories, Post[] savedPosts, User[] friends, User[] followers, User[] followed) {
        this.userId = userId;
        this.profilePicture = profilePicture;
        this.userName = userName;
        this.profileName = profileName;
        this.email = email;
        this.password = password;
        this.description = description;
        this.posts = posts;
        this.stories = stories;
        this.isPrivate = isPrivate;
        this.savedPosts = savedPosts;
        this.friends = friends;
        this.followers = followers;
        this.followed = followed;
        this.createdDate = createdDate;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Post[] getPosts() {
        return posts;
    }

    public void setPosts(Post[] posts) {
        this.posts = posts;
    }

    public Story[] getStories() {
        return stories;
    }

    public void setStories(Story[] stories) {
        this.stories = stories;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public Post[] getSavedPosts() {
        return savedPosts;
    }

    public void setSavedPosts(Post[] savedPosts) {
        this.savedPosts = savedPosts;
    }

    public User[] getFriends() {
        return friends;
    }

    public void setFriends(User[] friends) {
        this.friends = friends;
    }

    public User[] getFollowers() {
        return followers;
    }

    public void setFollowers(User[] followers) {
        this.followers = followers;
    }

    public User[] getFollowed() {
        return followed;
    }

    public void setFollowed(User[] followed) {
        this.followed = followed;
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
        Profile profile = (Profile) o;
        return userId == profile.userId && isPrivate == profile.isPrivate && Arrays.equals(profilePicture, profile.profilePicture) && Objects.equals(profileName, profile.profileName) && Objects.equals(userName, profile.userName) && Objects.equals(email, profile.email) && Objects.equals(password, profile.password) && Objects.equals(description, profile.description) && Arrays.equals(posts, profile.posts) && Arrays.equals(stories, profile.stories) && Arrays.equals(savedPosts, profile.savedPosts) && Arrays.equals(friends, profile.friends) && Arrays.equals(followers, profile.followers) && Arrays.equals(followed, profile.followed) && Objects.equals(createdDate, profile.createdDate);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(userId, profileName, userName, email, password, description, isPrivate, createdDate);
        result = 31 * result + Arrays.hashCode(profilePicture);
        result = 31 * result + Arrays.hashCode(posts);
        result = 31 * result + Arrays.hashCode(stories);
        result = 31 * result + Arrays.hashCode(savedPosts);
        result = 31 * result + Arrays.hashCode(friends);
        result = 31 * result + Arrays.hashCode(followers);
        result = 31 * result + Arrays.hashCode(followed);
        return result;
    }
}
