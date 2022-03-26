package com.server.packages;

import com.server.user.Post;
import com.server.user.User;
import com.server.user.Story;

public class ProfilePacket extends Packet{
    private long userId;
    private byte[] profilePicture;
    private String profileName;
    private String userName;
    private String description;
    private Post[] posts;
    private Story[] stories;
    private boolean isPrivate;
    private Post[] savedPosts;
    private User[] friends;
    private User[] followers;
    private User[] followed;

    public ProfilePacket() {
    }

    public ProfilePacket(String command, User sentByUser, long userId, byte[] profilePicture, String profileName, String userName, String description, Post[] posts, Story[] stories, boolean isPrivate, Post[] savedPosts, User[] friends, User[] followers, User[] followed) {
        super(command, sentByUser);
        this.userId = userId;
        this.profilePicture = profilePicture;
        this.profileName = profileName;
        this.userName = userName;
        this.description = description;
        this.posts = posts;
        this.stories = stories;
        this.isPrivate = isPrivate;
        this.savedPosts = savedPosts;
        this.friends = friends;
        this.followers = followers;
        this.followed = followed;
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

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
}
