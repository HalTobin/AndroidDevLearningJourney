package com.chapeaumoineau.musicrange.services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import com.chapeaumoineau.musicrange.injections.Injection;
import com.chapeaumoineau.musicrange.injections.ViewModelFactory;
import com.chapeaumoineau.musicrange.model.Album;
import com.chapeaumoineau.musicrange.model.Artist;
import com.chapeaumoineau.musicrange.model.Song;
import com.chapeaumoineau.musicrange.viewModel.MusicViewModel;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import static com.chapeaumoineau.musicrange.utils.playerValue.*;

public class Scan {

    Context context;
    private MusicViewModel musicViewModel;

    int recorded = 0;

    public Scan(Context context) {
        this.context = context;
        configureViewModel();
        this.musicViewModel.getAllSongs().observe((LifecycleOwner) context, songsFromDB -> {
            this.recorded = songsFromDB.size();
        });
    }

    public void sync(int TO_SCAN) {
        this.musicViewModel.resetOP_RUNNING();
        switch(TO_SCAN) {
            case ALL:
                syncLocal();
                break;
            case LOCAL:
                syncLocal();
                break;
            case SPOTIFY:
                syncSpotify();
                break;
        }
    }

    public void syncLocal() {
        ContentResolver contentResolver = context.getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String mySelection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor songCursor = contentResolver.query(songUri, null, mySelection, null, null);

        if(songCursor != null && songCursor.moveToFirst()) {
            do {
                //GET METADATA FROM SONG
                String myTitle = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String myPath = songCursor.getString(songCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                int myDuration = Integer.parseInt(songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                String myArtist = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                String myAlbum = songCursor.getString(songCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));

                //SAVE SONG AND ITS METADATA TO LOCAL DATABASE
                Song mySong = new Song(1, myPath, myTitle, myDuration, true);
                musicViewModel.createSongWithAll(mySong, new Artist(myArtist), new Album(myAlbum));
                System.out.println("OP : " + musicViewModel.getOP_RUNNING());
            } while(songCursor.moveToNext());
        }
        songCursor.close();
        while (musicViewModel.getOP_RUNNING() != recorded) {
            System.out.println(musicViewModel.getOP_RUNNING() + " ? " + recorded);
        }
    }

    void syncSpotify() {

    }

    public int getCurrentOperations() {
        return this.musicViewModel.getOP_RUNNING();
    }

    private void configureViewModel() {
        ViewModelFactory myViewModelFactory = Injection.provideViewModelFactory(context);
        this.musicViewModel = ViewModelProviders.of((AppCompatActivity) context, myViewModelFactory).get(MusicViewModel.class);
        this.musicViewModel.setFilesDir(context.getFilesDir());
    }

}
