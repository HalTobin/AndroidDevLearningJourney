package com.chapeaumoineau.musicrange.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chapeaumoineau.musicrange.R;
import com.chapeaumoineau.musicrange.activity.adapter.PlaylistDialogRecyclerViewAdapter;
import com.chapeaumoineau.musicrange.event.OnSavePlaylist;
import com.chapeaumoineau.musicrange.model.Playlist;
import com.chapeaumoineau.musicrange.model.SongWithAll;
import com.google.android.material.textfield.TextInputEditText;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.chapeaumoineau.musicrange.model.Playlist.createAutoThumbnail;
import static com.chapeaumoineau.musicrange.model.Playlist.getAllPlaylists;
import static com.chapeaumoineau.musicrange.model.Playlist.savePlaylist;

public class SavePlaylistDialog extends AppCompatDialogFragment {

    @BindView(R.id.dialog_save_playlist_new_bt)
    ImageView newPlaylistButton;
    @BindView(R.id.dialog_save_playlist_text_input)
    TextInputEditText newPlaylistTitle;

    private List<Playlist> myPlaylists = new ArrayList<>();
    private List<SongWithAll> mySongs;
    private RecyclerView myRecyclerView;

    Context context;
    private PlaylistDialogRecyclerViewAdapter myAdapter;

    public SavePlaylistDialog(List<SongWithAll> mySongs) { this.mySongs = mySongs; }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CornerDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_save_playlist, null);
        myRecyclerView = view.findViewById(R.id.dialog_save_playlist_list);
        builder.setView(view);
        ButterKnife.bind(this, view);
        context = view.getContext();

        myPlaylists = getAllPlaylists(context);

        newPlaylistButton.setOnClickListener(v -> {
            String title = newPlaylistTitle.getText().toString();
            long timestamp = new Date().getTime();
            Playlist myNewPlaylist = new Playlist(title, timestamp, mySongs);
            saveMyPlaylist(myNewPlaylist);
        });

        return builder.create();
    }

    private void initRecycler() {
        myAdapter = new PlaylistDialogRecyclerViewAdapter(myPlaylists);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        myRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecycler();
    }

    private void refreshList() {
        myRecyclerView.setAdapter(new PlaylistDialogRecyclerViewAdapter(myPlaylists));
    }

    private void saveMyPlaylist(Playlist myPlaylistToSave) {
        if(!newPlaylistTitle.getText().equals("")) {
            try {
                savePlaylist(myPlaylistToSave, context);
                createAutoThumbnail(myPlaylistToSave, context);
                dismiss();
            } catch (InvalidDataException | IOException | UnsupportedTagException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void getPlaylist(OnSavePlaylist event) {
        Playlist myPlaylist = event.playlist;
        myPlaylist.setList(mySongs);
        saveMyPlaylist(myPlaylist);
        this.dismiss();
    }

}
