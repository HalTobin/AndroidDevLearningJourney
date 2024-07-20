package com.chapeaumoineau.musicrange.services.player;

import com.chapeaumoineau.musicrange.model.Song;
import com.chapeaumoineau.musicrange.services.player.subPlayer.LocalPlayer;

import java.io.IOException;

public interface PlayerApi {

    void initializePlayer();

    void setSong(Song toListen) throws IOException;

    void onSongEnd() throws IOException;

    int play();

    void pause();

    void setTimeCode(int newTimeCode);

    int getCurrentPosition();

    void setDuration(int duration);

    int getDuration();

    void turnOff();

    boolean isInitialized();

    boolean isPlaying();

    void setSubPlayerListener(MySubPlayerListener listener);
}
