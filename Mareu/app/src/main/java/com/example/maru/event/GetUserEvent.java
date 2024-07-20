package com.example.maru.event;

/**
 * Event fired when a user select an User
 */
public class GetUserEvent {

    /**
     * User to select
     */
    public String user;

    /**
     * Constructor.
     * @param user
     */
    public GetUserEvent(String user) { this.user = user; }
}
