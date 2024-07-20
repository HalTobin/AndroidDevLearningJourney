package com.chapeaumoineau.musicrange.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

import com.google.gson.Gson;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Playlist {

    private String title;
    private long timestamp;
    private List<SongWithAll> list;

    public Playlist(String title, long timestamp,List<SongWithAll> list) {
        this.title = title;
        this.timestamp = timestamp;
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<SongWithAll> getList() {
        return list;
    }

    public void setList(List<SongWithAll> list) {
        this.list = list;
    }

    public static List<Playlist> getAllPlaylists(Context context) {
        List<Playlist> myPlaylists = new ArrayList<>();

        String path = context.getFilesDir().toString() + "/playlists";
        File directory = new File(path);
        File[] files = directory.listFiles();
        if(files != null) {
            for (int c = 0; c < files.length; c++)
            {
                myPlaylists.add(readSavedData(path + "/" + files[c].getName()));
            }
        }

        return myPlaylists;
    }

    public static Playlist readSavedData(String path) {
        StringBuffer myStringBuffer = new StringBuffer("");
        Playlist playlist = null;
        try {
            File myFile = new File(path);
            FileInputStream fIn = new FileInputStream(myFile);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader buffreader = new BufferedReader(isr);
            String readString = buffreader.readLine();
            while (readString != null) {
                myStringBuffer.append(readString);
                readString = buffreader.readLine();
            }
            Gson g = new Gson();
            playlist = g.fromJson(myStringBuffer.toString(), Playlist.class);
            isr.close();
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }

        return playlist;
    }

    public static void savePlaylist(Playlist myPlaylist, Context context) {
        File myPath = new File(context.getFilesDir().toString() + "/playlists");
        Gson gson = new Gson();
        String mySerializedPlaylist = gson.toJson(myPlaylist);
        if(!myPath.exists()) myPath.mkdirs();
        File myFile = new File(myPath.toString() + "/" + myPlaylist.getTimestamp() + ".json");
        FileOutputStream myOutputStream = null;
        try {
            myOutputStream = new FileOutputStream(myFile, false);
            myOutputStream.write(mySerializedPlaylist.getBytes(Charset.defaultCharset()));
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

    public static void createAutoThumbnail(Playlist myPlaylist, Context context) throws InvalidDataException, IOException, UnsupportedTagException {
        List<Integer> albumsId = new ArrayList<>();
        List<SongWithAll> tracklist = myPlaylist.getList();
        List<byte[]> myThumbnails = new ArrayList<>();

        for(int c = 0; c < myPlaylist.getList().size(); c++) {
            SongWithAll tempSongWithAll = tracklist.get(c);
            if(!albumsId.contains(tempSongWithAll.getMyAlbum().getId()) && (tempSongWithAll.getMySong().getThumbnail() != null)) {
                albumsId.add(tempSongWithAll.getMyAlbum().getId());
                myThumbnails.add(tempSongWithAll.getMySong().getThumbnail());
            }
        }

        Bitmap images[] = new Bitmap[albumsId.size()];
        Bitmap myAutoThumbnail = Bitmap.createBitmap(1500, 1500, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(myAutoThumbnail);

        int nbThumbnail = images.length;
        if (nbThumbnail > 4) nbThumbnail = 4;
        switch (nbThumbnail) {
            case 0 :

                break;
            case 1 :
                myAutoThumbnail = BitmapFactory.decodeByteArray(myThumbnails.get(0), 0, myThumbnails.get(0).length);
                break;
            case 2 :
                canvas.drawBitmap(splitAndResizeImage(myThumbnails.get(0)), 0, 0, null);
                canvas.drawBitmap(splitAndResizeImage(myThumbnails.get(1)), 0, 750, null);
                break;
            case 3 :
                canvas.drawBitmap(resizeImage(myThumbnails.get(0)), 0, 0, null);
                canvas.drawBitmap(resizeImage(myThumbnails.get(1)), 750, 0, null);
                canvas.drawBitmap(splitAndResizeImage(myThumbnails.get(2)), 0, 750, null);
                break;
            case 4 :
                canvas.drawBitmap(resizeImage(myThumbnails.get(0)), 0, 0, null);
                canvas.drawBitmap(resizeImage(myThumbnails.get(1)), 750, 0, null);
                canvas.drawBitmap(resizeImage(myThumbnails.get(2)), 0, 750, null);
                canvas.drawBitmap(resizeImage(myThumbnails.get(3)), 750, 750, null);
                break;

        }

        File myPath = new File(context.getFilesDir().toString() + "/thumbnails");
        if(!myPath.exists()) myPath.mkdirs();
        File myFile = new File(myPath.toString() + "/playlist_" + myPlaylist.getTimestamp() + ".jpg");
        if(myFile.exists()) myFile.delete();
        FileOutputStream myOutputStream = null;
        try {
            myOutputStream = new FileOutputStream(myFile, false);
            myAutoThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, myOutputStream);
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

    private static Bitmap splitAndResizeImage(byte[] thumbnail) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 1500, 1500, true);
        return Bitmap.createScaledBitmap(Bitmap.createBitmap(scaledBitmap, 0, 0, 1500, 750), 1500, 750, true);
    }

    private static Bitmap resizeImage(byte[] thumbnail) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(thumbnail, 0, thumbnail.length);
        return Bitmap.createScaledBitmap(bitmap, 750, 750, true);
    }
}
