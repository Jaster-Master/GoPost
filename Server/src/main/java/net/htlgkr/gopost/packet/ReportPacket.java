package net.htlgkr.gopost.packet;

import net.htlgkr.gopost.data.User;

public class ReportPacket extends Packet {
    private User reportUser;
    private String reason;

    public ReportPacket() {
    }

    public ReportPacket(String command, User sentByUser, User reportUser, String reason) {
        super(command, sentByUser);
        this.reportUser = reportUser;
        this.reason = reason;
    }

    public User getReportUser() {
        return reportUser;
    }

    public void setReportUser(User reportUser) {
        this.reportUser = reportUser;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
