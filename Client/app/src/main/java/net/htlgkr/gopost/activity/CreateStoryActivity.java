package net.htlgkr.gopost.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.util.ImageConverter;

public class CreateStoryActivity extends BaseActivity {

    private byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_story);
        String imagePath = getImageBytes(getIntent());
        Bitmap image = BitmapFactory.decodeFile(imagePath);
        imageBytes = ImageConverter.getBytesFromBitmap(image);
    }

    private String getImageBytes(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) return null;
        String imageBytesKey = getString(R.string.image_path_intent_key);
        return extras.getString(imageBytesKey);
    }
}