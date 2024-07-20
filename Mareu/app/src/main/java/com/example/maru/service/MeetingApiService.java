package com.example.maru.service;

import com.example.maru.model.Meeting;

import java.util.Calendar;
import java.util.List;

/**
 * Meeting API client
 */
public interface MeetingApiService {

    /**
     * Get all the Meetings
     * @return {@Link List}
     */
    List<Meeting> getMeetings();

    /**
     * Get all the Meetings of a specific date
     * @return {@Link List}
     */
    List<Meeting> getMeetingsByDate(Calendar toFilter);

    /**
     * Get all the Meetings of a specific room
     * @return {@Link List}
     */
    List<Meeting> getMeetingsByRoom(String room);

    /**
     * Get all the Rooms
     * @return {@Link List}
     */
    List<String> getRooms();

    /**
     * Get all the Users
     * @return {@Link List}
     */
    List<String> getUsers();

    /**
     * Delete a meeting
     * @param meeting
     */
    void deleteMeeting(Meeting meeting);

    /**
     * Create a meeting
     */
    void createMeeting(Meeting meeting);

    boolean isRoomAvailable(String newRoom, Calendar newDate);

    /**
     * Get the next available Id
     * @return
     */
    long getNextId();
}
