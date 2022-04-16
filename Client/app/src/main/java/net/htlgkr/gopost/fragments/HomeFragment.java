package net.htlgkr.gopost.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.htlgkr.gopost.R;


public class HomeFragment extends Fragment {

    private HomeFragmentListener listener;
    private ImageView imageViewSearch;
    private ImageView imageViewHome;
    private ImageView imageViewProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        imageViewSearch = view.findViewById(R.id.imageViewSearch);
        imageViewHome = view.findViewById(R.id.imageViewHome);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);

        imageViewSearch.setOnClickListener(v -> listener.onInputHomeSent("Search"));
        imageViewHome.setOnClickListener(v -> listener.onInputHomeSent("Home"));
        imageViewProfile.setOnClickListener(v -> listener.onInputHomeSent("Profile"));
        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if(context instanceof HomeFragmentListener) {
            listener = (HomeFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}