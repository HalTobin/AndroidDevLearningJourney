package com.chapeaumoineau.musicrange.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.chapeaumoineau.musicrange.model.Artist;
import com.chapeaumoineau.musicrange.model.Song;

import java.util.List;

@Dao
public interface ArtistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void createArtist(Artist artist);

    @Query("SELECT * FROM Artist WHERE id = :artistId")
    LiveData<Artist> getArtist(int artistId);

    @Query("SELECT * FROM Artist")
    LiveData<List<Artist>> getAllArtists();

    @Query("SELECT id FROM Artist WHERE name = :name")
    LiveData<Integer> getIdByArtist(String name);

    @Query("SELECT id FROM Artist WHERE name = :name")
    Integer getIntIdByArtist(String name);

    @Query("SELECT * FROM Artist WHERE name = :name")
    boolean artistExist(String name);

    @Update
    int updateArtist(Artist artist);

    @Query("DELETE FROM Artist WHERE id = :artistId")
    int deleteArtist(int artistId);

    @Query("DELETE FROM Song WHERE artistId = :artistId")
    int deleteSongsFromArtist(int artistId);
}
