package net.htlgkr.gopost.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.fragments.HomeFragment;
import net.htlgkr.gopost.fragments.HomeFragmentListener;
import net.htlgkr.gopost.fragments.MainFragment;
import net.htlgkr.gopost.fragments.MainFragmentListener;

public class BaseActivity extends AppCompatActivity implements HomeFragmentListener, MainFragmentListener {

    public static String log_tag;
    public static BaseActivity instance;
    private HomeFragment homeFragment;
    private MainFragment mainFragment;
    private final static String LOG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        log_tag = instance.getClass().getSimpleName();
        setTheme(savedInstanceState);
        setContentView(R.layout.activity_register); //TODO: Change from template to actual xml

        homeFragment = new HomeFragment();
        //getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentContainer, homeFragment).commit();
        mainFragment = new MainFragment();
        //getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, mainFragment).commit();
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


    @Override
    public void onInputHomeSent(String input) {
        Log.i(LOG, "Received input from HomeFragment: " + input);
        switch (input) {
            case "Search":
                //TODO Switch to SearchActivity
                break;
            case "Home":
                //TODO Switch to HomeActivity
                break;
            case "Profile":
                //TODO Switch to ProfileActivity
                break;
        }
    }

    @Override
    public void onInputMainSent(String input) {
        Log.i(LOG, "Received input from MainFragment: " + input);
        switch (input) {
            case "CreatePost":
                //TODO Create Post
                break;
        }
    }

    public void requestPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
