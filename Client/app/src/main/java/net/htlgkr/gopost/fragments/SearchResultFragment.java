package net.htlgkr.gopost.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import net.htlgkr.gopost.R;

public class SearchResultFragment extends BaseFragment {

    private EditText searchField;
    private SearchContentFragment searchContentFragment;
    private SearchHistoryFragment searchHistoryFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchField = view.findViewById(R.id.searchTextField);
        searchContentFragment = new SearchContentFragment();
        searchHistoryFragment = new SearchHistoryFragment();
        searchField.setOnClickListener(field -> loadFragment(searchHistoryFragment));
        loadFragment(searchContentFragment);
        return view;
    }

    private void loadFragment(Fragment fragment) {
        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.searchContentFragmentContainer, fragment)
                .commit();
    }
}
