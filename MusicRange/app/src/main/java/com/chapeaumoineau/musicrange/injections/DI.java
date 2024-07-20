package com.chapeaumoineau.musicrange.injections;

import com.chapeaumoineau.musicrange.services.player.MasterPlayer;

/**
 * Dependency injector to get instance of services
 */
public class DI {

    private static MasterPlayer player = new MasterPlayer();

    public static MasterPlayer getPlayerApi() {
        return player;
    }

    public static MasterPlayer getNewInstancePlayerApi() {
        return new MasterPlayer();
    }

}
