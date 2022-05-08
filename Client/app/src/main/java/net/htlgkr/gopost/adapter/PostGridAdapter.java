package net.htlgkr.gopost.adapter;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.data.Post;
import net.htlgkr.gopost.util.ImageConverter;

import java.util.List;

public class PostGridAdapter extends BaseAdapter {

    private final List<Post> posts;
    private final int layoutId;
    private final LayoutInflater inflater;

    public PostGridAdapter(List<Post> posts, int layoutId, LayoutInflater inflater) {
        this.posts = posts;
        this.layoutId = layoutId;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int i) {
        return posts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Post post = posts.get(i);
        View itemView = (view == null) ? inflater.inflate(layoutId, null) : view;

        ImageButton postImage = itemView.findViewById(R.id.postsGridViewImageButton);
        Bitmap postBitmap = ImageConverter.getBitmapFromBytes(post.getPictures().get(0));
        Log.i("amogus", String.valueOf(postBitmap));
        postImage.setImageBitmap(postBitmap);

        return itemView;
    }
}
