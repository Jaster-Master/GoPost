package net.htlgkr.gopost.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class BaseActivity extends AppCompatActivity {
    public static BaseActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        enableSystemTheme();
    }

    public void enableSystemTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public void enableDarkTheme() {
        /*UiModeManager uiModeManager = (UiModeManager) LoginActivity.INSTANCE.getSystemService(Context.UI_MODE_SERVICE);
        uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);*/
    }

    public void disableDarkTheme() {
        /*UiModeManager uiModeManager = (UiModeManager) LoginActivity.INSTANCE.getSystemService(Context.UI_MODE_SERVICE);
        uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);*/
    }
}
