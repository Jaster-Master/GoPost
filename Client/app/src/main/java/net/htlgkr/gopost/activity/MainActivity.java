package net.htlgkr.gopost.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.fragments.HeaderBarFragment;
import net.htlgkr.gopost.fragments.HomeFragment;
import net.htlgkr.gopost.fragments.MenuBarFragment;
import net.htlgkr.gopost.fragments.ProfileFragment;
import net.htlgkr.gopost.fragments.SearchFragment;

public class MainActivity extends BaseActivity {

    private MenuBarFragment menuBarFragment;
    private HeaderBarFragment headerBarFragment;
    private HomeFragment homeFragment;
    private SearchFragment searchFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuBarFragment = new MenuBarFragment();
        headerBarFragment = new HeaderBarFragment();
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.homeFragmentContainer, menuBarFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.mainFragmentContainer, headerBarFragment).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragmentContainer, homeFragment).commit();
    }

    public void onSearchButtonAction(View view) {
        Log.i(log_tag, "Pressed Search Button");
        searchFragment = new SearchFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragmentContainer, searchFragment).commit();
    }

    public void onHomeButtonAction(View view) {
        Log.i(log_tag, "Pressed Home Button");
        homeFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragmentContainer, homeFragment).commit();
    }

    public void onProfileButtonAction(View view) {
        Log.i(log_tag, "Pressed Profile Button");
        profileFragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.contentFragmentContainer, profileFragment).commit();
    }
}