package com.example.maru.event;

/**
 * Event fired when a user select a Room
 */
public class GetRoomEvent {

    /**
     * Room to select
     */
    public String room;

    /**
     * Constructor.
     * @param room
     */
    public GetRoomEvent(String room) { this.room = room; }
}
