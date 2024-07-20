package com.chapeaumoineau.musicrange.activity.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.chapeaumoineau.musicrange.R;
import com.chapeaumoineau.musicrange.injections.DI;
import com.chapeaumoineau.musicrange.model.SongWithAll;
import com.chapeaumoineau.musicrange.services.TimeCode;
import com.chapeaumoineau.musicrange.services.player.MasterPlayer;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerFragment extends Fragment {

    @BindView(R.id.fragment_player_thumbnail)
    ImageView playerThumbnail;
    @BindView(R.id.fragment_player_title)
    TextView playerTitle;
    @BindView(R.id.fragment_player_artist)
    TextView playerArtist;
    @BindView(R.id.fragment_player_album)
    TextView playerAlbum;

    @BindView(R.id.fragment_player_current_timecode)
    TextView playerCurrentTimeCode;
    @BindView(R.id.fragment_player_seekbar)
    SeekBar playerSeekbar;
    @BindView(R.id.fragment_player_max_timecode)
    TextView playerMaxTimeCode;

    @BindView(R.id.fragment_player_bt_play_or_pause)
    ImageView playerPlayOrPause;

    Runnable runnable;
    Handler handler;

    private MasterPlayer myPlayer;
    SongWithAll currentSong;

    public PlayerFragment() {}

    public static PlayerFragment newInstance() {
        PlayerFragment fragment = new PlayerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myPlayer = DI.getPlayerApi();
        myPlayer.setPlayerListener(new MasterPlayer.MyPlayerListener() {
            @Override
            public void onSongSetted() throws IOException {
                refreshPlayer();
            }
        });
        handler = new Handler();
    }

    @OnClick(R.id.fragment_player_bt_play_or_pause)
    void btPlayOrPauseClicked(View view) {
        myPlayer.playOrPause();
        setPlayOrPauseButtonState();
    }

    @OnClick(R.id.fragment_player_bt_next)
    void nextClicked(View view) {
        try {
            myPlayer.nextSong();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.fragment_player_bt_prev)
    void prevClicked(View view)  {
        try {
            myPlayer.prevSong();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        ButterKnife.bind(this, view);

        playerPlayOrPause.setImageResource(R.drawable.bt_player_play);

        playerSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser) {
                    myPlayer.setCurrentTimeCode(progress);
                    seekBar.setProgress(progress);
                }
                playerCurrentTimeCode.setText(TimeCode.fromMillisTo_MinSec(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        updateSeekbar();

        return view;
    }

    public void refreshPlayer() {
        currentSong = myPlayer.getCurrentSong();
        if(currentSong != null && playerTitle != null) {
            try {
                Glide.with(playerThumbnail.getContext()).load(currentSong.getMySong().getThumbnail()).into(playerThumbnail);
            } catch (InvalidDataException e) {
                e.printStackTrace();
                Glide.with(playerThumbnail).load(R.drawable.default_thumbnail_album).into(playerThumbnail);
            } catch (IOException e) {
                e.printStackTrace();
                Glide.with(playerThumbnail).load(R.drawable.default_thumbnail_album).into(playerThumbnail);
            } catch (UnsupportedTagException e) {
                e.printStackTrace();
                Glide.with(playerThumbnail).load(R.drawable.default_thumbnail_album).into(playerThumbnail);
            }

            if (currentSong.getMySong().isTitleDefined()) playerTitle.setText(currentSong.getMySong().getTitle());
            else playerTitle.setText("Unknown Title");

            if (currentSong.getMySong().isArtistDefined()) playerArtist.setText(currentSong.getMyArtist().getName());
            else playerArtist.setText("Unknown Artist");

            if (currentSong.getMySong().isAlbumDefined()) playerAlbum.setText(currentSong.getMyAlbum().getTitle());
            else playerAlbum.setText("Unknown Album");

            playerMaxTimeCode.setText(TimeCode.fromMillisTo_MinSec(myPlayer.getDuration()));

            setPlayOrPauseButtonState();
            playerSeekbar.setMax(myPlayer.getDuration());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshPlayer();
    }

    public void setPlayOrPauseButtonState() {
        if(myPlayer.isPlaying()&&(myPlayer!=null)) playerPlayOrPause.setImageResource(R.drawable.bt_player_pause);
        else playerPlayOrPause.setImageResource(R.drawable.bt_player_play);
    }

    public void updateSeekbar() {
        playerSeekbar.setProgress(myPlayer.getCurrentPosition());
        playerCurrentTimeCode.setText(TimeCode.fromMillisTo_MinSec(myPlayer.getCurrentPosition()));

        runnable = new Runnable() {
            @Override
            public void run() {
                updateSeekbar();
            }
        };
        handler.postDelayed(runnable, 1000);
    }

}
