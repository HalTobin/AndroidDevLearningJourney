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
import com.chapeaumoineau.musicrange.model.Album;
import com.chapeaumoineau.musicrange.model.AlbumWithContent;


import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<AlbumWithContent> albumList;

    public AlbumRecyclerViewAdapter(List<AlbumWithContent> items) {
        albumList = items;
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
        Album myAlbum = albumList.get(position).getMyAlbum();
        holder.title.setText(myAlbum.getTitle());
        File myThumbnail = new File(holder.cover.getContext().getFilesDir().toString() + "/thumbnails/" + myAlbum.getId() + ".jpg");
        if(myThumbnail.exists()) Glide.with(holder.cover.getContext()).load(myThumbnail).into(holder.cover);
        holder.itemView.setOnClickListener(v -> {
            loadAlbumContentDialog(albumList.get(position));
        });
    }

    @Override
    public int getItemCount() { return albumList.size(); }

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

    private void loadAlbumContentDialog(AlbumWithContent albumToLoad) {
        AppCompatActivity activity = (AppCompatActivity) context;
        AlbumOrPlaylistContentDialog albumContentDialog = new AlbumOrPlaylistContentDialog(albumToLoad);
        albumContentDialog.show(activity.getSupportFragmentManager(), "Album Content");
    }
}
