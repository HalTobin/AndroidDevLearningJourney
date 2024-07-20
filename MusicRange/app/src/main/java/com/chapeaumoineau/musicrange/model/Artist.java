package com.chapeaumoineau.musicrange.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = @Index(value = {"name"}, unique = true))
public class Artist {

    //UNIQUE IDENTIFIER OF AN ARTIST
    @PrimaryKey (autoGenerate = true)
    int id;

    //NAME OF THE ARTIST
    String name;

    public Artist() {}

    @Ignore
    public Artist(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
