package net.htlgkr.gopost.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.client.Client;
import net.htlgkr.gopost.data.Post;
import net.htlgkr.gopost.packet.Packet;
import net.htlgkr.gopost.packet.PostPacket;
import net.htlgkr.gopost.util.Command;
import net.htlgkr.gopost.util.ImageConverter;
import net.htlgkr.gopost.util.ObservableValue;
import net.htlgkr.gopost.util.Util;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CreatePostActivity extends BaseActivity {

    private byte[] imageBytes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);
        String imagePath = getImageBytes(getIntent());
        Bitmap image = BitmapFactory.decodeFile(imagePath);
        imageBytes = ImageConverter.getBytesFromBitmap(image);

        new Thread(() -> {
            List<byte[]> pictures = new ArrayList<>();
            pictures.add(imageBytes);
            Post post = new Post(pictures, Client.client, Util.generatePostURL(1), LocalDateTime.now(), null, "null", null, null, null);
            PostPacket postPacket = new PostPacket(Command.UPLOAD_POST, Client.client, post);
            ObservableValue<Packet> postValue = new ObservableValue<>(postPacket);
            postValue.setOnValueSet((ObservableValue.SetListener<Packet>) value -> {
                Log.i(log_tag, "post created");
            });
            Client.getConnection().sendPacket(postValue);
        }).start();
    }

    private String getImageBytes(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras == null) return null;
        String imageBytesKey = getString(R.string.image_path_intent_key);
        return extras.getString(imageBytesKey);
    }
}