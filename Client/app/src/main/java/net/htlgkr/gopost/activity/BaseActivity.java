package net.htlgkr.gopost.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.fragments.HomeFragment;
import net.htlgkr.gopost.fragments.HomeFragmentListener;

public class BaseActivity extends AppCompatActivity implements HomeFragmentListener {

    public static String tag;
    public static BaseActivity instance;
    private HomeFragment homeFragment;
    private final static String LOG = BaseActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        tag = instance.getClass().getSimpleName();
        setTheme(savedInstanceState);
        setContentView(R.layout.template_main); //TODO: Change from template to actual xml

        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentContainer,homeFragment).commit();
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
    public void onInputSent(String input) {
        Log.i(LOG,"Received input from HomeFragment: "+input);
        switch(input)
        {
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
}
