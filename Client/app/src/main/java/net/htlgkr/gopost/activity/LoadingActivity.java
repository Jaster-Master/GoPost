package net.htlgkr.gopost.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.client.Client;
import net.htlgkr.gopost.data.User;
import net.htlgkr.gopost.notification.AutoStart;
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
        startNotificationService();

        new Thread(() -> {
            Log.i(log_tag, String.valueOf(Client.openConnection()));
            loadLoginData();
        }).start();
    }

    private void startNotificationService() {
        Intent broadcastIntent = new Intent(this, AutoStart.class);
        broadcastIntent.setAction(AutoStart.ACTION_ACTIVITY_START);
        sendBroadcast(broadcastIntent);
    }

    private void loadLoginData() {
        SharedPreferences sharedPreferences = getSharedPreferences("GoPostLoginData", MODE_PRIVATE);
        long userId = sharedPreferences.getLong("userId", -1);
        if (userId == -1) {
            loadLoginActivity();
            return;
        }
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
        LoginPacket loginPacket = new LoginPacket(Command.LOGIN, user, profileName, userName, email, password);
        ObservableValue<Packet> packet = new ObservableValue<>(loginPacket);
        packet.setOnValueSet((ObservableValue.SetListener<Packet>) value -> {
            Client.client = value.getSentByUser();
            Util.saveLoginData(this);
            loadMainActivity();
        });
        Client.getConnection().sendPacket(packet);
    }

    private void loadMainActivity() {
        executeLater(() -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        });
    }

    private void loadLoginActivity() {
        executeLater(() -> {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        });
    }

    private static void executeLater(Runnable runnable) {
        new Handler(Looper.getMainLooper()).postDelayed(runnable, 1000);
    }
}