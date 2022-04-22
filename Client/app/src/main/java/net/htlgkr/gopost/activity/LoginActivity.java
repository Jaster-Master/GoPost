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
import net.htlgkr.gopost.util.Util;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        new Thread(() -> {
            ObservableValue<Packet> packet = new ObservableValue<>(new LoginPacket(Command.FIRST_TIME_LOGIN, null, "Jaster", "jaster", "amogus@sus.com", Encrypt.SHA512("amongamong")));
            packet.setOnValueSet((ObservableValue.SetListener<Packet>) value -> {
                Log.i(log_tag, value.getCommand().toString());
                Client.client = value.getSentByUser();
                Util.saveLoginData(this);
            });
            Client.getConnection().sendPacket(packet);
        }).start();
    }
}