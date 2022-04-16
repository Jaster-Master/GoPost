package net.htlgkr.gopost.activity;

import android.os.Bundle;
import android.util.Log;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.client.Client;
import net.htlgkr.gopost.packet.LoginPacket;
import net.htlgkr.gopost.packet.Packet;
import net.htlgkr.gopost.util.Command;
import net.htlgkr.gopost.util.Encrypt;
import net.htlgkr.gopost.util.ObservableValue;

public class LoginActivity extends BaseActivity {

    public static String tag = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new Thread(() -> {
            Log.e(tag, String.valueOf(Client.openConnection()));
            ObservableValue<Packet> packet = new ObservableValue<>(new LoginPacket(Command.FIRST_TIME_LOGIN, null, "Jaster", "jaster", "amogus@sus.com", Encrypt.SHA512("amongamong")));
            packet.setOnValueSet((ObservableValue.SetListener<Packet>) value -> Log.e(tag, value.getCommand().toString()));
            Client.getConnection().sendPacket(packet);
        }).start();
    }
}