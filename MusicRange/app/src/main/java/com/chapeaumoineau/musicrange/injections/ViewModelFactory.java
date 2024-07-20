package com.chapeaumoineau.musicrange.injections;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.chapeaumoineau.musicrange.repositories.AlbumDataRepository;
import com.chapeaumoineau.musicrange.repositories.ArtistDataRepository;
import com.chapeaumoineau.musicrange.repositories.SongDataRepository;
import com.chapeaumoineau.musicrange.viewModel.MusicViewModel;

import java.util.concurrent.Executor;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private final SongDataRepository songDataSource;
    private final ArtistDataRepository artistDataSource;
    private final AlbumDataRepository albumDataSource;
    private final Executor myExecutor;

    public ViewModelFactory(SongDataRepository songDataSource, ArtistDataRepository artistDataSource, AlbumDataRepository albumDataSource, Executor myExecutor) {
        this.songDataSource = songDataSource;
        this.artistDataSource = artistDataSource;
        this.albumDataSource = albumDataSource;
        this.myExecutor = myExecutor;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if(modelClass.isAssignableFrom(MusicViewModel.class)) {
        return (T) new MusicViewModel(songDataSource, artistDataSource, albumDataSource, myExecutor);
        }
        throw new IllegalArgumentException("Unknow ViewModel class");
    }
}
