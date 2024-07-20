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

import org.w3c.dom.Text;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetlistOptionsBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.bottomsheet_setlistoption_mini_thumbnail)
    ImageView optionSetlistThumbnail;
    @BindView(R.id.bottomsheet_setlistoption_title)
    TextView optionSetlistTitle;
    @BindView(R.id.bottomsheet_setlistoption_artist)
    TextView optionSetlistArtist;
    @BindView(R.id.bottomsheet_setlistoption_album)
    TextView optionSetlistAlbum;
    @BindView(R.id.bottomsheet_setlistoption_duration)
    TextView optionSetlistDuration;

    @BindView(R.id.bottomsheet_setlistoption_play_now_song_txt)
    TextView optionSetlistPlayNow;
    @BindView(R.id.bottomsheet_setlistoption_set_next_song_txt)
    TextView optionSetlistNextSong;
    @BindView(R.id.bottomsheet_setlistoption_move_to_end_txt)
    TextView optionSetlistMoveToEnd;
    @BindView(R.id.bottomsheet_setlistoption_remove_song_from_queu)
    TextView optionSetlistRemoveFromQueu;

    private SongWithAll mySongOption;
    private MySetlistOptionListener mySongOptionListener;

    public SetlistOptionsBottomSheet(SongWithAll mySongOption) { this.mySongOption = mySongOption; }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottomsheet_setlistoptions, container, false);
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
        if(myTempThumbnail != null) Glide.with(optionSetlistThumbnail.getContext()).load(myTempThumbnail).into(optionSetlistThumbnail);
        else Glide.with(optionSetlistThumbnail.getContext()).load(R.drawable.default_thumbnail_album).into(optionSetlistThumbnail);

        optionSetlistTitle.setText(mySongOption.getMySong().getTitle());
        optionSetlistArtist.setText(mySongOption.getMyArtist().getName());
        optionSetlistAlbum.setText(mySongOption.getMyAlbum().getTitle());
        optionSetlistDuration.setText(TimeCode.fromMillisTo_MinSec(mySongOption.getMySong().getDuration()));

        optionSetlistPlayNow.setOnClickListener(v -> {
            mySongOptionListener.onForceToPlay();
            this.dismiss();
        });

        optionSetlistNextSong.setOnClickListener(v -> {
            mySongOptionListener.onMoveToNext();
            this.dismiss();
        });

        optionSetlistMoveToEnd.setOnClickListener(v -> {
            mySongOptionListener.onMoveToEnd();
            this.dismiss();
        });

        optionSetlistRemoveFromQueu.setOnClickListener(v -> {
            mySongOptionListener.onRemove();
            this.dismiss();
        });

        return view;
    }

    public void setOptionSetlistListener(SetlistOptionsBottomSheet.MySetlistOptionListener listener) { this.mySongOptionListener = listener; }

    public interface MySetlistOptionListener {
        void onForceToPlay();
        void onMoveToNext();
        void onMoveToEnd();
        void onRemove();
    }

}
