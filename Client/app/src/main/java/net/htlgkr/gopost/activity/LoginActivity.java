package net.htlgkr.gopost.activity;

import android.os.Bundle;

import net.htlgkr.gopost.R;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*new Thread(() -> {
            Log.e(log_tag, String.valueOf(Client.openConnection()));
            ObservableValue<Packet> packet = new ObservableValue<>(new LoginPacket(Command.FIRST_TIME_LOGIN, null, "Jaster", "jaster", "amogus@sus.com", Encrypt.SHA512("amongamong")));
            packet.setOnValueSet((ObservableValue.SetListener<Packet>) value -> Log.e(log_tag, value.getCommand().toString()));
            Client.getConnection().sendPacket(packet);
        }).start();*/
    }
}