package net.htlgkr.gopost.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import net.htlgkr.gopost.R;


public class MenuBarFragment extends Fragment {

    private ImageView imageViewSearch;
    private ImageView imageViewHome;
    private ImageView imageViewProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_bar, container, false);
        imageViewSearch = view.findViewById(R.id.searchButton);
        imageViewHome = view.findViewById(R.id.homeButton);
        imageViewProfile = view.findViewById(R.id.profileButton);
        return view;
    }
}