package net.htlgkr.gopost.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.client.Client;
import net.htlgkr.gopost.packet.LoginPacket;
import net.htlgkr.gopost.packet.Packet;
import net.htlgkr.gopost.util.Command;
import net.htlgkr.gopost.util.Encrypt;
import net.htlgkr.gopost.util.ObservableValue;
import net.htlgkr.gopost.util.Util;

public class LoginActivity extends BaseActivity {

    private EditText editTextLoginUserName, editTextLoginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextLoginUserName = findViewById(R.id.editTextLoginUserName);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);

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

    public void onLoginButtonAction(View view) {
        String userName = editTextLoginUserName.getText().toString();
        String password = editTextLoginPassword.getText().toString();

        sendLoginRequest(userName, password);
    }

    private void sendLoginRequest(String userName, String password) {
        new Thread(() -> {
            ObservableValue<Packet> packet = new ObservableValue<>(new LoginPacket(Command.LOGIN, null, null, userName, null, Encrypt.SHA512(password)));
            packet.setOnValueSet((ObservableValue.SetListener<Packet>) value -> {
                Log.i(log_tag, value.getCommand().toString());
                Client.client = value.getSentByUser();
                Util.saveLoginData(this);
                loadMainActivity();
            });
            Client.getConnection().sendPacket(packet);
        }).start();
    }

    private void loadMainActivity() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    public void onRegisterButtonAction(View view) {
        Intent registerIntent = new Intent(this, RegisterActivity.class);
        startActivity(registerIntent);
        finish();
    }
}