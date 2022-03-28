package net.htlgkr.gopost.activity;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public static BaseActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setTheme(savedInstanceState);
    }

    private void setTheme(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.getBoolean("IsDarkMode")) {
            setTheme(android.R.style.Theme_Black_NoTitleBar);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("IsDarkMode", true);
    }
}
