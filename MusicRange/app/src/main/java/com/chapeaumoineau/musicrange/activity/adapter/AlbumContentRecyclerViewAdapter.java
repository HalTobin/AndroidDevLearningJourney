package com.chapeaumoineau.musicrange.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.chapeaumoineau.musicrange.R;
import com.chapeaumoineau.musicrange.activity.dialog.SongOptionsBottomSheet;
import com.chapeaumoineau.musicrange.event.InstructionForSetlist;
import com.chapeaumoineau.musicrange.model.Song;
import com.chapeaumoineau.musicrange.model.SongWithAll;
import com.chapeaumoineau.musicrange.services.TimeCode;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumContentRecyclerViewAdapter extends RecyclerView.Adapter<AlbumContentRecyclerViewAdapter.MyViewHolder> {

    private List<SongWithAll> songList;

    private Context context;

    public AlbumContentRecyclerViewAdapter(List<SongWithAll> items) {
        songList = items;
    }

    @Override
    public AlbumContentRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_content_song_line, parent, false);
        return new AlbumContentRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlbumContentRecyclerViewAdapter.MyViewHolder holder, int position) {
        SongWithAll mySongWithAll = songList.get(position);
        Song mySong = mySongWithAll.getMySong();

        if(mySong.isTitleDefined()) holder.artistName.setText(mySongWithAll.getMyArtist().getName());
        else holder.artistName.setText("Unknown Artist");

        if(mySong.isArtistDefined()) holder.songTitle.setText(mySong.getTitle());
        else holder.songTitle.setText("Unknown Title");

        holder.albumDuration.setText(TimeCode.fromMillisTo_MinSec(mySong.getDuration()));

        holder.itemView.setOnClickListener(v -> {
            EventBus.getDefault().post(new InstructionForSetlist(songList, position, InstructionForSetlist.LOADALBUM));
        });

        holder.optionButton.setOnClickListener(v -> {
            SongOptionsBottomSheet myBottomSheet = new SongOptionsBottomSheet(mySongWithAll);
            myBottomSheet.setOptionSongListener(new SongOptionsBottomSheet.MySongOptionListener() {
                @Override
                public void onSetNext() {
                    EventBus.getDefault().post(new InstructionForSetlist(songList, position, InstructionForSetlist.SETNEXT));
                }

                @Override
                public void onAddedToQueu() {
                    EventBus.getDefault().post(new InstructionForSetlist(songList, position, InstructionForSetlist.ADDTOQUEU));
                }
            });
            AppCompatActivity activity = (AppCompatActivity) context;
            myBottomSheet.show(activity.getSupportFragmentManager(), "TAG");
        });
    }

    @Override
    public int getItemCount() { return songList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_content_song_line_txt_artist_name)
        public TextView artistName;
        @BindView(R.id.item_content_song_line_txt_duration)
        public TextView albumDuration;
        @BindView(R.id.item_content_song_line_txt_song_title)
        public TextView songTitle;
        @BindView(R.id.item_content_song_line_bt_more_option)
        ImageView optionButton;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
