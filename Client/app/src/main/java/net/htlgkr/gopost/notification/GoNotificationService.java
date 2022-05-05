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

        Log.i(LOG_TAG, "Message Received");
        if (!isForThisClient(message.getData())) return;
        showNotification(message.getData());
    }

    private boolean isForThisClient(Map<String, String> data) {
        Object userIdObject = data.get("userId");
        if (userIdObject == null) return false;
        long userId = Long.parseLong(userIdObject.toString());
        return userId != this.userId;
    }

    private void showNotification(Map<String, String> data) {
        CHANNEL_ID = getString(R.string.notification_channel_id);
        context = getApplicationContext();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        String title = data.get("title");
        String body = data.get("body");
        String icon = data.get("icon");
        notificationManager.notify(0, createNotification(title, body, icon));
        Log.i(LOG_TAG, "Notification: " + body);
    }

    private Notification createNotification(String title, String body, String icon) {
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
