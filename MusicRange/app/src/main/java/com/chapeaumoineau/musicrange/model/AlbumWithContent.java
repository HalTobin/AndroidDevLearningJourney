package com.chapeaumoineau.musicrange.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

public class AlbumWithContent {

    @Embedded
    private Album myAlbum;

    @Relation(parentColumn = "id", entityColumn = "albumId", entity = Song.class)
    private List<SongWithAll> mySongs;

    public AlbumWithContent() {}

    public AlbumWithContent(List<SongWithAll> songs, Album album) {
        this.mySongs = songs;
        this.myAlbum = album;
    }

    public List<SongWithAll> getMySongs() {
        return mySongs;
    }

    public void setMySongs(List<SongWithAll> mySong) {
        this.mySongs = mySong;
    }

    public Album getMyAlbum() {
        return myAlbum;
    }

    public void setMyAlbum(Album myAlbum) {
        this.myAlbum = myAlbum;
    }

    public static class SongTrackNumber implements Comparator<SongWithAll> {
        @Override
        public int compare(SongWithAll left, SongWithAll right) {
            int result = 0;
            try {
                result = left.getMySong().getTrackNumber() - right.getMySong().getTrackNumber();
            } catch (InvalidDataException | IOException | UnsupportedTagException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
