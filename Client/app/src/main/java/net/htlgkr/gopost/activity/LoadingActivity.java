package net.htlgkr.gopost.activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.client.Client;
import net.htlgkr.gopost.data.User;
import net.htlgkr.gopost.packet.LoginPacket;
import net.htlgkr.gopost.packet.Packet;
import net.htlgkr.gopost.util.Command;
import net.htlgkr.gopost.util.ObservableValue;
import net.htlgkr.gopost.util.Util;

public class LoadingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        createNotificationChannel();
        new Thread(() -> {
            Log.e(log_tag, String.valueOf(Client.openConnection()));
            loadLoginData();
        }).start();

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            Log.i(log_tag, "New Token: " + task.getResult());
            FirebaseMessaging.getInstance().subscribeToTopic("GoPost");
        });
    }

    private void loadLoginData() {
        SharedPreferences sharedPreferences = getSharedPreferences("GoPostLoginData", MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userId", -1);
        if (userId == -1) return;
        String userName = sharedPreferences.getString("userName", null);
        String profileName = sharedPreferences.getString("profileName", null);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);
        String profilePictureString = sharedPreferences.getString("profilePicture", null);
        if (profilePictureString == null) profilePictureString = "";
        byte[] profilePicture = Base64.decode(profilePictureString, Base64.DEFAULT);
        autoLogin(userId, userName, profileName, email, password, profilePicture);
    }

    private void autoLogin(long userId, String userName, String profileName, String email, String password, byte[] profilePicture) {
        User user = new User(userId, userName, profileName, email, password, profilePicture);
        LoginPacket loginPacket = new LoginPacket(Command.CHECK_PASSWORD, user, profileName, userName, email, password);
        ObservableValue<Packet> packet = new ObservableValue<>(loginPacket);
        packet.setOnValueSet((ObservableValue.SetListener<Packet>) value -> {
            Client.client = value.getSentByUser();
            Util.saveLoginData(this);
        });
        Client.getConnection().sendPacket(packet);
    }

    private void createNotificationChannel() {
        String channelId = getString(R.string.notification_channel_id);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(channelId, channelId, importance);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}