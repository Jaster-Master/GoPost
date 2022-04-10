package net.htlgkr.gopost.activity;

import android.os.Bundle;
import android.util.Log;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.client.Client;
import net.htlgkr.gopost.client.ServerConnection;
import net.htlgkr.gopost.data.User;
import net.htlgkr.gopost.packet.LoginPacket;
import net.htlgkr.gopost.packet.Packet;
import net.htlgkr.gopost.util.ObservableValue;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Client.openConnection();
            Log.i("LoginActivity","Ok");
            ServerConnection sc = new ServerConnection();
            Log.i("LoginActivity","Ok");
            ObservableValue<Packet> login = new ObservableValue<Packet>(new LoginPacket("firstTimeLogin", new User(0, "hi", "moin",
                    "df@df", "sdf", new byte[]{1, 0}), "moin", "hi", "df@df", "sdf"));
            Log.i("LoginActivity","Ok");
            sc.sendPacket(login);
            Log.i("LoginActivity","Ok");
            login.setOnValueSet((value) -> {
                System.out.println(value.getSentByUser().toString());
            });
            Log.i("LoginActivity","Ok");
    }
}