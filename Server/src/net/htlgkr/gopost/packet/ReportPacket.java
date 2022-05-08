package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.data.User;
import net.htlgkr.gopost.util.Command;

import java.util.Objects;

public class ReportPacket extends Packet {
    private String userName;
    private String reason;

    public ReportPacket() {
    }

    public ReportPacket(Command command, User sentByUser, String userName, String reason) {
        super(command, sentByUser);
        this.userName = userName;
        this.reason = reason;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportPacket that = (ReportPacket) o;
        return Objects.equals(userName, that.userName) && Objects.equals(reason, that.reason);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userName, reason);
    }
}
