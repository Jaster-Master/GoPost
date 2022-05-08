package net.htlgkr.gopost.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import net.htlgkr.gopost.client.Client;
import net.htlgkr.gopost.data.Profile;
import net.htlgkr.gopost.data.User;

public class Util {

    public static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

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

    public static void saveLoginData(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("GoPostLoginData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong("userId", Client.client.getUserId());
        editor.putString("userName", Client.client.getUserName());
        editor.putString("profileName", Client.client.getProfileName());
        editor.putString("email", Client.client.getEmail());
        editor.putString("password", Client.client.getPassword());
        String profilePicture = null;
        if (Client.client.getProfilePicture() != null) {
            profilePicture = Base64.encodeToString(Client.client.getProfilePicture(), Base64.DEFAULT);
        }
        editor.putString("profilePicture", profilePicture);
        editor.apply();
    }

    public static String generatePostURL(long postId) {
        return "https://gopost.zeige.info/post/id=" + postId;
    }

    public static String generateStoryURL(long storyId) {
        return "https://gopost.zeige.info/story/id=" + storyId;
    }
}
