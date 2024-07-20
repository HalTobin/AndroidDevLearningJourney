package com.chapeaumoineau.musicrange.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = @Index(value = {"title"}, unique = true))
public class Album {

    //UNIQUE ID AN ALBUM
    @PrimaryKey (autoGenerate = true)
    int id;

    //TITLE OF THE ALBUM
    String title;

    //IS AN ALBUM OR A COMPILE/PLAYLIST || false -> ALBUM || true -> COMPILE/PLAYLIST
    boolean isPlaylist;

    public Album() {}

    @Ignore
    public Album(String title) {
        this.title = title;
        this.isPlaylist = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isPlaylist() {
        return isPlaylist;
    }

    public void setPlaylist(boolean playlist) {
        isPlaylist = playlist;
    }
}
