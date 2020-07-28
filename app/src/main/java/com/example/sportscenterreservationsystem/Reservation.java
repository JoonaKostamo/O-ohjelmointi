package com.example.sportscenterreservationsystem;

import java.time.LocalDateTime;

public class Reservation {
    LocalDateTime begins;
    LocalDateTime ends;
    int userId;
    int roomId;
    String reason;
    String sport;
    int id;

    public Reservation(LocalDateTime givenBegins, LocalDateTime givenEnds, int givenUserId, int givenRoomId, String givenReason, String givenSport, int givenId) {
        begins = givenBegins;
        ends = givenEnds;
        userId = givenUserId;
        roomId = givenRoomId;
        reason = givenReason;
        sport = givenSport;
        id = givenId;
    }
}
