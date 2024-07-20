package com.chapeaumoineau.musicrange.injections;

import android.content.Context;

import com.chapeaumoineau.musicrange.database.MusicDatabase;
import com.chapeaumoineau.musicrange.repositories.AlbumDataRepository;
import com.chapeaumoineau.musicrange.repositories.ArtistDataRepository;
import com.chapeaumoineau.musicrange.repositories.SongDataRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Injection {
    public static SongDataRepository provideSongDataSource(Context context) {
        MusicDatabase database = MusicDatabase.getInstance(context);
        return new SongDataRepository(database.songDao());
    }

    public static ArtistDataRepository provideArtistDataSource(Context context) {
        MusicDatabase database = MusicDatabase.getInstance(context);
        return new ArtistDataRepository(database.artistDao());
    }

    public static AlbumDataRepository provideAlbumDataSource(Context context) {
        MusicDatabase database = MusicDatabase.getInstance(context);
        return new AlbumDataRepository(database.albumDao());
    }

    public static Executor provideExecutor() {return Executors.newSingleThreadExecutor();}

    public static ViewModelFactory provideViewModelFactory(Context context) {
        SongDataRepository dataSourceSong = provideSongDataSource(context);
        ArtistDataRepository dataSourceArtist = provideArtistDataSource(context);
        AlbumDataRepository dataSourceAlbum = provideAlbumDataSource(context);
        Executor executor = provideExecutor();
        return new ViewModelFactory(dataSourceSong, dataSourceArtist, dataSourceAlbum,executor);
    }
}
