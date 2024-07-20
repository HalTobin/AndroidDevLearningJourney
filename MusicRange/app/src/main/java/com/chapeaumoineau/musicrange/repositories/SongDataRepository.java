package com.chapeaumoineau.musicrange.repositories;

import androidx.lifecycle.LiveData;

import com.chapeaumoineau.musicrange.database.dao.SongDao;
import com.chapeaumoineau.musicrange.model.Song;
import com.chapeaumoineau.musicrange.model.SongWithAll;

import java.util.List;

public class SongDataRepository {

    // --- INITIALIZE DAO ---
    private final SongDao songDao;

    public SongDataRepository(SongDao songDao) {this.songDao = songDao;}

    // --- GETTER ---
    public LiveData<Song> getSong(int songId) {return this.songDao.getSong(songId);}

    public LiveData<List<Song>> getAllSongs() {return this.songDao.getAllSongs();}

    public LiveData<List<Song>> getSongsByAlbum(int albumId) {return this.songDao.getSongsByAlbum(albumId);}

    public LiveData<List<Song>> getSongsByArtist(int artistId) {return this.songDao.getSongsByArtist(artistId);}

    public LiveData<List<Song>> getSongsBySource(int sourceId) {return this.songDao.getSongsBySource(sourceId);}

    public LiveData<List<SongWithAll>> getSongWithAll() {return this.songDao.getAllSongsWithAll();}

    public LiveData<List<SongWithAll>> getSongsWithAllByAlbumId(int albumId) {return this.songDao.getSongsWithAllByAlbumId(albumId);}

    // --- CREATE ---
    public void createSong(Song song) {songDao.createSong(song);}

    // --- DELETE ---
    public void deleteSong(int songId) {songDao.deleteSong(songId);}

    // --- UPDATE ---
    public void updateSong(Song song) {songDao.updateSong(song);}

}
