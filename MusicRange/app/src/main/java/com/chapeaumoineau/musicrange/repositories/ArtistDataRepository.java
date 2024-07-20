package com.chapeaumoineau.musicrange.repositories;

import androidx.lifecycle.LiveData;

import com.chapeaumoineau.musicrange.database.dao.ArtistDao;
import com.chapeaumoineau.musicrange.model.Album;
import com.chapeaumoineau.musicrange.model.Artist;

import java.util.List;

public class ArtistDataRepository {

    // --- INITIALIZE DAO ---
    private final ArtistDao artistDao;

    public ArtistDataRepository(ArtistDao artistDao) {this.artistDao = artistDao;}

    // --- GETTER ---
    public LiveData<Artist> getArtist(int artistId) {return this.artistDao.getArtist(artistId);}

    public LiveData<List<Artist>> getAllArtists() {return this.artistDao.getAllArtists();}

    public LiveData<Integer> getIdByArtist(String name) {return this.artistDao.getIdByArtist(name);}

    public Integer getIntIdByArtist(String name) {return this.artistDao.getIntIdByArtist(name);}

    public boolean artistExist(String name) {return this.artistDao.artistExist(name);};

    // --- CREATE ---
    public void createArtist(Artist artist) {artistDao.createArtist(artist);}

    // --- DELETE ---
    public void deleteArtist(int artistId) {artistDao.deleteArtist(artistId);}

    // --- UPDATE ---
    public void updateArtist(Artist artist) {artistDao.updateArtist(artist);}
}
