package net.htlgkr.gopost.util;

import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.htlgkr.gopost.data.Profile;
import net.htlgkr.gopost.data.User;

public class Util {
    public static User profileToUser(Profile profile) {
        return new User(profile.getUserId(), profile.getProfileName(), profile.getUserName(), profile.getPassword(), profile.getEmail(), profile.getProfilePicture());
    }

    public static void startLoading(ViewGroup viewGroup, ObservableValue<Boolean> isFinished) {
        ProgressBar progressBar = new ProgressBar(viewGroup.getContext());
        viewGroup.addView(progressBar);
        isFinished.setOnValueChange((oldValue, newValue) -> {
            if (newValue) {
                viewGroup.removeView(progressBar);
            }
        });
        progressBar.setScaleX(0.2f);
        progressBar.setScaleY(0.2f);
    }
}
