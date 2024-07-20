package com.chapeaumoineau.musicrange.database;

import android.content.ContentValues;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.OnConflictStrategy;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.chapeaumoineau.musicrange.R;
import com.chapeaumoineau.musicrange.database.dao.AlbumDao;
import com.chapeaumoineau.musicrange.database.dao.ArtistDao;
import com.chapeaumoineau.musicrange.database.dao.SongDao;
import com.chapeaumoineau.musicrange.model.Album;
import com.chapeaumoineau.musicrange.model.Artist;
import com.chapeaumoineau.musicrange.model.Song;
import com.chapeaumoineau.musicrange.model.Source;

@Database(entities = {Song.class, Artist.class, Album.class, Source.class}, version = 1, exportSchema = false)
public abstract class MusicDatabase extends RoomDatabase {

    private static volatile MusicDatabase INSTANCE;

    public abstract SongDao songDao();
    public abstract ArtistDao artistDao();
    public abstract AlbumDao albumDao();

    public static MusicDatabase getInstance(Context context) {
        if(INSTANCE == null) {
            synchronized(MusicDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MusicDatabase.class, "MyDatabase.db").addCallback(prepopulateDatabase()).build();
                }
            }
        }
        return INSTANCE;
    }

    private static Callback prepopulateDatabase() {
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                ContentValues contentValues = new ContentValues();
                contentValues.put("id", 1);
                contentValues.put("name", "Local");
                contentValues.put("icon", R.drawable.ic_source_local);
                db.insert("Source", OnConflictStrategy.IGNORE, contentValues);

                contentValues.put("id", 2);
                contentValues.put("name", "Spotify");
                contentValues.put("icon", R.drawable.ic_source_spotify);
                db.insert("Source", OnConflictStrategy.IGNORE, contentValues);

                contentValues.put("id", 3);
                contentValues.put("name", "Deezer");
                contentValues.put("icon", R.drawable.ic_source_deezer);
                db.insert("Source", OnConflictStrategy.IGNORE, contentValues);
            }
        };
    }
}
