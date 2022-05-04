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

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends BaseAdapter {
    private List<Post> posts = new ArrayList<>();
    private int layoutid;
    private LayoutInflater inflater;

    public PostAdapter(List<Post> posts, int layoutid, LayoutInflater inflater) {
        this.posts = posts;
        this.layoutid = layoutid;
        this.inflater = inflater;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Post post = posts.get(i);
        View listitemView = (view==null)?inflater.inflate(layoutid,null):view;
        ImageView profilePicture = listitemView.findViewById(R.id.iv_ProfilePicture);
        ImageView postOptionsPicture = listitemView.findViewById(R.id.iv_PostOptions);
        ImageView postPicture = listitemView.findViewById(R.id.iv_Post);
        ImageView likePicture = listitemView.findViewById(R.id.iv_Like);
        ImageView commentPicture = listitemView.findViewById(R.id.iv_Comment);
        ImageView sendPicture = listitemView.findViewById(R.id.iv_send);
        ImageView markPicture = listitemView.findViewById(R.id.iv_mark);
        TextView profilenameText = listitemView.findViewById(R.id.tv_ProfileName);
        TextView locationText = listitemView.findViewById(R.id.tv_Location);
        TextView likesText = listitemView.findViewById(R.id.tv_Likes);
        TextView commentText = listitemView.findViewById(R.id.tv_Comment);
        TextView seeAllCommentsText = listitemView.findViewById(R.id.tv_SeeAllComments);
        EditText writeCommentEdit = listitemView.findViewById(R.id.et_writeComment);

        //TODO set all VIEWS!!!

        return listitemView;
    }
}
