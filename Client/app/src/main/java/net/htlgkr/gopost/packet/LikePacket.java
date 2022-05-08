package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.data.User;
import net.htlgkr.gopost.util.Command;

import java.util.Objects;

public class LikePacket extends Packet {

    private String likedPostUrl;

    public LikePacket(String likedPostUrl) {
        this.likedPostUrl = likedPostUrl;
    }

    public LikePacket(Command command, User sentByUser, String likedPostUrl) {
        super(command, sentByUser);
        this.likedPostUrl = likedPostUrl;
    }

    public String getLikedPostUrl() {
        return likedPostUrl;
    }

    public void setLikedPostUrl(String likedPostUrl) {
        this.likedPostUrl = likedPostUrl;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        LikePacket that = (LikePacket) o;
        return Objects.equals(likedPostUrl, that.likedPostUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), likedPostUrl);
    }
}
