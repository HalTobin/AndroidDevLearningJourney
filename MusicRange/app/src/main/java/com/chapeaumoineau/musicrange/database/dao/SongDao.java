package com.chapeaumoineau.musicrange.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.chapeaumoineau.musicrange.model.Album;
import com.chapeaumoineau.musicrange.model.Artist;
import com.chapeaumoineau.musicrange.model.Song;
import com.chapeaumoineau.musicrange.model.SongWithAll;

import java.util.List;

@Dao
public interface SongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void createSong(Song song);

    @Query("SELECT * FROM Song WHERE id = :songId")
    LiveData<Song> getSong(int songId);

    @Query("SELECT * FROM Song")
    LiveData<List<Song>> getAllSongs();

    @Transaction
    @Query("SELECT * FROM Song WHERE id = :songId")
    LiveData<SongWithAll> getSongWithAll(int songId);

    @Transaction
    @Query("SELECT * FROM Song WHERE albumId = :albumId")
    LiveData<List<SongWithAll>> getSongsWithAllByAlbumId(int albumId);

    @Transaction
    @Query("SELECT * FROM Song")
    LiveData<List<SongWithAll>> getAllSongsWithAll();

    @Query("SELECT * FROM Song WHERE albumId = :albumId")
    LiveData<List<Song>> getSongsByAlbum(int albumId);

    @Query("SELECT * FROM Song WHERE artistId = :artistId")
    LiveData<List<Song>> getSongsByArtist(int artistId);

    @Query("SELECT * FROM Song Where sourceId = :sourceId")
    LiveData<List<Song>> getSongsBySource(int sourceId);

    @Insert
    long insertSong(Song song);

    @Update
    int updateSong(Song song);

    @Query("DELETE FROM Song WHERE id = :songId")
    int deleteSong(int songId);

    @Query("DELETE FROM Song WHERE albumId = :albumId")
    int deleteSongsFromAlbum(int albumId);
}
