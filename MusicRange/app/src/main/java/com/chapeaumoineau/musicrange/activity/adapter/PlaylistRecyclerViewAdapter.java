package com.chapeaumoineau.musicrange.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chapeaumoineau.musicrange.R;
import com.chapeaumoineau.musicrange.activity.dialog.AlbumOrPlaylistContentDialog;
import com.chapeaumoineau.musicrange.model.Playlist;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaylistRecyclerViewAdapter extends RecyclerView.Adapter<PlaylistRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Playlist> playlistList;

    public PlaylistRecyclerViewAdapter(List<Playlist> items) {
        playlistList = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_album_box, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Playlist myPlaylist = playlistList.get(position);
        holder.title.setText(myPlaylist.getTitle());
        File myThumbnail = new File(holder.cover.getContext().getFilesDir().toString() + "/thumbnails/playlist_" + myPlaylist.getTimestamp() + ".jpg");
        if(myThumbnail.exists()) Glide.with(holder.cover.getContext()).load(myThumbnail).into(holder.cover);

        holder.itemView.setOnClickListener(v -> {
            loadAlbumContentDialog(myPlaylist, myThumbnail.getPath());
        });
    }

    @Override
    public int getItemCount() { return playlistList.size(); }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_album_box_title)
        public TextView title;
        @BindView(R.id.item_album_box_image)
        public ImageView cover;
        @BindView(R.id.item_album_box_cardview)
        public CardView albumBox;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    private void loadAlbumContentDialog(Playlist myPlaylist, String myThumbnailPath) {
        AppCompatActivity activity = (AppCompatActivity) context;
        AlbumOrPlaylistContentDialog ContentDialog = new AlbumOrPlaylistContentDialog(myPlaylist, myThumbnailPath);
        ContentDialog.show(activity.getSupportFragmentManager(), "Album Content");
    }
}
