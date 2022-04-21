package net.htlgkr.gopost.notification;

import android.app.Notification;

import java.util.Objects;

public class GoNotification {

    private final Notification notification;
    private final int notificationId;

    public GoNotification(Notification notification, int notificationId) {
        this.notification = notification;
        this.notificationId = notificationId;
    }

    public Notification getNotification() {
        return notification;
    }

    public int getNotificationId() {
        return notificationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoNotification that = (GoNotification) o;
        return notificationId == that.notificationId && Objects.equals(notification, that.notification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(notification, notificationId);
    }
}
