package com.chapeaumoineau.musicrange.activity.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.FileUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chapeaumoineau.musicrange.R;
import com.chapeaumoineau.musicrange.activity.adapter.AlbumContentRecyclerViewAdapter;
import com.chapeaumoineau.musicrange.event.InstructionForSetlist;
import com.chapeaumoineau.musicrange.model.AlbumWithContent;
import com.chapeaumoineau.musicrange.model.Playlist;
import com.chapeaumoineau.musicrange.model.SongWithAll;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumOrPlaylistContentDialog extends AppCompatDialogFragment {

    @BindView(R.id.sub_fragment_album_content_album_thumbnail)
    ImageView albumCover;
    @BindView(R.id.sub_fragment_album_content_album_title)
    TextView albumTitle;
    @BindView(R.id.sub_fragment_album_content_track_number_and_duration)
    TextView trackNumberAndAlbumDuration;

    private PickDialogTitleListener listener;

    private List<SongWithAll> mySongsWithAll;
    private String myTitle = "";
    private byte[] myThumbnail = null;
    private String myThumbnailPath = "";
    private RecyclerView myRecyclerView;

    private AlbumContentRecyclerViewAdapter myAdapter;

    public AlbumOrPlaylistContentDialog(AlbumWithContent myContent) {
        myTitle = myContent.getMyAlbum().getTitle();
        mySongsWithAll = myContent.getMySongs();
        try {
            myThumbnail = mySongsWithAll.get(0).getMySong().getThumbnail();
        } catch (InvalidDataException | IOException | UnsupportedTagException e) {
            e.printStackTrace();
        }
    }

    public AlbumOrPlaylistContentDialog(Playlist myContent, String myThumbnailPath) {
        myTitle = myContent.getTitle();
        mySongsWithAll = myContent.getList();
        this.myThumbnailPath = myThumbnailPath;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CornerDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_album_content, null);
        myRecyclerView = view.findViewById(R.id.dialog_fragment_song_list);
        builder.setView(view);

        ButterKnife.bind(this, view);
        albumTitle.setText(myTitle);
        if(myThumbnailPath.equals("")) Glide.with(albumCover.getContext()).load(myThumbnail).into(albumCover);
        else Glide.with(albumCover.getContext()).load(myThumbnailPath).into(albumCover);
        refreshList();

        return builder.create();
    }

    private void initRecycler() {
        myAdapter = new AlbumContentRecyclerViewAdapter(mySongsWithAll);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        myRecyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        initRecycler();
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
    public void getTitle(InstructionForSetlist event) throws IOException {
        listener.sendTitle(mySongsWithAll, event.position, event.mode);
        this.dismiss();
    }

    private void refreshList() {
        myRecyclerView.setAdapter(new AlbumContentRecyclerViewAdapter(mySongsWithAll));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (PickDialogTitleListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement PickDialogTitleListener");
        }
    }

    public interface PickDialogTitleListener {
        void sendTitle(List<SongWithAll> mySongsWithAlls, int position, int mode) throws IOException;
    }
}
