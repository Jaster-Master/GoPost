package net.htlgkr.gopost.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.client.Client;
import net.htlgkr.gopost.data.User;
import net.htlgkr.gopost.packet.LoginPacket;
import net.htlgkr.gopost.packet.Packet;
import net.htlgkr.gopost.util.Command;
import net.htlgkr.gopost.util.Encrypt;
import net.htlgkr.gopost.util.ObservableValue;
import net.htlgkr.gopost.util.Util;

import java.util.regex.Pattern;

public class RegisterActivity extends BaseActivity {

    private EditText editTextRegisterEmail, editTextRegisterProfileName, editTextRegisterUserName, editTextRegisterPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextRegisterEmail = findViewById(R.id.editTextRegisterEmail);
        editTextRegisterProfileName = findViewById(R.id.editTextRegisterProfileName);
        editTextRegisterUserName = findViewById(R.id.editTextRegisterUserName);
        editTextRegisterPassword = findViewById(R.id.editTextRegisterPassword);
    }

    public void onRegisterButtonAction(View view) {
        User registerUser = new User();
        String email = editTextRegisterEmail.getText().toString();
        if (!isValidEmail(email)) return;
        String profileName = editTextRegisterProfileName.getText().toString();
        String userName = editTextRegisterUserName.getText().toString();
        if (!isValidUserName(userName)) return;
        String password = editTextRegisterPassword.getText().toString();
        if (!isValidPassword(password)) return;

        registerUser.setEmail(email);
        registerUser.setProfileName(profileName);
        registerUser.setUserName(userName);
        registerUser.setPassword(Encrypt.SHA512(password));
        sendRegisterRequest(registerUser);
    }

    // https://emailregex.com
    private boolean isValidEmail(String email) {
        return Pattern.matches(Util.EMAIL_REGEX, email);
    }

    private boolean isValidUserName(String userName) {
        if (userName.isEmpty()) return false;
        return !userName.trim().isEmpty();
    }

    private boolean isValidPassword(String password) {
        if (password.isEmpty() || !password.trim().isEmpty()) return false;
        return password.length() >= 8;
    }

    private void sendRegisterRequest(User registerUser) {
        new Thread(() -> {
            ObservableValue<Packet> packet = new ObservableValue<>(new LoginPacket(Command.FIRST_TIME_LOGIN, registerUser, registerUser.getProfileName(), registerUser.getUserName(), registerUser.getEmail(), registerUser.getPassword()));
            packet.setOnValueSet((ObservableValue.SetListener<Packet>) value -> {
                Log.i(log_tag, value.getCommand().toString());
                Client.client = value.getSentByUser();
                Util.saveLoginData(this);
                loadLoadingActivity();
            });
            Client.getConnection().sendPacket(packet);
        }).start();
    }

    public void onLoginButtonAction(View view) {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

    private void loadLoadingActivity() {
        Intent loadingIntent = new Intent(this, LoadingActivity.class);
        startActivity(loadingIntent);
    }
}
