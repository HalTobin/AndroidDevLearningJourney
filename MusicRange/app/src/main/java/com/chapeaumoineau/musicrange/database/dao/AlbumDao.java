package com.chapeaumoineau.musicrange.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.chapeaumoineau.musicrange.model.Album;
import com.chapeaumoineau.musicrange.model.AlbumWithContent;
import com.chapeaumoineau.musicrange.model.Artist;

import java.util.List;

@Dao
public interface AlbumDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void createAlbum(Album album);

    @Query("SELECT * FROM Album WHERE id = :albumId")
    LiveData<Album> getAlbum(int albumId);

    @Query("SELECT * FROM Album")
    LiveData<List<Album>> getAllAlbumsAndPlaylists();

    @Query("SELECT * FROM Album WHERE isPlaylist = :isPlaylist")
    LiveData<List<Album>> getAllAlbums(boolean isPlaylist);

    @Query("SELECT * FROM Album WHERE isPlaylist = :isPlaylist ORDER BY title COLLATE NOCASE")
    LiveData<List<Album>> getAllAlbumsSortedByAZ(boolean isPlaylist);

    @Query("SELECT * FROM Song WHERE id = :albumId")
    LiveData<List<AlbumWithContent>> getAlbumWithContentById(int albumId);

    @Query("SELECT * FROM Album")
    LiveData<List<AlbumWithContent>> getAllAlbumWithContent();

    @Query("SELECT * FROM Album WHERE isPlaylist = :isPlaylist")
    LiveData<List<Album>> getAllPlaylist(boolean isPlaylist);

    @Query("SELECT id FROM Album WHERE title = :title")
    LiveData<Integer> getIdByAlbum(String title);

    @Query("SELECT id FROM Album WHERE title = :title")
    Integer getIntIdByAlbum(String title);

    @Query("SELECT * FROM Album WHERE title = :title")
    boolean albumExist(String title);

    @Update
    int updateAlbum(Album album);

    @Query("DELETE FROM Album WHERE id = :albumId")
    int deleteAlbum(int albumId);
}
