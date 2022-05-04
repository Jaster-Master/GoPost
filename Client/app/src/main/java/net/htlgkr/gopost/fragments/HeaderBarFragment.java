package net.htlgkr.gopost.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import net.htlgkr.gopost.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


public class HeaderBarFragment extends Fragment {

    public static final String LOG_TAG = HeaderBarFragment.class.getSimpleName();
    private static final int TAKE_PICTURE = 1;

    private ImageView imageViewIcon;
    private ImageView imageViewMenu;
    private TextView textViewGoPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header_bar, container, false);
        imageViewIcon = view.findViewById(R.id.imageViewIcon);
        imageViewMenu = view.findViewById(R.id.imageViewMenu);
        textViewGoPost = view.findViewById(R.id.textViewGoPost);


        imageViewMenu.setOnClickListener(this::createPopupMenu);
        return view;
    }

    private void createPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(requireActivity().getApplicationContext(), v);
        setIconVisible(popupMenu);
        popupMenu.inflate(R.menu.mainfragment_popup_menu);

        popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
        popupMenu.show();
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

    private final String filename = "amogus.jpeg";

    private void onCreatePostButtonAction() {
        /*String rootDir = getContext().getFilesDir().getAbsolutePath();
        File file = new File(rootDir + filename);
        FileProvider fileProvider = new FileProvider();
        ParcelFileDescriptor descriptor = fileProvider.openFile(file.toURI(), null);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, descriptor);
        startActivityForResult(intent, TAKE_PICTURE);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == TAKE_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                String rootDir = getContext().getFilesDir().getAbsolutePath();
                File file = new File(rootDir + filename);
                Bitmap takenImage = BitmapFactory.decodeFile(file.getAbsolutePath());
                byte[] imageBytes = ImageConverter.getBytesFromBitmap(takenImage);
                Log.i("amogus", String.valueOf(imageBytes.length));
            }
        }*/
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