package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.data.Post;
import net.htlgkr.gopost.data.User;

import java.util.Objects;

public class PostPacket extends Packet {

    private Post post;

    public PostPacket() {
    }

    public PostPacket(String command, User sentByUser, Post post) {
        super(command, sentByUser);
        this.post = post;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostPacket that = (PostPacket) o;
        return Objects.equals(post, that.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(post);
    }
}
