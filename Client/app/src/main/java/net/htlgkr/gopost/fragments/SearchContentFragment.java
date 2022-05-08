package net.htlgkr.gopost.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import net.htlgkr.gopost.R;
import net.htlgkr.gopost.adapter.PostGridAdapter;
import net.htlgkr.gopost.client.Client;
import net.htlgkr.gopost.data.Post;
import net.htlgkr.gopost.packet.Packet;
import net.htlgkr.gopost.packet.PostPacket;
import net.htlgkr.gopost.util.Command;
import net.htlgkr.gopost.util.ObservableValue;
import net.htlgkr.gopost.util.Util;

import java.util.ArrayList;
import java.util.List;

public class SearchContentFragment extends BaseFragment {

    private GridView postsGridView;
    private PostGridAdapter postGridAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search_content, container, false);
        postsGridView = view.findViewById(R.id.postsGridView);
        List<Post> posts = new ArrayList<>();
        requestRecommendationPosts(posts);
        fillGridView(posts);
        return view;
    }

    private void requestRecommendationPosts(List<Post> posts) {
        new Thread(() -> {
            Post post = new Post();
            post.setUrl(Util.generatePostURL(1));
            PostPacket postPacket = new PostPacket(Command.GET_POST, Client.client, post);
            ObservableValue<Packet> postValue = new ObservableValue<>(postPacket);
            postValue.setOnValueSet((ObservableValue.SetListener<Packet>) value -> {
                if (!(value instanceof PostPacket)) return;
                posts.add(((PostPacket) value).getPost());
                Log.i("amogus", String.valueOf(((PostPacket) value).getPost().getPictures().get(0).length));
                fillGridView(posts);
            });
            Client.getConnection().sendPacket(postValue);
        }).start();
    }

    private void fillGridView(List<Post> posts) {
        postGridAdapter = new PostGridAdapter(posts, R.layout.post_gridview_layout, getLayoutInflater());
        postsGridView.setAdapter(postGridAdapter);
    }
}
