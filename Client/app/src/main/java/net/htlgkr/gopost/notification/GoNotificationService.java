package net.htlgkr.gopost.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.activity.BaseActivity;

import java.util.Map;

public class GoNotificationService extends FirebaseMessagingService {

    public static final String LOG_TAG = GoNotificationService.class.getSimpleName();
    private String CHANNEL_ID;
    private Context context;
    private long userId;

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.i(LOG_TAG, "New Token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        SharedPreferences sharedPreferences = getSharedPreferences("GoPostLoginData", MODE_PRIVATE);
        userId = sharedPreferences.getLong("userId", -1);

        Log.i(LOG_TAG, "Message Received: " + message.getNotification());
        if (!isForThisClient(message.getData())) return;

        showNotification(message);
    }

    private boolean isForThisClient(Map<String, String> data) {
        Object userIdObject = data.get("userId");
        if (userIdObject == null) return false;
        long userId = Long.parseLong(userIdObject.toString());
        return userId != this.userId;
    }

    private void showNotification(RemoteMessage message) {
        CHANNEL_ID = getString(R.string.notification_channel_id);
        context = getApplicationContext();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        String notificationType = message.getData().get("notificationType");
        RemoteMessage.Notification remoteNotification = message.getNotification();
        if (notificationType == null || remoteNotification == null) return;

        Log.i(LOG_TAG, "Notification: " + notificationType);
        String title = remoteNotification.getTitle();
        String body = remoteNotification.getBody();
        String icon = remoteNotification.getIcon();
        switch (notificationType) {
            case "Follower":
                notificationManager.notify(1, createFollowNotification(title, body, icon));
                break;
            case "Like":
                notificationManager.notify(1, createLikeNotification(title, body, icon));
                break;
            case "Comment":
                notificationManager.notify(1, createCommentNotification(title, body, icon));
                break;
        }
    }

    private Notification createFollowNotification(String title, String body, String icon) {
        Intent activityFeedIntent = new Intent(getApplicationContext(), BaseActivity.class);
        activityFeedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent activityFeedPendingIntent =
                PendingIntent.getActivity(context, 0, activityFeedIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder followNotificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(getDrawableId(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(activityFeedPendingIntent)
                .setAutoCancel(true);
        return followNotificationBuilder.build();
    }

    private Notification createLikeNotification(String title, String body, String icon) {
        Intent activityFeedIntent = new Intent(context, BaseActivity.class);
        activityFeedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent activityFeedPendingIntent =
                PendingIntent.getActivity(context, 0, activityFeedIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder likeNotificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(getDrawableId(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(activityFeedPendingIntent)
                .setAutoCancel(true);
        return likeNotificationBuilder.build();
    }

    private Notification createCommentNotification(String title, String body, String icon) {
        Intent activityFeedIntent = new Intent(context, BaseActivity.class);
        activityFeedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent activityFeedPendingIntent =
                PendingIntent.getActivity(context, 0, activityFeedIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder commentNotificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(getDrawableId(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(activityFeedPendingIntent)
                .setAutoCancel(true);
        return commentNotificationBuilder.build();
    }

    private int getDrawableId(String icon) {
        return this.getResources().getIdentifier(icon, "drawable", this.getPackageName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent broadcastIntent = new Intent(this, AutoStart.class);
        broadcastIntent.setAction(AutoStart.ACTION_ACTIVITY_CLOSE);
        sendBroadcast(broadcastIntent);
    }
}
