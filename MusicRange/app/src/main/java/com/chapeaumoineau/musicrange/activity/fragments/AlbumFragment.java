package com.chapeaumoineau.musicrange.activity.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chapeaumoineau.musicrange.R;
import com.chapeaumoineau.musicrange.activity.adapter.AlbumRecyclerViewAdapter;
import com.chapeaumoineau.musicrange.injections.Injection;
import com.chapeaumoineau.musicrange.injections.ViewModelFactory;
import com.chapeaumoineau.musicrange.model.Album;
import com.chapeaumoineau.musicrange.model.AlbumWithContent;
import com.chapeaumoineau.musicrange.viewModel.MusicViewModel;

import java.util.ArrayList;
import java.util.List;

public class AlbumFragment extends Fragment {

    private List<AlbumWithContent> myAlbumsWithContent = new ArrayList<>();
    private List<Album> myAlbums = new ArrayList<>();
    private RecyclerView myRecyclerView;

    MusicViewModel musicViewModel;

    public AlbumFragment(MusicViewModel musicViewModel) {
        this.musicViewModel = musicViewModel;
    }

    public static AlbumFragment newInstance(MusicViewModel musicViewModel) {
        AlbumFragment fragment = new AlbumFragment(musicViewModel);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*this.musicViewModel.getAllAlbumsSortedByAZ().observe(this, albumsFromDB -> {
            this.myAlbums = albumsFromDB;
            refreshList();
        });*/

        this.musicViewModel.getAllAlbumsWithContent().observe(this, albumsWithContent -> {
            myAlbumsWithContent = albumsWithContent;
            refreshList();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_album, container, false);
        Context context = view.getContext();
        myRecyclerView = view.findViewById(R.id.fragment_album_recycler);
        myRecyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        return view;
    }

    private void refreshList() {
        myRecyclerView.setAdapter(new AlbumRecyclerViewAdapter(myAlbumsWithContent));
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }
}
