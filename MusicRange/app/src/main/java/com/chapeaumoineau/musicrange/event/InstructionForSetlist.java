package com.chapeaumoineau.musicrange.event;

import com.chapeaumoineau.musicrange.model.SongWithAll;

import java.util.List;

public class InstructionForSetlist {
    public List<SongWithAll> list;
    public int position;
    public int mode;

    public static final int LOADALBUM = 1;
    public static final int SETNEXT = 2;
    public static final int ADDTOQUEU = 3;
    public static final int MOVETONEXT = 4;
    public static final int MOVETOEND = 5;
    public static final int REMOVE = 6;
    public static final int FORCEPLAY = 7;

    public InstructionForSetlist(List<SongWithAll> list, int position, int mode) {
        this.list = list;
        this.position = position;
        this.mode = mode;
    }
}
