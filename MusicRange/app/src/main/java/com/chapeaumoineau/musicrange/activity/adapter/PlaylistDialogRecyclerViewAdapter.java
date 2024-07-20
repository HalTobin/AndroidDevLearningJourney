package com.chapeaumoineau.musicrange.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chapeaumoineau.musicrange.R;
import com.chapeaumoineau.musicrange.activity.dialog.SongOptionsBottomSheet;
import com.chapeaumoineau.musicrange.event.InstructionForSetlist;
import com.chapeaumoineau.musicrange.event.OnSavePlaylist;
import com.chapeaumoineau.musicrange.model.Playlist;
import com.chapeaumoineau.musicrange.model.Song;
import com.chapeaumoineau.musicrange.model.SongWithAll;
import com.chapeaumoineau.musicrange.services.TimeCode;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaylistDialogRecyclerViewAdapter extends RecyclerView.Adapter<PlaylistDialogRecyclerViewAdapter.MyViewHolder> {

    private List<Playlist> playlistList;
    private Context context;

    public PlaylistDialogRecyclerViewAdapter(List<Playlist> items) {
        playlistList = items;
    }

    @Override
    public PlaylistDialogRecyclerViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_playlist_line, parent, false);
        return new PlaylistDialogRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlaylistDialogRecyclerViewAdapter.MyViewHolder holder, int position) {
        Playlist myPlaylist = playlistList.get(position);
        holder.playlistName.setText(myPlaylist.getTitle());

        holder.itemView.setOnClickListener(v -> {
            EventBus.getDefault().post(new OnSavePlaylist(myPlaylist));
        });
    }

    @Override
    public int getItemCount() { return playlistList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_playlist_line_name)
        public TextView playlistName;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
