package com.chapeaumoineau.musicrange.repositories;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.chapeaumoineau.musicrange.database.dao.AlbumDao;
import com.chapeaumoineau.musicrange.model.Album;
import com.chapeaumoineau.musicrange.model.AlbumWithContent;
import com.chapeaumoineau.musicrange.model.Song;

import java.util.List;

public class AlbumDataRepository {

    // --- INITIALIZE DAO ---
    private final AlbumDao albumDao;

    public AlbumDataRepository(AlbumDao albumDao) {this.albumDao = albumDao;}

    // --- GETTER ---
    public LiveData<Album> getAlbum(int albumId) {return this.albumDao.getAlbum(albumId);}

    public LiveData<List<Album>> getAllAlbumsAndPlaylists() {return this.albumDao.getAllAlbumsAndPlaylists();}

    public LiveData<List<Album>> getAllAlbums() {return this.albumDao.getAllAlbums(false);}

    public LiveData<List<Album>> getAllAlbumsSortedByAZ() {return this.albumDao.getAllAlbumsSortedByAZ(false);}

    public LiveData<List<AlbumWithContent>> getAllAlbumWithContent() {return this.albumDao.getAllAlbumWithContent();}

    public LiveData<List<Album>> getAllPlaylists() {return this.albumDao.getAllAlbums(true);}

    public LiveData<Integer> getIdByAlbum(String name) {return this.albumDao.getIdByAlbum(name);}

    public Integer getIntIdByAlbum(String name) {return this.albumDao.getIntIdByAlbum(name);}

    public boolean albumExist(String title) {return this.albumDao.albumExist(title);};

    // --- CREATE ---
    public void createAlbum(Album album) {albumDao.createAlbum(album);}

    // --- DELETE ---
    public void deleteAlbum(int albumId) {albumDao.deleteAlbum(albumId);}

    // --- UPDATE ---
    public void updateAlbum(Album album) {albumDao.updateAlbum(album);}
}
