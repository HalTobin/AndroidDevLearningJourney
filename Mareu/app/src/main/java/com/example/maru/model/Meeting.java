package com.example.maru.model;

import android.graphics.Color;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

/**
 * Model object representing a Meeting
 */
public class Meeting implements Serializable {

    /** Identifier */
    private long id;

    /** Name */
    private String name;

    /** Room used for the meeting*/
    private String room;

    /** Date - long */
    private long dateLong;

    /** Date - Calender */
    private Calendar calendar = Calendar.getInstance();

    /** Color */
    private int colorInt;

    /** Adress */
    private List<String> users;

    /**
     * Constructor
     * Every parameters = null
     */
    public Meeting() {

    }

    /**
     * Constructor
     * @param id
     * @param name
     * @param room
     * @param date
     * @param colorInt
     * @params users
     */
    public Meeting(long id, String name, String room, Calendar date, int colorInt, List<String> users) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.dateLong = date.getTimeInMillis();
        this.calendar = date;
        this.colorInt = colorInt;
        this.users = users;
    }

    /**
     * Constructor
     * @param id
     * @param name
     * @param room
     * @param year
     * @param month
     * @param day
     * @param hour
     * @param min
     * @param colorHex
     * @params users
     */
    public Meeting(long id, String name, String room, int year, int month, int day, int hour, int min, String colorHex, List<String> users) {
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.MILLISECOND, 0);
        tempCalendar.set(year, month, day, hour, min);

        this.id = id;
        this.name = name;
        this.room = room;
        this.dateLong = tempCalendar.getTimeInMillis();
        this.calendar = tempCalendar;
        this.colorInt = Color.parseColor(colorHex);
        this.users = users;
    }

    /**
     * Constructor
     * @param id
     * @param name
     * @param room
     * @param timeStamp
     * @param colorInt
     * @params users
     */
    public Meeting(long id, String name, String room, long timeStamp, int colorInt, List<String> users) {
        this.id = id;
        this.name = name;
        this.room = room;
        this.dateLong = timeStamp;
        this.calendar.setTimeInMillis(timeStamp);
        this.colorInt = colorInt;
        this.users = users;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setDateLong(long timeStamp) {
        this.dateLong = timeStamp;
        this.calendar.setTimeInMillis(dateLong);
    }

    public long getTimeStamp() {
        return dateLong;
    }

    public void setCalendar(Calendar date) {
        this.calendar = date;
        this.dateLong = calendar.getTimeInMillis();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public int getDay() { return calendar.get(Calendar.DATE); }

    public int getHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMin() {
        return calendar.get(Calendar.MINUTE);
    }

    public void setYear(int year) {
        calendar.set(Calendar.YEAR, year);
    }

    public void setMonth(int month) {
        calendar.set(Calendar.MONTH, month);
    }

    public void setDay(int day) {
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    public void setHour(int hour) {
        calendar.set(Calendar.HOUR, hour);
    }

    public void setMin(int min) {
        calendar.set(Calendar.MINUTE, min);
    }

    public int getColorInt() {
        return colorInt;
    }

    public void setColorInt(int colorInt) {
        this.colorInt = colorInt;
    }

    public String getColorHex() {
        return Integer.toString(this.colorInt, 16);
    }

    public void setColorHex(String colorHex) {
        this.colorInt = Color.parseColor(colorHex);
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public String getTitleString() {
        String myString = String.format("%02d", this.getHour()) + "h" + String.format("%02d", this.getMin()) + " - " + this.getRoom() + " - " + this.getName();
        return myString;
    }

    public String getUsersString() {
        String myString = "";
        for (int c = 0; c < users.size(); c++) {
            if(c==users.size()-1) myString = myString + users.get(c);
            else myString = myString + users.get(c) + ", ";
        }
        return myString;
    }

    public boolean isDateEqual(Calendar toCompare) {
        boolean isEqual = true;
        if(this.getYear() != toCompare.get(Calendar.YEAR)) isEqual = false;
        if(this.getMonth() != toCompare.get(Calendar.MONTH)) isEqual = false;
        if(this.getDay() != toCompare.get(Calendar.DATE)) isEqual = false;
        return isEqual;
    }

    public boolean isAlreadyUsed(String newRoom, Calendar newDate) {
        boolean isEqual = true;
        if(!((this.getTimeStamp() - newDate.getTimeInMillis()) < 3600000 && (this.getTimeStamp() - newDate.getTimeInMillis()) > -3600000)) isEqual = false;
        if(!this.room.equals(newRoom)) isEqual = false;
        return isEqual;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(id, meeting.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

