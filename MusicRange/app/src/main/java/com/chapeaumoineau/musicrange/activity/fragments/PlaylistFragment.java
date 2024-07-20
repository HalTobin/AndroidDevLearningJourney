package com.chapeaumoineau.musicrange.activity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chapeaumoineau.musicrange.R;
import com.chapeaumoineau.musicrange.activity.adapter.AlbumRecyclerViewAdapter;
import com.chapeaumoineau.musicrange.activity.adapter.PlaylistRecyclerViewAdapter;
import com.chapeaumoineau.musicrange.model.Album;
import com.chapeaumoineau.musicrange.model.Playlist;
import com.chapeaumoineau.musicrange.viewModel.MusicViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.chapeaumoineau.musicrange.model.Playlist.getAllPlaylists;

public class PlaylistFragment extends Fragment {

    private List<Playlist> myPlaylist = new ArrayList<>();
    private RecyclerView myRecyclerView;

    public PlaylistFragment() {}

    public static PlaylistFragment newInstance() {
        PlaylistFragment fragment = new PlaylistFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_album, container, false);
        Context context = view.getContext();
        myRecyclerView = view.findViewById(R.id.fragment_album_recycler);
        myRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));

        myPlaylist = getAllPlaylists(context);
        refreshList();

        return view;
    }

    private void refreshList() {
        myRecyclerView.setAdapter(new PlaylistRecyclerViewAdapter(myPlaylist));
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }
}
