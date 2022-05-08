package net.htlgkr.gopost.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.data.Post;

import java.util.List;

public class PostAdapter extends BaseAdapter {

    private final List<Post> posts;
    private final int layoutId;
    private final LayoutInflater inflater;

    public PostAdapter(List<Post> posts, int layoutId, LayoutInflater inflater) {
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

        ImageView profilePicture = itemView.findViewById(R.id.iv_profile_picture);
        ImageView postOptionsPicture = itemView.findViewById(R.id.iv_post_options);
        ImageView postPicture = itemView.findViewById(R.id.iv_post);
        ImageView likePicture = itemView.findViewById(R.id.iv_like);
        ImageView commentPicture = itemView.findViewById(R.id.iv_comment);
        ImageView sendPicture = itemView.findViewById(R.id.iv_send);
        ImageView markPicture = itemView.findViewById(R.id.iv_mark);
        TextView profileNameText = itemView.findViewById(R.id.tv_profile_name);
        TextView locationText = itemView.findViewById(R.id.tv_location);
        TextView likesText = itemView.findViewById(R.id.tv_likes);
        TextView commentText = itemView.findViewById(R.id.tv_comment);
        TextView seeAllCommentsText = itemView.findViewById(R.id.tv_see_all_comments);
        EditText writeCommentEdit = itemView.findViewById(R.id.et_write_comment);

        //TODO set all VIEWS!!!

        return itemView;
    }
}
