package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.data.Comment;
import net.htlgkr.gopost.data.User;
import net.htlgkr.gopost.util.Command;

import java.util.Objects;

public class CommentPacket extends Packet {

    private String likedPostUrl;
    private Comment comment;

    public CommentPacket(String likedPostUrl, Comment comment) {
        this.likedPostUrl = likedPostUrl;
        this.comment = comment;
    }

    public CommentPacket(Command command, User sentByUser, String likedPostUrl, Comment comment) {
        super(command, sentByUser);
        this.likedPostUrl = likedPostUrl;
        this.comment = comment;
    }

    public String getLikedPostUrl() {
        return likedPostUrl;
    }

    public void setLikedPostUrl(String likedPostUrl) {
        this.likedPostUrl = likedPostUrl;
    }

    public Comment getComment() {
        return comment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CommentPacket that = (CommentPacket) o;
        return Objects.equals(likedPostUrl, that.likedPostUrl) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), likedPostUrl, comment);
    }
}
