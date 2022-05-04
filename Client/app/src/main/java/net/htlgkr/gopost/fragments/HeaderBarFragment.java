package net.htlgkr.gopost.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.activity.CreatePostActivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDate;


public class HeaderBarFragment extends Fragment {

    public static final String LOG_TAG = HeaderBarFragment.class.getSimpleName();
    private static final int TAKE_PICTURE = 1;

    private ImageView imageViewIcon;
    private ImageButton optionsMenuButton;
    private TextView textViewGoPost;

    private String currentImagePath;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header_bar, container, false);
        imageViewIcon = view.findViewById(R.id.imageViewIcon);
        optionsMenuButton = view.findViewById(R.id.optionsMenuButton);
        textViewGoPost = view.findViewById(R.id.textViewGoPost);
        createPopupMenu();
        return view;
    }

    private void createPopupMenu() {
        PopupMenu options = new PopupMenu(requireActivity().getApplicationContext(), optionsMenuButton);
        setIconVisible(options);
        options.inflate(R.menu.mainfragment_popup_menu);
        options.setOnMenuItemClickListener(this::onMenuItemClick);
        optionsMenuButton.setOnClickListener(view -> options.show());
    }

    private boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menuItemCreatePost) {
            Log.i(LOG_TAG, "Pressed Create-Post Button");
            onCreatePostButtonAction();
        } else if (item.getItemId() == R.id.menuItemCreateStory) {
            Log.i(LOG_TAG, "Pressed Create-Story Button");
            onCreateStoryButtonAction();
        } else if (item.getItemId() == R.id.menuItemFeed) {
            Log.i(LOG_TAG, "Pressed Activity-Feed Button");
            onActivityFeedButtonAction();
        }
        return true;
    }

    private void onCreatePostButtonAction() {
        File photo = createImageFile();
        if (photo == null) return;

        Uri photoURI = FileProvider.getUriForFile(getContext(), "com.example.android.fileprovider", photo);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    private File createImageFile() {
        try {
            String timeStamp = LocalDate.now().toString();
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(imageFileName, ".jpg", storageDir);

            currentImagePath = image.getAbsolutePath();
            return image;
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                loadCreatePostActivity(currentImagePath);
                getActivity().finish();
            }
        }
    }

    private void loadCreatePostActivity(String imagePath) {
        Intent createPostIntent = new Intent(getContext(), CreatePostActivity.class);
        String imageBytesKey = getString(R.string.image_path_intent_key);
        createPostIntent.putExtra(imageBytesKey, imagePath);
        getActivity().startActivity(createPostIntent);
    }

    private void onCreateStoryButtonAction() {

    }

    private void onActivityFeedButtonAction() {

    }

    private void setIconVisible(PopupMenu popupMenu) {
        try {
            Field[] fields = popupMenu.getClass().getDeclaredFields();
            for (Field field : fields) {
                if ("mPopup".equals(field.getName())) {
                    field.setAccessible(true);
                    Object menuPopupHelper = field.get(popupMenu);
                    Class<?> classPopupHelper = Class.forName(menuPopupHelper
                            .getClass().getName());
                    Method setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", boolean.class);
                    setForceIcons.invoke(menuPopupHelper, true);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}