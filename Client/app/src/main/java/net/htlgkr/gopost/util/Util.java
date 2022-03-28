package net.htlgkr.gopost.util;

import net.htlgkr.gopost.data.Profile;
import net.htlgkr.gopost.data.User;

public class Util {
    public User profileToUser(Profile profile) {
        return new User(profile.getUserId(), profile.getProfileName(), profile.getUserName(), profile.getPassword(), profile.getEmail(), profile.getProfilePicture());
    }
}
