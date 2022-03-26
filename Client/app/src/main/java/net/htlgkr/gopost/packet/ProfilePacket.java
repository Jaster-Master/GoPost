package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.data.Post;
import net.htlgkr.gopost.data.Profile;
import net.htlgkr.gopost.data.Story;
import net.htlgkr.gopost.data.User;

import java.util.Objects;

public class ProfilePacket extends Packet {
    private Profile profile;

    public ProfilePacket() {
    }

    public ProfilePacket(String command, User sentByUser, Profile profile) {
        super(command, sentByUser);
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProfilePacket that = (ProfilePacket) o;
        return Objects.equals(profile, that.profile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profile);
    }
}
