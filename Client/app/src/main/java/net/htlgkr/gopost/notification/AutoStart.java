package net.htlgkr.gopost.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import net.htlgkr.gopost.R;

public class AutoStart extends BroadcastReceiver {

    public static final String LOG_TAG = AutoStart.class.getSimpleName();
    public static final String ACTION_ACTIVITY_CLOSE = "gopost.action.ACTIVITY_CLOSE";
    public static final String ACTION_ACTIVITY_START = "gopost.action.ACTIVITY_START";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) return;
        switch (intent.getAction()) {
            case Intent.ACTION_BOOT_COMPLETED:
            case ACTION_ACTIVITY_START:
            case ACTION_ACTIVITY_CLOSE:
                Log.i(LOG_TAG, "Broadcast-Action: " + intent.getAction());
                createNotificationChannel(context);
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
                    Log.i(LOG_TAG, "New Token: " + task.getResult());
                    FirebaseMessaging.getInstance().subscribeToTopic("GoPost");
                });
                Intent serviceIntent = new Intent(context, GoNotificationService.class);
                context.startService(serviceIntent);
        }
    }

    private void createNotificationChannel(Context context) {
        String channelId = context.getString(R.string.notification_channel_id);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelId, channelId, importance);
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}
