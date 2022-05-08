package net.htlgkr.gopost.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public abstract class BaseActivity extends AppCompatActivity {

    public static String log_tag;
    public static BaseActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        log_tag = instance.getClass().getSimpleName();
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
