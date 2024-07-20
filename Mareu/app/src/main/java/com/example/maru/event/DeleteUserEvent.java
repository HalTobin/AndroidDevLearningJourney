package com.example.maru.event;

import com.example.maru.model.Meeting;

/**
 * Event fired when a user deletes an User
 */
public class DeleteUserEvent {

    /**
     * User to delete
     */
    public String user;

    /**
     * Constructor.
     * @param user
     */
    public DeleteUserEvent(String user) { this.user = user; }
}
