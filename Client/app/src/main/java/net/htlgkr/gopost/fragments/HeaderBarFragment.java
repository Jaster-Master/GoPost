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

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.activity.CreatePostActivity;
import net.htlgkr.gopost.activity.CreateStoryActivity;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class HeaderBarFragment extends BaseFragment {

    public static final String LOG_TAG = HeaderBarFragment.class.getSimpleName();

    private ImageView imageViewIcon;
    private ImageButton optionsMenuButton;
    private TextView textViewGoPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
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

    private String currentImagePath;
    private final ActivityResultLauncher<Intent> createPostActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                loadCreatePostActivity(currentImagePath);
                                getActivity().finish();
                            }
                        }
                    });
    private final ActivityResultLauncher<Intent> createStoryActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                loadCreateStoryActivity(currentImagePath);
                                getActivity().finish();
                            }
                        }
                    });

    private void loadCameraIntent(ActivityResultLauncher<Intent> activityResultLauncher) {
        File photo = createImageFile();
        if (photo == null) return;

        Uri photoURI = FileProvider.getUriForFile(getContext(), "com.example.android.fileprovider", photo);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        activityResultLauncher.launch(takePictureIntent);
    }

    private void onCreatePostButtonAction() {
        loadCameraIntent(createPostActivityResultLauncher);
    }

    private File createImageFile() {
        try {
            String imageFileName = "take_picture_temp";
            File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(imageFileName, ".jpg", storageDir);

            currentImagePath = image.getAbsolutePath();
            return image;
        } catch (IOException e) {
            return null;
        }
    }

    private void loadCreatePostActivity(String imagePath) {
        Intent createPostIntent = new Intent(getContext(), CreatePostActivity.class);
        String imageBytesKey = getString(R.string.image_path_intent_key);
        createPostIntent.putExtra(imageBytesKey, imagePath);
        getActivity().startActivity(createPostIntent);
    }

    private void onCreateStoryButtonAction() {
        loadCameraIntent(createStoryActivityResultLauncher);
    }

    private void loadCreateStoryActivity(String imagePath) {
        Intent createStoryIntent = new Intent(getContext(), CreateStoryActivity.class);
        String imageBytesKey = getString(R.string.image_path_intent_key);
        createStoryIntent.putExtra(imageBytesKey, imagePath);
        getActivity().startActivity(createStoryIntent);
    }

    private void onActivityFeedButtonAction() {
        loadActivityFeedFragment();
    }

    private void loadActivityFeedFragment() {
        ActivityFeedFragment activityFeedFragment = new ActivityFeedFragment();
        getActivity()
                .getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentFragmentContainer, activityFeedFragment)
                .commit();
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