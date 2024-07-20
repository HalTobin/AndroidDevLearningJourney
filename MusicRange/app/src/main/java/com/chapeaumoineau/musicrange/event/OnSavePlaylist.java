package com.chapeaumoineau.musicrange.event;

import com.chapeaumoineau.musicrange.model.Playlist;
import com.chapeaumoineau.musicrange.model.SongWithAll;

import java.util.List;

public class OnSavePlaylist {
    public Playlist playlist;

    public OnSavePlaylist(Playlist playlist) {
        this.playlist = playlist;
    }
}
