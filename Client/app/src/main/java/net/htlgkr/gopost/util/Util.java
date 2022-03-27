package net.htlgkr.gopost.util;

import androidx.appcompat.app.AppCompatDelegate;

public class Util {

    public static void enableSystemTheme() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public static void enableDarkTheme() {
        /*UiModeManager uiModeManager = (UiModeManager) LoginActivity.INSTANCE.getSystemService(Context.UI_MODE_SERVICE);
        uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_YES);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);*/
    }

    public static void disableDarkTheme() {
        /*UiModeManager uiModeManager = (UiModeManager) LoginActivity.INSTANCE.getSystemService(Context.UI_MODE_SERVICE);
        uiModeManager.setApplicationNightMode(UiModeManager.MODE_NIGHT_NO);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);*/
    }
}
