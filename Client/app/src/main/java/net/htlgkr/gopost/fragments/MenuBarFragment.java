package net.htlgkr.gopost.fragments;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import net.htlgkr.gopost.R;


public class MenuBarFragment extends Fragment {

    private ImageButton imageButtonSearch;
    private ImageButton imageButtonHome;
    private ImageButton imageButtonProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_bar, container, false);
        imageButtonSearch = view.findViewById(R.id.searchButton);
        imageButtonHome = view.findViewById(R.id.homeButton);
        imageButtonProfile = view.findViewById(R.id.profileButton);
        hideButtonBackgrounds();
        return view;
    }

    public void changeSelectedButton(View view)
    {
        hideButtonBackgrounds();
        view.setBackgroundResource(R.color.white);
    }

    private void hideButtonBackgrounds()
    {
        imageButtonHome.setBackgroundResource(0);
        imageButtonSearch.setBackgroundResource(0);
        imageButtonProfile.setBackgroundResource(0);
    }
}