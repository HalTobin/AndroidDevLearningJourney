package com.chapeaumoineau.musicrange.model;

import androidx.room.DatabaseView;
import androidx.room.Embedded;
import androidx.room.Relation;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;
import java.util.Comparator;

public class SongWithAll {

    @Embedded
    private Song mySong;

    @Relation(parentColumn = "artistId", entityColumn = "id")
    private Artist myArtist;

    @Relation(parentColumn = "albumId", entityColumn = "id")
    private Album myAlbum;

    @Relation(parentColumn = "sourceId", entityColumn = "id")
    private Source mySource;

    public SongWithAll() {}

    public SongWithAll(Song song, Artist artist, Album album, Source source) {
        this.mySong = song;
        this.myArtist = artist;
        this.myAlbum = album;
        this.mySource = source;
    }

    public Song getMySong() {
        return mySong;
    }

    public void setMySong(Song mySong) {
        this.mySong = mySong;
    }

    public Artist getMyArtist() {
        return myArtist;
    }

    public void setMyArtist(Artist myArtist) {
        this.myArtist = myArtist;
    }

    public Album getMyAlbum() {
        return myAlbum;
    }

    public void setMyAlbum(Album myAlbum) {
        this.myAlbum = myAlbum;
    }

    public Source getMySource() {
        return mySource;
    }

    public void setMySource(Source mySource) {
        this.mySource = mySource;
    }

    public static class SongTrackNumber implements Comparator<SongWithAll> {
        @Override
        public int compare(SongWithAll left, SongWithAll right) {
            int result = 0;
            try {
                result = left.getMySong().getTrackNumber() - right.getMySong().getTrackNumber();
            } catch (InvalidDataException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (UnsupportedTagException e) {
                e.printStackTrace();
            }
            return result;
        }
    }
}
