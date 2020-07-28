package com.example.sportscenterreservationsystem;

import java.util.ArrayList;

public class Room {
    String name;
    String description;
    int id;
    ArrayList<Integer> reservations;

    public Room(String givenName, String givenDescription, int givenId, ArrayList<Integer> givenReservations) {
        name = givenName;
        description = givenDescription;
        id = givenId;
        reservations = givenReservations;
    }
}
