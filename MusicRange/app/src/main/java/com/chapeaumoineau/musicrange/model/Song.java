package com.chapeaumoineau.musicrange.model;

import android.media.MediaMetadataRetriever;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Entity(indices = @Index(value = {"songUrl"}, unique = true))
public class Song {

    //UNIQUE ID OF A SONG
    @PrimaryKey(autoGenerate = true)
    private int id;

    //TITLE OF THE SONG
    private String title;

    //SOURCE OF THE SONG
    private int sourceId;

    //URL OF THE SONG
    @NonNull
    private String songUrl;

    //ID'S FROM THE ARTIST ASSOCIATED TO THE SONG
    private int artistId;

    //ID'S FROM THE ALBUM ASSOCIATED TO THE SONG
    private int albumId;

    //TIME OF THE SONG
    private int duration;

    //IS ACCESSIBLE WITHOUT INTERNET
    private boolean isOffline;

    public Song() {}

    @Ignore
    public Song(int sourceId, @NonNull String songUrl, String title, int artistId, int albumId, int duration, boolean isOffline) {
        this.sourceId = sourceId;
        this.songUrl = songUrl;
        this.title = title;
        this.artistId = artistId;
        this.albumId = albumId;
        this.duration = duration;
        this.isOffline = isOffline;
    }

    @Ignore
    public Song(int sourceId, @NonNull String songUrl, String title, int duration, boolean isOffline) {
        this.sourceId = sourceId;
        this.songUrl = songUrl;
        this.title = title;
        this.duration = duration;
        this.isOffline = isOffline;
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

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    //TRYING TO GET THUMBNAIL THROUGH MP3AGIC (EXTERNAL LIBRARY)
    public byte[] getThumbnail() throws InvalidDataException, IOException, UnsupportedTagException {
        Mp3File mp3file = new Mp3File(this.songUrl);
        byte[] temp = null;
        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            temp = id3v2Tag.getAlbumImage();
        }
        return temp;
    }

    public int getTrackNumber() throws InvalidDataException, IOException, UnsupportedTagException {
        Mp3File mp3file = new Mp3File(this.songUrl);
        int temp = 0;
        if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            temp = Integer.valueOf(id3v2Tag.getTrack());
        }
        return temp;
    }

    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline(boolean offline) {
        isOffline = offline;
    }

    public boolean isTitleDefined() {
        return (this.title != null);
    }

    public boolean isArtistDefined() {
        return (this.artistId != 0);
    }

    public boolean isAlbumDefined() {
        return (this.albumId != 0);
    }
}
