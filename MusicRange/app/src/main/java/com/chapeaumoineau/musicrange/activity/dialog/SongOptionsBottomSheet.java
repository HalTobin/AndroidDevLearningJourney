package com.chapeaumoineau.musicrange.activity.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chapeaumoineau.musicrange.R;
import com.chapeaumoineau.musicrange.model.SongWithAll;
import com.chapeaumoineau.musicrange.services.TimeCode;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongOptionsBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.bottomsheet_songption_mini_thumbnail)
    ImageView optionSongThumbnail;
    @BindView(R.id.bottomsheet_songption_title)
    TextView optionSongTitle;
    @BindView(R.id.bottomsheet_songption_artist)
    TextView optionSongArtist;
    @BindView(R.id.bottomsheet_songption_album)
    TextView optionSongAlbum;
    @BindView(R.id.bottomsheet_songption_duration)
    TextView optionSongDuration;

    @BindView(R.id.bottomsheet_songoption_set_next_song_txt)
    TextView optionSongNextSong;
    @BindView(R.id.bottomsheet_songoption_add_song_to_queu_txt)
    TextView optionSongAddToQueu;

    private SongWithAll mySongOption;
    private MySongOptionListener mySongOptionListener;

    public SongOptionsBottomSheet(SongWithAll mySongOption) { this.mySongOption = mySongOption; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_songoptions, container, false);
        ButterKnife.bind(this, view);

        byte[] myTempThumbnail = null;
        try {
            myTempThumbnail = mySongOption.getMySong().getThumbnail();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        }
        if(myTempThumbnail != null) Glide.with(optionSongThumbnail.getContext()).load(myTempThumbnail).into(optionSongThumbnail);
        else Glide.with(optionSongThumbnail.getContext()).load(R.drawable.default_thumbnail_album).into(optionSongThumbnail);

        optionSongTitle.setText(mySongOption.getMySong().getTitle());
        optionSongArtist.setText(mySongOption.getMyArtist().getName());
        optionSongAlbum.setText(mySongOption.getMyAlbum().getTitle());
        optionSongDuration.setText(TimeCode.fromMillisTo_MinSec(mySongOption.getMySong().getDuration()));

        optionSongNextSong.setOnClickListener(v -> {
            mySongOptionListener.onSetNext();
            this.dismiss();
        });

        optionSongAddToQueu.setOnClickListener(v -> {
            mySongOptionListener.onAddedToQueu();
            this.dismiss();
        });

        return view;
    }

    public void setOptionSongListener(MySongOptionListener listener) { this.mySongOptionListener = listener; }

    public interface MySongOptionListener {
        void onSetNext();
        void onAddedToQueu();
    }

}
