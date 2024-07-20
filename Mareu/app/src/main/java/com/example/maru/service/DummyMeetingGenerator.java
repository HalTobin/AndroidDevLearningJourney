package com.example.maru.service;

import com.example.maru.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DummyMeetingGenerator {

    public static List<String> DUMMY_USERS = Arrays.asList(
            "admin@lamzon.com", "rh@lamzon.com", "comm@lamzone.com", "user1@lamzon.com", "user2@lamzon.com", "user3@lamzon.com", "user4@lamzon.com"
    );

    public static List<String> DUMMY_ROOMS = Arrays.asList(
            "A2", "A6", "A7", "B2", "B3", "B9", "C1", "C5", "C8", "D1"
    );

    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(1, "Point hebdomadaire", "A2", 2020, 11, 4, 9, 30, "#F84C44", DUMMY_USERS),
            new Meeting(2, "Achat matériel", "C5", 2020, 11, 7, 14, 0, "#2262AF", Arrays.asList(DUMMY_USERS.get(0), DUMMY_USERS.get(5), DUMMY_USERS.get(6))),
            new Meeting(3, "Point hebdomadaire", "A2", 2020, 11, 11, 14, 0, "#F84C44",  DUMMY_USERS),
            new Meeting(4, "Acceuil nouvelle recrue", "A7", 2020, 11, 12, 11, 0, "#DD3D48",  Arrays.asList(DUMMY_USERS.get(1), DUMMY_USERS.get(3), DUMMY_USERS.get(4))),
            new Meeting(5, "Point hebdomadaire", "A2", 2020, 11, 18, 9, 30, "#F84C44",  DUMMY_USERS),
            new Meeting(6, "Point avant fermeture pour Noël", "A2", 2020, 11, 18, 14, 0, "#DD3D48",  DUMMY_USERS),
            new Meeting(7, "Point pour la reprise", "A2", 2021, 0, 4, 9, 30, "#DD3D48",  DUMMY_USERS),
            new Meeting(8, "Recrutement de personnel", "B9", 2021, 0, 5, 14, 0, "#59BC14", Arrays.asList(DUMMY_USERS.get(0), DUMMY_USERS.get(1), DUMMY_USERS.get(3)))
    );

    static List<String> generateUsers() {
        return new ArrayList<>(DUMMY_USERS);
    }

    static List<String> generateRooms() {
        return new ArrayList<>(DUMMY_ROOMS);
    }

    static List<Meeting> generateMeetings() {
        return new ArrayList<>(DUMMY_MEETINGS);
    }
    }
