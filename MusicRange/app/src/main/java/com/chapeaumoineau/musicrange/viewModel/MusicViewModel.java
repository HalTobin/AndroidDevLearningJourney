package com.chapeaumoineau.musicrange.viewModel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.chapeaumoineau.musicrange.model.Album;
import com.chapeaumoineau.musicrange.model.AlbumWithContent;
import com.chapeaumoineau.musicrange.model.Artist;
import com.chapeaumoineau.musicrange.model.Song;
import com.chapeaumoineau.musicrange.model.SongWithAll;
import com.chapeaumoineau.musicrange.repositories.AlbumDataRepository;
import com.chapeaumoineau.musicrange.repositories.ArtistDataRepository;
import com.chapeaumoineau.musicrange.repositories.SongDataRepository;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class MusicViewModel extends ViewModel {

    private final static int LOCAL = 0;
    private final static int SPOTIFY = 1;
    private final static int DEEZER = 2;
    private final static int AMAZONMUSIC = 3;
    private final static int SOUNDCLOUD = 4;

    private final SongDataRepository songDataSource;
    private final ArtistDataRepository artistDataSource;
    private final AlbumDataRepository albumDataSource;
    private final Executor myExecutor;

    private File FilesDir = null;

    private AtomicInteger OP_RUNNING = new AtomicInteger(0);

    public MusicViewModel(SongDataRepository songDataSource, ArtistDataRepository artistDataSource, AlbumDataRepository albumDataSource, Executor myExecutor) {
        this.songDataSource = songDataSource;
        this.artistDataSource = artistDataSource;
        this.albumDataSource = albumDataSource;
        this.myExecutor = myExecutor;
    }

    //public LiveData<Song> getSong(int songId) {return this.currentSong;}

    public LiveData<List<Song>> getAllSongs() {return songDataSource.getAllSongs();}

    public LiveData<List<Song>> getSongsByAlbum(int albumId) {return songDataSource.getSongsByAlbum(albumId);}

    public LiveData<List<Song>> getSongsByArtist(int artistId) {return songDataSource.getSongsByArtist(artistId);}

    public LiveData<List<Song>> getSongsBySource(int sourceId) {return songDataSource.getSongsBySource(sourceId);}

    public LiveData<Album> getAlbum(int albumId) {return albumDataSource.getAlbum(albumId);}

    public LiveData<List<Album>> getAllAlbums() {return albumDataSource.getAllAlbums();}

    public LiveData<List<AlbumWithContent>> getAllAlbumsWithContent() {return albumDataSource.getAllAlbumWithContent();}

    public LiveData<List<Album>> getAllAlbumsSortedByAZ() {return albumDataSource.getAllAlbumsSortedByAZ();}

    public LiveData<Artist> getArtist(int artistId) {return artistDataSource.getArtist(artistId);}

    public LiveData<List<Artist>> getAllArtists() {return artistDataSource.getAllArtists();}

    public LiveData<Integer> getIdByArtist(String artist) {return artistDataSource.getIdByArtist(artist);}

    public Integer getIntIdByArtist(String artist) {return artistDataSource.getIntIdByArtist(artist);}

    public LiveData<Integer> getIdByAlbum(String album) {return albumDataSource.getIdByAlbum(album);}

    public Integer getIntIdByAlbum(String album) {return albumDataSource.getIntIdByAlbum(album);}

    public LiveData<List<SongWithAll>> getSongsWithAllByAlbumId(int albumId) {return songDataSource.getSongsWithAllByAlbumId(albumId);}

    public void createSong(Song song) {
        myExecutor.execute(() -> {
            songDataSource.createSong(song);
        });
    }

    public void createSongWithAll(Song song, Artist artist, Album album) {
        myExecutor.execute(() -> {
            OP_RUNNING.incrementAndGet();
            if(!artistDataSource.artistExist(artist.getName())) {
                artistDataSource.createArtist(artist);
                while(artistDataSource.getIntIdByArtist(artist.getName()) == null);
            }
            if(!albumDataSource.albumExist(album.getTitle())) {
                albumDataSource.createAlbum(album);
                while(albumDataSource.getIntIdByAlbum(album.getTitle()) == null);

                byte[] thumbnail = new byte[0];
                try {
                    thumbnail = song.getThumbnail();
                } catch (InvalidDataException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnsupportedTagException e) {
                    e.printStackTrace();
                }

                if(thumbnail != null) {
                    try {
                        saveToInternalStorage(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length), 400, 400), albumDataSource.getIntIdByAlbum(album.getTitle()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        //System.out.println("BITMAP ERROR " + album.getTitle());
                    }
                }
            }
            song.setArtistId(artistDataSource.getIntIdByArtist(artist.getName()));
            song.setAlbumId(albumDataSource.getIntIdByAlbum(album.getTitle()));
            songDataSource.createSong(song);
        });
    }

    //COMPRESS AND SAVE THUMBNAIL INTO THE INTERNAL STORAGE
    private void saveToInternalStorage(Bitmap bitmapImage, int id){
        File myPath = new File(FilesDir.toString() + "/thumbnails");
        if(!myPath.exists()) myPath.mkdirs();
        File myFile = new File(myPath.toString() + "/" + id + ".jpg");
        FileOutputStream myOutputStream = null;
        try {
            myOutputStream = new FileOutputStream(myFile);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, myOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                myOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteSong(int songId) {
        myExecutor.execute(() -> {
            songDataSource.deleteSong(songId);
        });
    }

    public void updateSong(Song song) {
        myExecutor.execute(() -> {
            songDataSource.updateSong(song);
        });
    }

    public void createArtist(Artist artist) {
        myExecutor.execute(() -> {
            artistDataSource.createArtist(artist);
        });
    }

    public LiveData<Integer> createArtistWithCallBack(Artist artist) {
        myExecutor.execute(() -> {
            artistDataSource.createArtist(artist);
        });
        return artistDataSource.getIdByArtist(artist.getName());
    }

    public void deleteArtist(int artistId) {
        myExecutor.execute(() -> {
            artistDataSource.deleteArtist(artistId);
        });
    }

    public void updateArtist(Artist artist) {
        myExecutor.execute(() -> {
            artistDataSource.updateArtist(artist);
        });
    }

    public void createAlbum(Album album) {
        myExecutor.execute(() -> {
            albumDataSource.createAlbum(album);
        });
    }

    public LiveData<Integer> createAlbumWithCallBack(Album album) {
        myExecutor.execute(() -> {
            albumDataSource.createAlbum(album);
        });
        return artistDataSource.getIdByArtist(album.getTitle());
    }

    public void deleteAlbum(int albumId) {
        myExecutor.execute(() -> {
            albumDataSource.deleteAlbum(albumId);
        });
    }

    public void updateAlbum(Album album) {
        myExecutor.execute(() -> {
            albumDataSource.updateAlbum(album);
        });
    }

    public void setFilesDir(File path) {
        this.FilesDir = path;
    }

    public File getFilesDir() {
        return FilesDir;
    }

    public void resetOP_RUNNING() {
        OP_RUNNING.set(0);
    }

    public int getOP_RUNNING() {
        return OP_RUNNING.get();
    }

    public int incrOP_RUNNING() {
        return OP_RUNNING.incrementAndGet();
    }

    public int decrtOP_RUNNING() {
        return OP_RUNNING.decrementAndGet();
    }
}
