package com.chapeaumoineau.musicrange.services.player.subPlayer;

import android.media.AudioManager;
import android.media.MediaPlayer;

import com.chapeaumoineau.musicrange.model.Song;
import com.chapeaumoineau.musicrange.services.player.MySubPlayerListener;
import com.chapeaumoineau.musicrange.services.player.PlayerApi;

import java.io.IOException;

public class LocalPlayer implements PlayerApi {

    private MediaPlayer localPlayer;
    private int duration = 0;
    private boolean isInitialized;

    private MySubPlayerListener listener;

    public LocalPlayer() {
        this.listener = null;
    }

    @Override
    public void initializePlayer() {
        localPlayer = new MediaPlayer();
        localPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        isInitialized = true;
    }

    @Override
    public void turnOff() {
        localPlayer.release();
        isInitialized = false;
        duration = 0;
    }

    @Override
    public void setSong(Song toListen) throws IOException {
        if(isPlaying()) localPlayer.reset();
        initializePlayer();
        duration = 0;
        try {
            localPlayer.setDataSource(toListen.getSongUrl());
            localPlayer.prepare();
            play();
        }
        catch (IOException e) {e.printStackTrace();}
    }

    @Override
    public void onSongEnd() throws IOException {
        this.listener.onSongEnded();
    }

    @Override
    public int play() {
        if(localPlayer==null || !isInitialized) initializePlayer();
        setDuration(localPlayer.getDuration());

        localPlayer.start();
        getCurrentPosition();

        localPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {

            }
        });

        localPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    onSongEnd();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return getDuration();
    }

    @Override
    public void pause() {
        if(isPlaying()) {
            localPlayer.pause();
        }
    }

    @Override
    public void setTimeCode(int newTimeCode) {
        if(isInitialized) localPlayer.seekTo(newTimeCode);
    }

    @Override
    public int getCurrentPosition() {
        int toReturn = 0;
        if((localPlayer != null) && (isPlaying())) toReturn = localPlayer.getCurrentPosition();
        return toReturn;
    }

    @Override
    public void setDuration(int duration) { this.duration = duration; }

    @Override
    public int getDuration() { return this.duration; }

    @Override
    public boolean isInitialized() { return this.isInitialized; }

    @Override
    public boolean isPlaying() {
        boolean toReturn = false;
        if(isInitialized)
            try { toReturn = this.localPlayer.isPlaying(); }
            catch (Exception e) {
                e.printStackTrace();
                toReturn = false;
            }
        else toReturn = false;
        return toReturn;
    }

    @Override
    public void setSubPlayerListener(MySubPlayerListener listener) { this.listener = listener; }

}
