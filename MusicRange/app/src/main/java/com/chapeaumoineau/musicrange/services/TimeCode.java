package com.chapeaumoineau.musicrange.services;

import com.chapeaumoineau.musicrange.database.MusicDatabase;

import java.util.concurrent.TimeUnit;

public class TimeCode {

    private static volatile TimeCode INSTANCE;

    private TimeCode() {};

    public static TimeCode getInstance() {
        if(INSTANCE == null) {
            synchronized(MusicDatabase.class) {
                if(INSTANCE == null) {
                    INSTANCE = new TimeCode();
                }
            }
        }
        return INSTANCE;
    }

    public static String fromMillisTo_MinSec(int millis) {
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public static String fromMillisTo_HourMinSec(int millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) -
                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

}
