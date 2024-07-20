package com.chapeaumoineau.musicrange.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class Source {

    //UNIQUE ID OF A SOURCE OF MUSIC
    @PrimaryKey (autoGenerate = true)
    int id;

    //NAME OF THE SOURCE OF MUSIC
    String name;

    //ICON ASSOCIATED TO THE SOURCE
    int icon;

    public Source() {}

    @Ignore
    public Source(int id, String name, int icon) {
        this.id = id;
        this.name = name;
        this.icon = icon;
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
