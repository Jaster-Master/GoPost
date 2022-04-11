package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.data.Story;
import net.htlgkr.gopost.data.User;
import net.htlgkr.gopost.util.Command;

import java.util.Objects;

public class StoryPacket extends Packet {

    private Story story;

    public StoryPacket() {
    }

    public StoryPacket(Command command, User sentByUser, Story story) {
        super(command, sentByUser);
        this.story = story;
    }

    public Story getStory() {
        return story;
    }

    public void setStory(Story story) {
        this.story = story;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StoryPacket that = (StoryPacket) o;
        return Objects.equals(story, that.story);
    }

    @Override
    public int hashCode() {
        return Objects.hash(story);
    }
}
