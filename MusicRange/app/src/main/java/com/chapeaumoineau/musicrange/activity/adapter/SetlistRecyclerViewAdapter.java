package com.chapeaumoineau.musicrange.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chapeaumoineau.musicrange.R;
import com.chapeaumoineau.musicrange.activity.dialog.SetlistOptionsBottomSheet;
import com.chapeaumoineau.musicrange.event.InstructionForSetlist;
import com.chapeaumoineau.musicrange.model.SongWithAll;
import com.chapeaumoineau.musicrange.services.TimeCode;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetlistRecyclerViewAdapter extends RecyclerView.Adapter<SetlistRecyclerViewAdapter.MyViewHolder> {

    private List<SongWithAll> songList;
    private int highlight;

    private Context context;

    public SetlistRecyclerViewAdapter(List<SongWithAll> items, int highlight) {
        this.highlight = highlight;
        songList = items;
    }

    @Override
    public SetlistRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_setlist_line, parent, false);
        return new SetlistRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SetlistRecyclerViewAdapter.MyViewHolder holder, int position) {
        SongWithAll mySong = songList.get(position);

        if(mySong.getMySong().isTitleDefined()) holder.viewSongTitle.setText(mySong.getMySong().getTitle());
        else holder.viewSongTitle.setText("Unknown Title");

        if(mySong.getMySong().isArtistDefined()) holder.viewArtistName.setText(mySong.getMyArtist().getName());
        else holder.viewArtistName.setText("Unknown Artist");

        if(mySong.getMySong().isAlbumDefined()) holder.viewAlbumTitle.setText(mySong.getMyAlbum().getTitle());
        else holder.viewAlbumTitle.setText("Unknown Album");

        holder.viewDuration.setText(TimeCode.fromMillisTo_MinSec(mySong.getMySong().getDuration()));

        if(highlight==position) {
            holder.itemView.setBackgroundColor(holder.itemView.getResources().getColor(R.color.design_default_color_secondary));
        }
        else {
            holder.itemView.setBackgroundColor(0x00000000);
        }

        holder.itemView.setOnClickListener(v -> {
            SetlistOptionsBottomSheet myBottomSheet = new SetlistOptionsBottomSheet(mySong);
            myBottomSheet.setOptionSetlistListener(new SetlistOptionsBottomSheet.MySetlistOptionListener() {
                @Override
                public void onForceToPlay() {
                    EventBus.getDefault().post(new InstructionForSetlist(songList, position, InstructionForSetlist.FORCEPLAY));
                }

                @Override
                public void onMoveToNext() {
                    EventBus.getDefault().post(new InstructionForSetlist(songList, position, InstructionForSetlist.MOVETONEXT));
                }

                @Override
                public void onMoveToEnd() {
                    EventBus.getDefault().post(new InstructionForSetlist(songList, position, InstructionForSetlist.MOVETOEND));
                }

                @Override
                public void onRemove() {
                    EventBus.getDefault().post(new InstructionForSetlist(songList, position, InstructionForSetlist.REMOVE));
                }
            });
            AppCompatActivity activity = (AppCompatActivity) context;
            myBottomSheet.show(activity.getSupportFragmentManager(), "TAG");
        });
    }

    @Override
    public int getItemCount() { return songList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_setlist_line_title)
        public TextView viewSongTitle;
        @BindView(R.id.item_setlist_line_artist)
        public TextView viewArtistName;
        @BindView(R.id.item_setlist_line_album)
        public TextView viewAlbumTitle;
        @BindView(R.id.item_setlist_line_duration)
        public TextView viewDuration;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
