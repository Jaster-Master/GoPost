package net.htlgkr.gopost.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.activity.BaseActivity;

public class GoNotificationService extends FirebaseMessagingService {

    public static final String LOG_TAG = GoNotificationService.class.getSimpleName();
    private final String CHANNEL_ID = getString(R.string.notification_channel_id);
    private final Context context = getApplicationContext();
    private final NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.i(LOG_TAG, "New Token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        showNotification(message.getMessageType());
    }

    private void showNotification(String notification) {
        switch (notification) {
            case "Follower":
                notificationManager.notify(1, createFollowNotification());
                break;
            case "Like":
                notificationManager.notify(1, createLikeNotification());
                break;
            case "Comment":
                notificationManager.notify(1, createCommentNotification());
                break;
        }
    }

    private Notification createFollowNotification() {
        Intent activityFeedIntent = new Intent(getApplicationContext(), BaseActivity.class);
        activityFeedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent activityFeedPendingIntent =
                PendingIntent.getActivity(context, 0, activityFeedIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder followNotificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle("GoPost - Follow")
                .setContentText("Somebody followed you!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(activityFeedPendingIntent)
                .setAutoCancel(true);
        return followNotificationBuilder.build();
    }

    private Notification createLikeNotification() {
        Intent activityFeedIntent = new Intent(context, BaseActivity.class);
        activityFeedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent activityFeedPendingIntent =
                PendingIntent.getActivity(context, 0, activityFeedIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder likeNotificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle("GoPost - Like")
                .setContentText("Somebody liked your content!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(activityFeedPendingIntent)
                .setAutoCancel(true);
        return likeNotificationBuilder.build();
    }

    private Notification createCommentNotification() {
        Intent activityFeedIntent = new Intent(context, BaseActivity.class);
        activityFeedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent activityFeedPendingIntent =
                PendingIntent.getActivity(context, 0, activityFeedIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder commentNotificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle("GoPost - Comment")
                .setContentText("Somebody commented your content!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(activityFeedPendingIntent)
                .setAutoCancel(true);
        return commentNotificationBuilder.build();
    }
}
