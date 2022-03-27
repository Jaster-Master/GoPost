package net.htlgkr.gopost.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.util.Util;

public class LoginActivity extends AppCompatActivity {

    public static LoginActivity INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        INSTANCE = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Util.enableSystemTheme();
    }
}