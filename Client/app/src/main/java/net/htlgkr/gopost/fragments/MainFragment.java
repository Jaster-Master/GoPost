package net.htlgkr.gopost.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
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


public class MainFragment extends Fragment {

    private MainFragmentListener listener;

    private ImageView imageViewIcon;
    private ImageView imageViewMenu;
    private TextView textViewGoPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        imageViewIcon = view.findViewById(R.id.imageViewIcon);
        imageViewMenu = view.findViewById(R.id.imageViewMenu);
        textViewGoPost = view.findViewById(R.id.textViewGoPost);


        imageViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopupMenu(v);
            }
        });
        return view;
    }

    @SuppressLint("RestrictedApi")
    private void createPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(requireActivity().getApplicationContext(), v);
        setIconVisible(popupMenu);
        popupMenu.inflate(R.menu.mainfragment_popup_menu);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return menuItemClicked(item);
            }
        });
        popupMenu.show();
    }

    private boolean menuItemClicked(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuItemCreatePost:
                listener.onInputMainSent("CreatePost");
                break;
            case R.id.menuItemCreateStory:
                listener.onInputMainSent("CreateStory");
        }
        return true;
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof HomeFragmentListener) {
            listener = (MainFragmentListener) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}