package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.client.User;

public class BlockPacket extends Packet {
    private User blockUser;

    public BlockPacket(String command, User sentByUser, User blockUser) {
        super(command, sentByUser);
        this.blockUser = blockUser;
    }

    public User getBlockUser() {
        return blockUser;
    }

    public BlockPacket() {
    }

    public void setBlockUser(User blockUser) {
        this.blockUser = blockUser;
    }
}
