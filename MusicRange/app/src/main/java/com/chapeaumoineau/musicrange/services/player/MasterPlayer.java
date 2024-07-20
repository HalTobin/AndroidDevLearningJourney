package com.chapeaumoineau.musicrange.services.player;

import android.content.Context;
import android.content.SharedPreferences;

import com.chapeaumoineau.musicrange.model.SongWithAll;
import com.chapeaumoineau.musicrange.services.player.subPlayer.LocalPlayer;
import com.chapeaumoineau.musicrange.utils.playerValue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MasterPlayer {

    private List<SongWithAll> myCurrentSetList = null;
    private SongWithAll myCurrentSong = null;

    List<PlayerApi> mySubPlayers = new ArrayList<>();

    private MyPlayerListener playerListener = null;
    private MySetlistListener setlistListener = null;

    SharedPreferences sharedPreferences;

    public boolean isDefined() {
        return ((myCurrentSetList!=null) && (myCurrentSong!=null));
    }

    public void initSubPlayer() {
        mySubPlayers.add(new LocalPlayer());
        mySubPlayers.get(playerValue.LOCAL).setSubPlayerListener(() -> nextSong());
    }

    public void loadPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("setlist", MODE_PRIVATE);
        List<SongWithAll> fromPreferences = new ArrayList<SongWithAll>();
        Gson gson = new Gson();
        String listTemp = sharedPreferences.getString("SETLIST", "");
        int trackTemp = sharedPreferences.getInt("TRACK", 0);
        if(!listTemp.equals("")) {
            TypeToken<ArrayList<SongWithAll>> token = new TypeToken<ArrayList<SongWithAll>>(){};
            fromPreferences = gson.fromJson(listTemp, token.getType());
            try {
                this.setCurrentSetlist(fromPreferences, trackTemp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void play() {
        mySubPlayers.get(getCurrentPlayerIndex()).play();
    }

    public void pause() {
        mySubPlayers.get(getCurrentPlayerIndex()).pause();
    }

    public boolean isPlaying() {
        return mySubPlayers.get(myCurrentSong.getMySource().getId()-1).isPlaying();
    }

    public int getCurrentPlayerIndex() {
        return myCurrentSong.getMySource().getId()-1;
    }

    public void playOrPause() {
        if(this.myCurrentSetList.size() > 0) {
            if(isPlaying()) pause();
            else play();
        }
    }

    public void setCurrentTimeCode(int newTimeCode) {
        mySubPlayers.get(getCurrentPlayerIndex()).setTimeCode(newTimeCode);
    }

    public void setCurrentSong(int position) throws IOException {
        if(this.myCurrentSetList.size() > 0) {
            this.myCurrentSong = myCurrentSetList.get(position);
            this.mySubPlayers.get(this.myCurrentSong.getMySource().getId()-1).setSong(myCurrentSong.getMySong());
            if(this.playerListener!=null) this.playerListener.onSongSetted();
            if(this.setlistListener!=null) this.setlistListener.onSongSetted();
        }
        saveSong();
    }

    public SongWithAll getCurrentSong() {
        return myCurrentSong;
    }

    public void setCurrentSetlist(List<SongWithAll> setList, int position) throws IOException {
        this.myCurrentSetList = setList;
        this.setCurrentSong(position);
        if(this.setlistListener!=null) this.setlistListener.onSetlistSetted();
        saveSetlist();
    }

    public List<SongWithAll> getCurrentSetlist() {
        return this.myCurrentSetList;
    }

    public void setNextSong(SongWithAll newSong) {
        this.myCurrentSetList.add(getSetlistPosition()+1, newSong);
        setlistListener.onSetlistSetted();
        saveSetlist();
    }

    public void addToQueu(SongWithAll newSong) {
        this.myCurrentSetList.add(newSong);
        setlistListener.onSetlistSetted();
        saveSetlist();
    }

    public void removeFromQueu(int toRemove) throws IOException {
        if(getSetlistPosition() == toRemove) nextSong();
        this.myCurrentSetList.remove(toRemove);
        setlistListener.onSetlistSetted();
        saveSetlist();
    }

    public void moveToNext(int position) {
        SongWithAll temp = this.myCurrentSetList.get(position);
        this.myCurrentSetList.remove(position);
        this.myCurrentSetList.add(getSetlistPosition()+1, temp);
        setlistListener.onSetlistSetted();
    }

    public void moveToEnd(int position) {
        this.myCurrentSetList.add(this.myCurrentSetList.get(position));
        this.myCurrentSetList.remove(position);
        setlistListener.onSetlistSetted();
    }

    public int getSetlistPosition() {
        return this.myCurrentSetList.indexOf(getCurrentSong());
    }

    public int getSetlistSize() {
        return this.myCurrentSetList.size();
    }

    public int getCurrentPosition() {
        int temp = 0;
        if(myCurrentSong!=null) temp = mySubPlayers.get(getCurrentPlayerIndex()).getCurrentPosition();
        return temp;
    }

    public int getDuration() { return mySubPlayers.get(getCurrentPlayerIndex()).getDuration(); }

    public void prevSong() throws IOException {
        mySubPlayers.get(getCurrentPlayerIndex()).turnOff();
        if(this.getSetlistPosition() > 0) setCurrentSong(this.myCurrentSetList.indexOf(myCurrentSong)-1);
        else setCurrentSong(this.myCurrentSetList.size()-1);
        play();
    }

    public void nextSong() throws IOException {
        mySubPlayers.get(getCurrentPlayerIndex()).turnOff();
        if(this.getSetlistPosition() < (this.myCurrentSetList.size()-1)) setCurrentSong(this.myCurrentSetList.indexOf(myCurrentSong)+1);
        else setCurrentSong(0);
        play();
    }

    public void goTo(int position) throws IOException {
        setCurrentSong(position);
        play();
    }

    public void songEnded() throws IOException {
        nextSong();
    }

    public void saveSong() {
        if(this.getSetlistSize()>0) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("TRACK", this.getSetlistPosition());
            editor.apply();
        }
    }

    public void saveSetlist() {
        if(this.getSetlistSize()>0) {
            Gson gson = new Gson();
            String serialized = gson.toJson(this.getCurrentSetlist());
            System.out.println(serialized);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("SETLIST", serialized);
            editor.putInt("TRACK", this.getSetlistPosition());
            editor.apply();
        }
    }

    public void swapSetlist(int from, int to) {
        Collections.swap(this.myCurrentSetList, from, to);
    }

    public void setPlayerListener(MyPlayerListener listener) { this.playerListener = listener; }

    public void setSetlistListener(MySetlistListener listener) { this.setlistListener = listener; }

    public interface MyPlayerListener {
        void onSongSetted() throws IOException;
    }

    public interface MySetlistListener {
        void onSongSetted() throws IOException;
        void onSetlistSetted();
    }

}
